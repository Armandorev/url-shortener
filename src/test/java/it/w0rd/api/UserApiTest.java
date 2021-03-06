package it.w0rd.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.w0rd.DataTest;
import it.w0rd.api.requests.user.CreateRequest;
import it.w0rd.api.responses.ShortenedUrlResponse;
import it.w0rd.api.responses.StatsResponse;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.MalformedURLException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserApiTest extends DataTest {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Autowired
    private WebApplicationContext wac;
    
    private MockMvc mockMvc;

    @Before
    public void initMockMvc() throws IOException {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    public void shouldReturnGeneralStats() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/api/stats").contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn();

        StatsResponse statsResponse = OBJECT_MAPPER.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<StatsResponse>() {
        });

        assertThat(statsResponse.getRemainingCount(), equalTo(21L));
        assertThat(statsResponse.getShortenedCount(), equalTo(0L));
    }

    @Test
    public void shouldAddShortenedURLWhenPostedTo() throws Exception {
        byte[] requestJson = OBJECT_MAPPER.writeValueAsBytes(new CreateRequest("https://run.pivotal.io/"));

        MvcResult postResponse = mockMvc.perform(post("/api/shorten")
                .content(requestJson).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType("application/json"))
                .andReturn();

        String responseAsString = postResponse.getResponse().getContentAsString();
        ShortenedUrlResponse response = OBJECT_MAPPER.readValue(responseAsString, new TypeReference<ShortenedUrlResponse>() {
        });

        assertThat(response.getOriginal().toString(), equalTo(new CreateRequest("https://run.pivotal.io/").getUrl()));
    }

    @Test(expected = MalformedURLException.class)
    public void shouldGetErrorMessageWhenSubmittingAnInvalidURL() throws Exception {
        CreateRequest request = new CreateRequest("htp://invalid.url");

        byte[] requestJson = OBJECT_MAPPER.writeValueAsBytes(request);

        mockMvc.perform(post("/api/shorten").content(requestJson).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().is(400))
                .andExpect(content().contentType("application/json"))
                .andReturn();
    }

    public byte[] objectToBytes(Object o) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutput out;
        out = new ObjectOutputStream(bos);
        out.writeObject(o);
        return bos.toByteArray();
    }

}
