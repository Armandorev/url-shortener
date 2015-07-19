package benjamin.groehbiel.ch;

import benjamin.groehbiel.ch.shortener.ShortenerRepository;
import benjamin.groehbiel.ch.shortener.word.WordRepository;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ApplicationTest {

    @Bean
    public ShortenerRepository shortenerRepository() {
        return new WordRepository();
    }

}
