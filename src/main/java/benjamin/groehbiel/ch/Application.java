package benjamin.groehbiel.ch;

import benjamin.groehbiel.ch.shortener.ShortenerRepository;
import benjamin.groehbiel.ch.shortener.word.WordBasedRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public ShortenerRepository shortenerRepository() {
        return new WordBasedRepository();
    }

}
