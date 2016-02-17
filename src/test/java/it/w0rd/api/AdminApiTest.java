package it.w0rd.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.w0rd.DataTest;
import it.w0rd.api.requests.admin.DeleteRequest;
import it.w0rd.api.requests.admin.ImportRequest;
import it.w0rd.persistence.db.DictionaryHash;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AdminApiTest extends DataTest {

    private MockMvc mockMvc;
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Autowired
    private ShortenerService shortenerService;

    @Autowired
    private WebApplicationContext wac;

    @Before
    public void setup() throws IOException {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void shouldReturnAllShortenedUrls() throws Exception {
        shortenUrl("http://www.pivotal.io");
        shortenUrl("http://www.pivotallabs.com");

        List<ShortenerResponse> responses = performGetAndUnserialize("/api/admin/shortened_urls");

        assertThat(responses, hasSize(2));
    }

    @Test
    public void shouldReturnAllAvailableWords() throws Exception {
        List<DictionaryHash> dictionaryHashes = mapToDictionaryHashes(mockMvc.perform(get("/api/admin/words")).andReturn());

        assertThat(dictionaryHashes.size(), equalTo(20));
        assertThat(dictionaryHashes, Matchers.<DictionaryHash>everyItem(hasProperty("available", equalTo(true))));
    }

    @Test
    public void shouldReturnAllUnavailableWords() throws Exception {
        shortenUrl("http://www.pivotal.io");
        List<DictionaryHash> dictionaryHashes = mapToDictionaryHashes(mockMvc.perform(get("/api/admin/words")).andReturn());

        assertThat(dictionaryHashes, Matchers.<DictionaryHash>hasItem(hasProperty("available", equalTo(false))));
    }

    @Test
    public void shouldNotOverwriteTakenWordsWhenImportingNewWords() throws Exception {
        shortenUrl("http://www.pivotal.io");
        shortenerService.populateDictionary(10);
        shortenerService.populateDictionary(10);
        shortenerService.populateDictionary(10);
        shortenerService.populateDictionary(10);

        List<DictionaryHash> dictionaryHashes = mapToDictionaryHashes(mockMvc.perform(get("/api/admin/words?size=50")).andReturn());

        assertThat(dictionaryHashes, Matchers.<DictionaryHash>hasItem(hasProperty("available", equalTo(false))));
    }

    @Test
    public void shouldRemoveAllUnusedWords() throws Exception {
        shortenUrl("http://www.pivotal.io");
        mockMvc.perform(post("/api/admin/words/remove_unused")).andReturn();

        assertThat(shortenerService.getRemainingCount(), equalTo(0L));
        assertThat(shortenerService.getShortenedCount(), equalTo(1L));
    }

    @Test
    public void shouldImportFreshWordsGivenAmountAndCriteria() throws Exception {
        dictionaryManager.clear();

        ImportRequest importRequest = new ImportRequest();
        importRequest.setNumberOfWords(10);
        importRequest.setWordLength(6);
        byte[] postJson = OBJECT_MAPPER.writeValueAsBytes(importRequest);

        mockMvc.perform(
                post("/api/admin/words/import")
                        .content(postJson).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        assertThat(shortenerService.getRemainingCount(), equalTo(7L));
    }

    @Test
    public void shouldRemoveExistingHash() throws Exception {
        ShortenedUrl handle = shortenUrl("http://www.test.com");
        DeleteRequest deleteRequest = new DeleteRequest(handle.getHash());

        byte[] postJson = OBJECT_MAPPER.writeValueAsBytes(deleteRequest);

        mockMvc.perform(post("/api/admin/words/remove").content(postJson).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        assertThat(shortenerService.getShortenedCount(), equalTo(0L));
    }

    private List<ShortenerResponse> performGetAndUnserialize(String path) throws Exception {
        MvcResult mvcResult = mockMvc.perform(get(path).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn();

        return mapToShortenerResponses(mvcResult);
    }

    private List<DictionaryHash> mapToDictionaryHashes(MvcResult mvcResult) throws IOException {
        String bodyContent = mvcResult.getResponse().getContentAsString();
        DictionaryHashPage o = OBJECT_MAPPER.readValue(bodyContent, new TypeReference<DictionaryHashPage>() {
        });
        return o.getContent();
    }

    private List<ShortenerResponse> mapToShortenerResponses(MvcResult mvcResult) throws IOException {
        String bodyString = mvcResult.getResponse().getContentAsString();
        return OBJECT_MAPPER.readValue(bodyString, new TypeReference<List<ShortenerResponse>>() {
        });
    }

    private ShortenedUrl shortenUrl(String url) throws URISyntaxException, IOException {
        return shortenerService.shorten(new URI(url));
    }

}