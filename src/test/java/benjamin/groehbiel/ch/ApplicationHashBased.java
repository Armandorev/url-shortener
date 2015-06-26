package benjamin.groehbiel.ch;

import benjamin.groehbiel.ch.shortener.ShortenerRepository;
import benjamin.groehbiel.ch.shortener.hash.HashBasedRepository;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ApplicationHashBased {

    @Bean
    public ShortenerRepository shortenerRepository() {
        return new HashBasedRepository();
    }

}
