package benjamin.groehbiel.ch;

import io.pivotal.labs.cfenv.CloudFoundryEnvironment;
import io.pivotal.labs.cfenv.CloudFoundryEnvironmentException;
import io.pivotal.labs.cfenv.CloudFoundryService;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

import java.net.URI;
import java.net.URISyntaxException;

public class PersistenceInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

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
    }

    private void setupEnvironment() {
        System.setProperty("app.host", System.getenv("APP_HOST"));
        System.setProperty("app.protocol", System.getenv("APP_PROTOCOL"));
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
    }

    private void setupRedis(CloudFoundryEnvironment environment) {
        CloudFoundryService redisService = environment.getService("pcf-redis");
        String redisHost = (String) redisService.getCredentials().get("host");
        String redisPassword = (String) redisService.getCredentials().get("password");
        Integer redisPort = (Integer) redisService.getCredentials().get("port");

        System.setProperty("redis.host", redisHost);
        System.setProperty("redis.password", redisPassword);
        System.setProperty("redis.port", redisPort.toString());
    }

}

