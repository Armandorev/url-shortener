package benjamin.groehbiel.ch.api.admin;

import benjamin.groehbiel.ch.Application;
import benjamin.groehbiel.ch.PersistenceInitializer;
import benjamin.groehbiel.ch.TestBase;
import benjamin.groehbiel.ch.api.ShortenerResponse;
import benjamin.groehbiel.ch.shortener.ShortenerService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.MatcherAssert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AdminApiTest extends TestBase {

    private MockMvc mockMvc;
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Autowired
    private ShortenerService shortenerService;

    @Autowired
    private WebApplicationContext wac;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void shouldReturnAllShortenedUrls() throws Exception {
        addUrlToRepository("http://www.pivotal.io");
        addUrlToRepository("http://www.pivotallabs.com");

        MvcResult mvcResult = getMvcResultForSuccessfulGet("/api/admin/shortened_urls");
        List<ShortenerResponse> responses = mapResponseStringToShortenerResponses(mvcResult);

        MatcherAssert.assertThat(responses, hasSize(2));

        MatcherAssert.assertThat(responses, containsInAnyOrder(
                new ShortenerResponse(new URI("http://www.pivotal.io"), new URI("xxx"), "able", ""),
                new ShortenerResponse(new URI("http://www.pivotallabs.com"), new URI("xxx"), "unable", "")
        ));
    }

    private List<ShortenerResponse> mapResponseStringToShortenerResponses(MvcResult mvcResult) throws IOException {
        String bodyString = mvcResult.getResponse().getContentAsString();
        return OBJECT_MAPPER.readValue(bodyString, new TypeReference<List<ShortenerResponse>>() {});
    }

    private MvcResult getMvcResultForSuccessfulGet(String path) throws Exception {
        return mockMvc.perform(get(path).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn();
    }

    private void addUrlToRepository(String url) throws URISyntaxException, IOException {
        URI urlPivotal = new URI(url);
        shortenerService.shorten(urlPivotal);
    }

}