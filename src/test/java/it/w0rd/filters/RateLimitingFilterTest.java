package it.w0rd.filters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.w0rd.DataTest;
import it.w0rd.api.requests.user.CreateRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class RateLimitingFilterTest extends DataTest {
    private MockMvc mockMvc;
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private RateLimitingFilter rateLimitFilter;

    private byte[] requestJson;

    public RateLimitingFilterTest() throws JsonProcessingException {
        requestJson = OBJECT_MAPPER.writeValueAsBytes(new CreateRequest("https://run.pivotal.io/"));
    }

    @Before
    public void setup() throws IOException {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).addFilter(rateLimitFilter).build();
    }

    @Test
    public void passesTheRequestThroughWhenTheClientHasNotExceededTheLimit() throws Exception {
        assertCreatedStatusCodeForXRequests();
    }

    @Test
    public void returnsARateLimitExceededStatusCodeWhenTheClientExceedsTheLimitForADay() throws Exception {
        assertCreatedStatusCodeForXRequests();

        executePostShortenRequest().andExpect(status().is(HttpStatus.TOO_MANY_REQUESTS.value()));
    }

    private void assertCreatedStatusCodeForXRequests() throws Exception {
        for (int requestNumber = 0; requestNumber < RateLimitingFilter.MAX_DAILY_REQUESTS_PER_CLIENT; requestNumber++) {
            executePostShortenRequest()
                    .andExpect(status().is(HttpServletResponse.SC_CREATED))
                    .andReturn();
        }
    }

    private ResultActions executePostShortenRequest() throws Exception {
        return mockMvc.perform(
                post("/api/shorten")
                        .content(requestJson)
                        .contentType(MediaType.APPLICATION_JSON_VALUE));
    }

}
