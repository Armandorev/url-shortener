package benjamin.groehbiel.ch.E2E;

import org.fluentlenium.core.domain.FluentList;
import org.fluentlenium.core.domain.FluentWebElement;
import org.fluentlenium.core.filter.Filter;
import org.hamcrest.MatcherAssert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Value;

import java.net.URI;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;

public class HomeScreenTest extends SpringTestFluentleniumHashBased {

    @Value("${local.server.port}")
    int port;

    @Test
    public void displaysAFormWithAnURLInput() {
        goTo("http://localhost:" + port);
        FluentList<FluentWebElement> fluentWebElements = find("#urlForm .url", new Filter[]{});
        MatcherAssert.assertThat(fluentWebElements.size(), equalTo(1));
    }

    @Test
    public void submittingAValidURLReturnsAShortenedURL() throws InterruptedException {
        goTo("http://localhost:" + port);
        fill("#urlForm .url").with("http://www.pivotal.io");
        click("#urlForm button");

        await();

        FluentWebElement successMessage = find("p.success", new Filter[]{}).get(0);
        MatcherAssert.assertThat(successMessage.getText(), containsString("is your shortened URL"));
    }

    @Test
    public void submittingAInvalidURLShowsAnError() throws Exception {
        goTo("http://localhost:" + port);
        fill("#urlForm .url").with("htp://www.pivotal.io");
        click("#urlForm button");

        await();

        FluentWebElement errorMessage = find("p.error", new Filter[]{}).get(0);
        MatcherAssert.assertThat(errorMessage.getText(), containsString("I hate to say"));
    }

    @Test
    public void counterIsShownDenotingTheNumberOfShortenedURLs() throws Exception {
        shortenerService.shorten(new URI("http://pivotal.io"));
        goTo("http://localhost:" + port);
        FluentWebElement statsElement = find(".stats", new Filter[]{}).get(0);
        MatcherAssert.assertThat(statsElement.getText(), equalTo("1 shortened URLs."));
    }

}
