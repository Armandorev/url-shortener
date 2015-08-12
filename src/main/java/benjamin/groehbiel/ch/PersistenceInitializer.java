package benjamin.groehbiel.ch;

import io.pivotal.labs.cfenv.CloudFoundryEnvironment;
import io.pivotal.labs.cfenv.CloudFoundryEnvironmentException;
import io.pivotal.labs.cfenv.CloudFoundryService;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

public class PersistenceInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        try {
            configure();
        } catch (CloudFoundryEnvironmentException e) {
            throw new IllegalStateException(e);
        }
    }

    private void configure() throws CloudFoundryEnvironmentException {
        System.out.println("init persistence");
        CloudFoundryEnvironment environment = new CloudFoundryEnvironment(System::getenv);
        CloudFoundryService redisService = environment.getService("pcf-redis");

        String redisHost = (String) redisService.getCredentials().get("host");
        String redisPassword = (String) redisService.getCredentials().get("password");
        Integer redisPort = (Integer) redisService.getCredentials().get("port");

        System.setProperty("spring.redis.host", redisHost);
        System.setProperty("spring.redis.password", redisPassword);
        System.setProperty("spring.redis.port", redisPort.toString());
    }

}

