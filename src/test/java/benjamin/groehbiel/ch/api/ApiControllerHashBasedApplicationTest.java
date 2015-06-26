package benjamin.groehbiel.ch.api;

import benjamin.groehbiel.ch.shortener.ShortenerService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.MatcherAssert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
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

public class ApiControllerHashBasedApplicationTest extends SpringHashBasedApplicationTest {

    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Autowired
    ShortenerService shortenerService;

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
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

        List<ShortenerResponse> newResponse = OBJECT_MAPPER.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<List<ShortenerResponse>>() {});
        MatcherAssert.assertThat(newResponse, hasSize(2));

        MatcherAssert.assertThat(newResponse, containsInAnyOrder(
                new ShortenerResponse(urlPivotal, new URI(ShortenerService.SHORTENER_HOST + "a")),
                new ShortenerResponse(urlLabs, new URI(ShortenerService.SHORTENER_HOST + "b"))
        ));
    }

    @Test
    public void shouldAddShortenedURLWhenPostedTo() throws Exception {
        ShortenerRequest request = new ShortenerRequest("https://run.pivotal.io/");

        byte[] requestJson = OBJECT_MAPPER.writeValueAsBytes(request);

        MvcResult postResponse = mockMvc.perform(post("/api/shorten").content(requestJson).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType("application/json"))
                .andReturn();

        ShortenerResponse response = OBJECT_MAPPER.readValue(postResponse.getResponse().getContentAsString(), new TypeReference<ShortenerResponse>() {});
        MatcherAssert.assertThat(response.getOriginal().toString(), equalTo(request.getUrl()));
        MatcherAssert.assertThat(response.getShortened().toString(), equalTo(ShortenerService.SHORTENER_HOST + "a"));
    }

    @Ignore
    @Test
    public void shouldGetErrorMessageWhenSubmittingAnInvalidURL() throws Exception {
        ShortenerRequest request = new ShortenerRequest("htp://invalid.url");

        byte[] requestJson = OBJECT_MAPPER.writeValueAsBytes(request);

        MvcResult postResponse = mockMvc.perform(post("/api/shorten").content(requestJson).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().is(400))
                .andExpect(content().contentType("application/json"))
                .andReturn();

        ShortenerException exception = OBJECT_MAPPER.readValue(postResponse.getResponse().getContentAsString(), new TypeReference<ShortenerException>() {});
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
