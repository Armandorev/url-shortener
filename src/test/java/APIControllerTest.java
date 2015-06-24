import benjamin.groehbiel.ch.Application;
import benjamin.groehbiel.ch.ShortenerResponse;
import benjamin.groehbiel.ch.shortener.ShortenerRequest;
import benjamin.groehbiel.ch.shortener.ShortenerService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.MatcherAssert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
    @Ignore
    // TODO: Figure out how to post to REST api using serialisation of ShortenerRequest.
    public void shouldAddShortenedURLWhenPostedTo() throws IOException, URISyntaxException, ClassNotFoundException {
        ShortenerRequest request = new ShortenerRequest("https://run.pivotal.io/");

        HttpURLConnection conn = null;

        conn = post(request);

        ObjectInputStream ois = new ObjectInputStream(conn.getInputStream());
        ShortenerResponse response = (ShortenerResponse) ois.readObject();

        System.out.println(response.getOriginal() + " " + response.getShortened());
        ois.close();

//        ShortenerResponse response = OBJECT_MAPPER.readValue(new ObjectInputStream(conn.getInputStream()), new TypeReference<ShortenerResponse>() {});
        MatcherAssert.assertThat(response.getOriginal().toString(), equalTo(request.getUrl()));
        MatcherAssert.assertThat(response.getShortened().toString(), equalTo(ShortenerService.SHORTENER_HOST + "a"));

    }

    @Test
    @Ignore
    public void shouldGetErrorMessageWhenSubmittingAnInvalidURL() throws Exception {
        String invalidURL = "htp://test.com";
        HttpURLConnection conn = post(new ShortenerRequest(invalidURL));
        MatcherAssert.assertThat(conn.getResponseCode(), equalTo(500));
    }

    public HttpURLConnection post(ShortenerRequest request) throws IOException, URISyntaxException {
        URL apiURL = new URL("http://localhost:" + port + "/api/shorten");

        HttpURLConnection conn = (HttpURLConnection) apiURL.openConnection();

        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        conn.setRequestProperty("charset", Charset.forName("UTF-8").toString());

//        conn.connect();

        // posting params
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(conn.getOutputStream());
        objectOutputStream.writeObject(request);
        objectOutputStream.close();

        return conn;
    }

}
