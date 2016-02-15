package it.w0rd.e2e;

import it.w0rd.Application;
import it.w0rd.persistence.PersistenceInitializer;
import it.w0rd.persistence.WordNetHelper;
import it.w0rd.persistence.db.DictionaryManager;
import it.w0rd.persistence.redis.RedisManager;
import org.fluentlenium.adapter.FluentTest;
import org.fluentlenium.core.domain.FluentList;
import org.fluentlenium.core.domain.FluentWebElement;
import org.hamcrest.MatcherAssert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.io.IOException;

import static org.hamcrest.Matchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class, initializers = PersistenceInitializer.class)
@IntegrationTest("server.port:0")
@WebAppConfiguration
public class ShortenerScreenTest extends FluentTest {

    @Value("${local.server.port}")
    int port;

    @Autowired
    private DictionaryManager dictionaryManager;

    @Autowired
    private RedisManager redisManager;

    @Before
    public void setup() throws IOException {
        dictionaryManager.fill(WordNetHelper.loadDirectory("WordNet"));
    }

    @After
    public void flushTable() {
        wipeData();
    }

    @Test
    public void showsSuccessAndWordDescription() {
        goTo("http://localhost:" + port);
        fill(".control__input").with("http://www.pivotal.io");
        click(".control__button");

        await();

        FluentList<FluentWebElement> descriptions = find(".bubble");
        MatcherAssert.assertThat(descriptions, hasSize(1));
        MatcherAssert.assertThat(descriptions.get(0).isDisplayed(), equalTo(true));
        MatcherAssert.assertThat(descriptions.get(0).getText(), containsString("(usually followed by `to') having the necessary means or skill or know-how or authority to do something; \"able to swim\"; \"she was able to program her computer\"; \"we were at last able to buy a car\"; \"able to get a grant for the project\""));
    }

    @Test
    public void shouldPrependMissingProtocol() {
        goTo("http://localhost:" + port);
        fill(".control__input").with("www.pivotal.io");
        String modifiedUrl = findFirst(".control__input").getValue();
        MatcherAssert.assertThat(modifiedUrl, equalTo("http://www.pivotal.io"));
    }

    private void wipeData() {
        dictionaryManager.clear();
        redisManager.clear();
    }

}
