import benjamin.groehbiel.ch.Application;
import benjamin.groehbiel.ch.ShortenedURIResponse;
import benjamin.groehbiel.ch.shortener.ShortenerService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.logging.Logger;

import static org.hamcrest.Matchers.hasSize;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest("server.port:0")
public class APIControllerTest {

    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Autowired
    ShortenerService shortenerService;

    @Value("${local.server.port}")
    int port;

    @Before
    public void before() {
        Logger.getLogger("APIController.class").info("Running tests on port " + port);
    }

    @Test
    public void shouldExposeAListOfTheAllURIsShortened() throws Exception {
        URL resourceURL = new URL("http://localhost:" + port + "/api/all");

        List<ShortenedURIResponse> response = OBJECT_MAPPER.readValue(resourceURL, new TypeReference<List<ShortenedURIResponse>>() {});
        MatcherAssert.assertThat(response, hasSize(0));

        shortenerService.shorten(new URI("http://www.pivotal.io"));
        shortenerService.shorten(new URI("http://www.pivotallabs.com"));

        List<ShortenedURIResponse> newResponse = OBJECT_MAPPER.readValue(resourceURL, new TypeReference<List<ShortenedURIResponse>>() {});
        MatcherAssert.assertThat(newResponse, hasSize(2));
    }

}
