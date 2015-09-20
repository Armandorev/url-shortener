package benjamin.groehbiel.ch;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ApplicationTest.class, initializers = PersistenceInitializer.class)
@WebAppConfiguration
@IntegrationTest("server.port:0")
public class HttpsFilterTest {

    @Value("${local.server.port}")
    private int port;

    @Autowired
    private HttpsFilter httpsFilter;

    @After
    public void tearDown() {
        httpsFilter.setForceHttps(false);
    }

    @Test
    public void shouldEnforceHttps() throws IOException {
        httpsFilter.setForceHttps(true);
        HttpURLConnection connection = getHttpURLConnection("http://localhost:" + port);

        assertThat(connection.getResponseCode(), equalTo(HttpServletResponse.SC_MOVED_PERMANENTLY));
        assertThat(connection.getHeaderField("Location"), equalTo("https://localhost:" + port + "/"));
    }

    @Test
    public void shouldRespectFilterOff() throws IOException {
        httpsFilter.setForceHttps(false);
        HttpURLConnection connection = getHttpURLConnection("http://localhost:" + port);

        assertThat(connection.getResponseCode(), equalTo(HttpServletResponse.SC_OK));
    }

    private HttpURLConnection getHttpURLConnection(String url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setInstanceFollowRedirects(false);
        connection.connect();
        return connection;
    }
}
