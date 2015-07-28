package benjamin.groehbiel.ch.e2e;

import benjamin.groehbiel.ch.ApplicationTest;
import benjamin.groehbiel.ch.SpringTest;
import benjamin.groehbiel.ch.shortener.ShortenerRepository;
import benjamin.groehbiel.ch.shortener.word.EnglishDictionary;
import benjamin.groehbiel.ch.shortener.word.WordDefinition;
import org.fluentlenium.adapter.FluentTest;
import org.fluentlenium.core.domain.FluentList;
import org.fluentlenium.core.domain.FluentWebElement;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.MatcherAssert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.LinkedList;
import java.util.List;

import static org.hamcrest.Matchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ApplicationTest.class)
@WebAppConfiguration
@IntegrationTest("server.port:0")
public class LandingPageTest extends FluentTest {

    @Autowired
    private ShortenerRepository shortenerRepository;

    @Autowired
    private EnglishDictionary englishDictionary;

    @Before
    public void clearRepository() {
        shortenerRepository.clear();

        List<WordDefinition> words = new LinkedList<>();
        words.add(new WordDefinition("fun", "enjoyment, amusement, or light-hearted pleasure."));
        words.add(new WordDefinition("eloquence", "fluent or persuasive speaking or writing."));
        words.add(new WordDefinition("elephant", "a very large plant-eating mammal with a prehensile trunk, long curved ivory tusks, and large ears, native to Africa and southern Asia."));
        englishDictionary.set(words);
    }

    @Value("${local.server.port}")
    int port;

    @Test
    public void showsSuccessAndWordDescription() {
        goTo("http://localhost:" + port);
        fill("#urlForm .url").with("http://www.pivotal.io");
        click("#urlForm button");

        await();

        FluentList<FluentWebElement> descriptions = find(".success .description");
        MatcherAssert.assertThat(descriptions, hasSize(1));
        MatcherAssert.assertThat(descriptions.get(0).isDisplayed(), equalTo(true));
        MatcherAssert.assertThat(descriptions.get(0).getText(), containsString("enjoyment, amusement, or light-hearted pleasure."));
    }

    @Test
    public void makeSuccessMessageClosable() {
        goTo("http://localhost:" + port);
        fill("#urlForm .url").with("http://www.pivotal.io");
        click("#urlForm button");

        await();

        FluentWebElement searchButton = findFirst("#urlForm button");
        MatcherAssert.assertThat(searchButton, hasAttribute("disabled"));

        FluentWebElement closeIcon = findFirst(".success .close");
        closeIcon.click();

        MatcherAssert.assertThat(searchButton, not(hasAttribute("disabled")));
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
