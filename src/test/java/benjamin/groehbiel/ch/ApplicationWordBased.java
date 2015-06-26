package benjamin.groehbiel.ch;

import benjamin.groehbiel.ch.shortener.ShortenerRepository;
import benjamin.groehbiel.ch.shortener.word.WordBasedRepository;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ApplicationWordBased {

    @Bean
    public ShortenerRepository shortenerRepository() {
        return new WordBasedRepository();
    }

}
