package benjamin.groehbiel.ch;

import benjamin.groehbiel.ch.shortener.WordRepository;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        new SpringApplicationBuilder(Application.class)
                .initializers(new PersistenceInitializer())
                .run(args);
    }

    @Bean
    public WordRepository shortenerRepository() {
        return new WordRepository();
    }

}
