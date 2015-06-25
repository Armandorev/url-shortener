package benjamin.groehbiel.ch.E2E;

import org.fluentlenium.core.domain.FluentList;
import org.fluentlenium.core.domain.FluentWebElement;
import org.fluentlenium.core.filter.Filter;
import org.hamcrest.MatcherAssert;
import org.junit.Test;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Value;

import java.util.concurrent.TimeUnit;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;

public class HomeScreenTest extends SpringTestFluentlenium {

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
        MatcherAssert.assertThat(successMessage.getText(), containsString("has been copied"));
    }

    public void waitForAngular() {
        String script = "angular.getTestability(document.querySelector('body')).whenStable(arguments[0]);";
        ((JavascriptExecutor) getDriver()).executeAsyncScript(script);
    }

    @Override
    public WebDriver getDefaultDriver() {
        WebDriver driver = super.getDefaultDriver();
        driver.manage().timeouts().setScriptTimeout(6, TimeUnit.SECONDS);
        return driver;
    }

}
