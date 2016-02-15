package it.w0rd;

import it.w0rd.persistence.PersistenceInitializer;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        new SpringApplicationBuilder(Application.class)
                .initializers(new PersistenceInitializer())
                .run(args);
    }

}
