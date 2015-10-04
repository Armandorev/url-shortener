package benjamin.groehbiel.ch.e2e;

import benjamin.groehbiel.ch.ApplicationTest;
import benjamin.groehbiel.ch.PersistenceInitializer;
import benjamin.groehbiel.ch.shortener.db.DictionaryManager;
import benjamin.groehbiel.ch.shortener.wordnet.WordDefinition;
import benjamin.groehbiel.ch.shortener.wordnet.WordNetHelper;
import org.fluentlenium.adapter.FluentTest;
import org.fluentlenium.core.domain.FluentList;
import org.fluentlenium.core.domain.FluentWebElement;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.MatcherAssert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.io.IOException;
import java.util.List;

import static org.hamcrest.Matchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ApplicationTest.class, initializers = PersistenceInitializer.class)
@WebAppConfiguration
@IntegrationTest("server.port:0")
public class LandingPageTest extends FluentTest {

    @Value("${local.server.port}")
    int port;

    // TODO: reuse DatabaseTest
    @Autowired
    private DictionaryManager dictionaryManager;

    @Autowired
    private StringRedisTemplate redis;

    @Before
    public void populateTable() throws IOException {
        List<WordDefinition> words = WordNetHelper.load("src/test/resources/WordNet/");
        dictionaryManager.fill(WordNetHelper.turnIntoDictionaryHashes(words));
        System.out.println("Init database " + dictionaryManager.getWordsAvailableSize());
    }

    @After
    public void flushTable() {
        dictionaryManager.clear();
        redis.getConnectionFactory().getConnection().flushDb();
    }
    // END TODO

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

    private Matcher<? super FluentWebElement> hasAttribute(String attribute) {
        Matcher<FluentWebElement> matcher = new BaseMatcher<FluentWebElement>() {
            @Override
            public void describeTo(Description description) {
            }

            @Override
            public boolean matches(Object item) {
                FluentWebElement button = (FluentWebElement) item;
                String disabledAttr = button.getAttribute(attribute);
                if (disabledAttr == null) {
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public void describeMismatch(Object item, Description mismatchDescription) {
                mismatchDescription.appendText("Could not find attribute " + attribute);
            }
        };

        return matcher;
    }

}
