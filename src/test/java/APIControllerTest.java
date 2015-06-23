import benjamin.groehbiel.ch.Application;
import benjamin.groehbiel.ch.ShortenerResponse;
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
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;
import java.util.logging.Logger;

import static org.hamcrest.Matchers.*;

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

    String host;

    @Before
    public void before() {
        host = "http://localhost:" + port;
        Logger.getLogger("APIController.class").info("Running tests on " + host);
        shortenerService.clear();
    }

    @Test
    public void shouldExposeAListOfTheAllURIsShortened() throws Exception {
        URL resourceURL = new URL(host + "/api/all");

        List<ShortenerResponse> response = OBJECT_MAPPER.readValue(resourceURL, new TypeReference<List<ShortenerResponse>>() {});
        MatcherAssert.assertThat(response, hasSize(0));

        URI urlPivotal = new URI("http://www.pivotal.io");
        URI urlLabs = new URI("http://www.pivotallabs.com");
        shortenerService.shorten(urlPivotal);
        shortenerService.shorten(urlLabs);

        List<ShortenerResponse> newResponse = OBJECT_MAPPER.readValue(resourceURL, new TypeReference<List<ShortenerResponse>>() {});
        MatcherAssert.assertThat(newResponse, hasSize(2));

        MatcherAssert.assertThat(newResponse, containsInAnyOrder(
                new ShortenerResponse(urlPivotal, new URI(ShortenerService.SHORTENER_HOST + "a")),
                new ShortenerResponse(urlLabs, new URI(ShortenerService.SHORTENER_HOST + "b"))
        ));
    }

    @Test
    public void shouldAddShortenedURLWhenPostedTo() throws Exception {
        String validURL = "https://run.pivotal.io/";
        HttpURLConnection conn = post(validURL);

        ShortenerResponse response = OBJECT_MAPPER.readValue(conn.getInputStream(), new TypeReference<ShortenerResponse>() {});
        MatcherAssert.assertThat(response.getOriginal().toString(), equalTo(validURL));
        MatcherAssert.assertThat(response.getShortened().toString(), equalTo(ShortenerService.SHORTENER_HOST + "a"));
    }

    @Test
    public void shouldGetErrorMessageWhenSubmittingAnInvalidURL() throws Exception {
        String invalidURL = "htp://test.com";
        HttpURLConnection   conn = post(invalidURL);
        MatcherAssert.assertThat(conn.getResponseCode(), equalTo(500));
    }

    /**
     * @param postParams has the form param1=value1&param2=value2
     * @return connection handle
     * @throws IOException
     * @throws URISyntaxException
     */
    public HttpURLConnection post(String postParams) throws IOException, URISyntaxException {
        URL url = new URL("http://localhost:" + port + "/api/shorten");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        byte[] postData = postParams.getBytes(Charset.forName("UTF-8"));
        int postDataLength = postData.length;

        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        conn.setRequestProperty("charset", Charset.forName("UTF-8").toString());
        conn.setRequestProperty("Content-Length", Integer.toString(postDataLength));
        conn.setUseCaches(false);

        conn.connect();

        try (DataOutputStream wr = new DataOutputStream(conn.getOutputStream())) {
            wr.write(postData);
        }
        return conn;
    }

}
