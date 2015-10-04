package benjamin.groehbiel.ch.api;

import benjamin.groehbiel.ch.ApplicationTest;
import benjamin.groehbiel.ch.PersistenceInitializer;
import benjamin.groehbiel.ch.DatabaseTest;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.URI;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ApplicationTest.class, initializers = PersistenceInitializer.class)
@WebAppConfiguration
@IntegrationTest("server.port:0")
public class ApiApplicationTest extends DatabaseTest {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Autowired
    private ShortenerService shortenerService;

    @Autowired
    private WebApplicationContext wac;

    @Value("${app.host}")
    private String host;

    @Value("${app.protocol}")
    private String protocol;

    private MockMvc mockMvc;

    @Before
    public void initMockMvc() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    public void shouldReturnGeneralStats() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/api/stats").contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn();

        ShortenerStats shortenerStats = OBJECT_MAPPER.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<ShortenerStats>() {});
        MatcherAssert.assertThat(shortenerStats.getRemainingCount(), equalTo(21L));
        MatcherAssert.assertThat(shortenerStats.getShortenedCount(), equalTo(0L));
    }

    @Test
    public void shouldExposeAListOfAllShortenedURLs() throws Exception {
        URI urlPivotal = new URI("http://www.pivotal.io");
        URI urlLabs = new URI("http://www.pivotallabs.com");
        shortenerService.shorten(urlPivotal);
        shortenerService.shorten(urlLabs);

        MvcResult mvcResult = mockMvc.perform(get("/api/all").contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn();

        List<ShortenerResponse> responses = OBJECT_MAPPER.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<List<ShortenerResponse>>() {
        });

        MatcherAssert.assertThat(responses, hasSize(2));

        MatcherAssert.assertThat(responses, containsInAnyOrder(
                new ShortenerResponse(urlPivotal, new URI("xxx"), "able", ""),
                new ShortenerResponse(urlLabs, new URI("xxx"), "unable", "")
        ));
    }

    @Test
    public void shouldAddShortenedURLWhenPostedTo() throws Exception {
        ShortenerRequest request = new ShortenerRequest("https://run.pivotal.io/");

        byte[] requestJson = OBJECT_MAPPER.writeValueAsBytes(request);

        MvcResult postResponse = mockMvc.perform(post("/api/shorten")
                .content(requestJson).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType("application/json"))
                .andReturn();

        String responseAsString = postResponse.getResponse().getContentAsString();
        ShortenerResponse response = OBJECT_MAPPER.readValue(responseAsString, new TypeReference<ShortenerResponse>() {});
        MatcherAssert.assertThat(response.getOriginal().toString(), equalTo(request.getUrl()));

        String expectedShortenedUrl = protocol + "://" + host + "/" + "able";
        String actualShortenedUrl = response.getShortened().toString();
        MatcherAssert.assertThat(actualShortenedUrl, equalTo(expectedShortenedUrl));
    }

    @Test
    @Ignore
    public void shouldGetErrorMessageWhenSubmittingAnInvalidURL() throws Exception {
        ShortenerRequest request = new ShortenerRequest("htp://invalid.url");

        byte[] requestJson = OBJECT_MAPPER.writeValueAsBytes(request);

        MvcResult postResponse = mockMvc.perform(post("/api/shorten").content(requestJson).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().is(400))
                .andExpect(content().contentType("application/json"))
                .andReturn();

        ShortenerException exception = OBJECT_MAPPER.readValue(postResponse.getResponse().getContentAsString(), new TypeReference<ShortenerException>() {
        });
        MatcherAssert.assertThat(exception.getMessage(), equalTo("Could not parse your URL: unknown protocol: htp"));
    }

    public byte[] ojectToBytes(Object o) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutput out;
        out = new ObjectOutputStream(bos);
        out.writeObject(o);
        return bos.toByteArray();
    }

}
