package benjamin.groehbiel.ch;

import benjamin.groehbiel.ch.ApplicationHashBased;
import benjamin.groehbiel.ch.shortener.ShortenerRepository;
import benjamin.groehbiel.ch.shortener.ShortenerService;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ApplicationHashBased.class)
@WebAppConfiguration
@IntegrationTest("server.port:0")
public abstract class SpringHashBasedApplicationTest {

    //TODO: this needs to be refreshed for each test.
    @Autowired
    private ShortenerRepository shortenerRepository;

    @Before
    public void clearRepository() {
        shortenerRepository.clear();
    }

}
