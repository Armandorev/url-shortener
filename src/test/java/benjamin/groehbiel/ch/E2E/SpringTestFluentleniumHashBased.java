package benjamin.groehbiel.ch.E2E;

import benjamin.groehbiel.ch.Application;
import benjamin.groehbiel.ch.ApplicationHashBased;
import benjamin.groehbiel.ch.shortener.ShortenerRepository;
import benjamin.groehbiel.ch.shortener.ShortenerService;
import org.fluentlenium.adapter.FluentTest;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Autowired
    ShortenerService shortenerService;

    @Value("${local.server.port}")
    int port;

    String host;

    @Before
    public void before() {
        host = "http://localhost:" + port;
        shortenerRepository.clear();
    }

}
