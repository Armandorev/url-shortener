package benjamin.groehbiel.ch;

import benjamin.groehbiel.ch.shortener.ShortenerRepository;
import benjamin.groehbiel.ch.shortener.word.WordBasedRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public ShortenerRepository shortenerRepository() {
        return new WordBasedRepository();
    }

    @Bean
    public FilterRegistrationBean redirectFilterRegister(RedirectFilter redirectFilter) {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(redirectFilter);
        registration.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return registration;
    }

}
