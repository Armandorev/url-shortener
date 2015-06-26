package benjamin.groehbiel.ch.E2E.hash;

import benjamin.groehbiel.ch.ApplicationHashBased;
import benjamin.groehbiel.ch.shortener.ShortenerRepository;
import org.fluentlenium.adapter.FluentTest;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ApplicationHashBased.class)
@WebAppConfiguration
@IntegrationTest("server.port:0")
public abstract class SpringTestFluentleniumHashBased extends FluentTest {

    @Autowired
    ShortenerRepository shortenerRepository;

}
