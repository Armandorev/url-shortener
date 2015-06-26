package benjamin.groehbiel.ch.E2E;

import org.fluentlenium.core.domain.FluentList;
import org.fluentlenium.core.domain.FluentWebElement;
import org.hamcrest.MatcherAssert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Value;

import static org.hamcrest.Matchers.*;

public class HomeScreenWordBased extends SpringTestFluentleniumWordBased {

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

}
