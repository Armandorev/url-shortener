package benjamin.groehbiel.ch.E2E;

import org.fluentlenium.core.domain.FluentList;
import org.fluentlenium.core.domain.FluentWebElement;
import org.fluentlenium.core.filter.Filter;
import org.hamcrest.MatcherAssert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Value;

import static org.hamcrest.CoreMatchers.equalTo;

public class HomeScreenTest extends SpringTestFluentlenium {

    @Value("${local.server.port}")
    int port;

    @Test
    public void displaysAForm() {
        goTo("http://localhost:" + port);

        FluentList<FluentWebElement> fluentWebElements = find(".container", new Filter[]{});
        MatcherAssert.assertThat(fluentWebElements.size(), equalTo(1));
    }

}
