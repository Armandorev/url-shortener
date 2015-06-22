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

import static org.hamcrest.Matchers.equalTo;
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
        shortenerService.clear();
    }

    @Test
    public void shouldExposeAListOfTheAllURIsShortened() throws Exception {
        URL resourceURL = new URL("http://localhost:" + port + "/api/all");

        List<ShortenerResponse> response = OBJECT_MAPPER.readValue(resourceURL, new TypeReference<List<ShortenerResponse>>() {});
        MatcherAssert.assertThat(response, hasSize(0));

        shortenerService.shorten(new URI("http://www.pivotal.io"));
        shortenerService.shorten(new URI("http://www.pivotallabs.com"));

        List<ShortenerResponse> newResponse = OBJECT_MAPPER.readValue(resourceURL, new TypeReference<List<ShortenerResponse>>() {});
        MatcherAssert.assertThat(newResponse, hasSize(2));
    }

    @Test
    public void shouldAddShortenedURLWhenPostedTo() throws Exception {
        URI newURI = new URI("https://run.pivotal.io/");
        HttpURLConnection conn = post(newURI);

        ShortenerResponse response = OBJECT_MAPPER.readValue(conn.getInputStream(), new TypeReference<ShortenerResponse>() {});
        MatcherAssert.assertThat(response.getOriginal(), equalTo(newURI));
        MatcherAssert.assertThat(response.getShortened().toString(), equalTo(ShortenerService.SHORTENER_HOST + "a"));
    }

    public HttpURLConnection post(URI urlPostParam) throws IOException, URISyntaxException {
        URL url = new URL("http://localhost:" + port + "/api/shorten");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        byte[] postData = urlPostParam.toString().getBytes(Charset.forName("UTF-8"));
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
