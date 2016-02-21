package it.w0rd;

import io.pivotal.labs.cfenv.CloudFoundryEnvironment;
import io.pivotal.labs.cfenv.CloudFoundryEnvironmentException;
import io.pivotal.labs.cfenv.CloudFoundryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

import java.net.URI;
import java.net.URISyntaxException;

public class ApplicationInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    public static final Logger LOGGER = LoggerFactory.getLogger(ApplicationInitializer.class);

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        try {
            configure();
        } catch (CloudFoundryEnvironmentException | URISyntaxException e) {
            throw new IllegalStateException(e);
        }
    }

    private void configure() throws CloudFoundryEnvironmentException, URISyntaxException {
        CloudFoundryEnvironment environment = new CloudFoundryEnvironment(System::getenv);
        setupRedis(environment);
        setupPostgres(environment);
        setupEnvironment();
        setupAdminCredentials();
    }

    private void setupEnvironment() {
        String appHost = System.getenv("W0RDIT_HOST");
        System.setProperty("app.host", appHost);

        String appProtocol = System.getenv("W0RDIT_PROTOCOL");
        System.setProperty("app.protocol", appProtocol);

        LOGGER.info("App environment initialised. host: {}, app protocol: {}", appHost, appProtocol);
    }

    private void setupAdminCredentials() {
        String username = System.getenv("W0RDIT_ADMIN_USERNAME");
        if (username == null || username.isEmpty()) {
            username = "admin";
            LOGGER.warn("Using default username");
        }
        System.setProperty("admin.username", username);

        String password = System.getenv("W0RDIT_ADMIN_PASSWORD");
        if (password == null || password.isEmpty()) {
            password = "default";
            LOGGER.warn("Using default password");
        }
        System.setProperty("admin.password", password);

        LOGGER.info("Admin Authentication initialised with with username {}", username);
    }

    private void setupPostgres(CloudFoundryEnvironment environment) throws URISyntaxException {
        CloudFoundryService postgresService = environment.getService("postgres-db");

        URI dbUri = postgresService.getUri();
        String host = "jdbc:postgresql://" + dbUri.getHost() + dbUri.getPath();
        String[] usernameAndPassword = dbUri.getUserInfo().split(":", 2);
        String user = usernameAndPassword[0];
        String password = usernameAndPassword[1];

        System.setProperty("spring.datasource.url", host);
        System.setProperty("spring.datasource.username", user);
        System.setProperty("spring.datasource.password", password);

        String maxConns = (String) postgresService.getCredentials().get("max_conns");
        System.setProperty("spring.datasource.max-active", maxConns);

        LOGGER.info("Connecting to Postgres on host {}", host, user);
    }

    private void setupRedis(CloudFoundryEnvironment environment) {
        CloudFoundryService redisService = environment.getService("pcf-redis");
        String redisHost = (String) redisService.getCredentials().get("host");
        String redisPassword = (String) redisService.getCredentials().get("password");
        Integer redisPort = (Integer) redisService.getCredentials().get("port");

        System.setProperty("redis.host", redisHost);
        System.setProperty("redis.password", redisPassword);
        System.setProperty("redis.port", redisPort.toString());

        LOGGER.info("Connecting to Redis on host {}:{}", redisHost, redisPort.toString());
    }

}

