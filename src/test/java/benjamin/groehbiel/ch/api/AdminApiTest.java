package benjamin.groehbiel.ch.api;

import benjamin.groehbiel.ch.Application;
import benjamin.groehbiel.ch.PersistenceInitializer;
import benjamin.groehbiel.ch.shortener.ShortenerService;
import benjamin.groehbiel.ch.shortener.db.DictionaryHash;
import benjamin.groehbiel.ch.shortener.db.DictionaryManager;
import benjamin.groehbiel.ch.shortener.redis.RedisManager;
import benjamin.groehbiel.ch.shortener.wordnet.WordNetHelper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.After;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class, initializers = PersistenceInitializer.class)
@WebAppConfiguration
@IntegrationTest("server.port:0")
public class AdminApiTest {

    private MockMvc mockMvc;
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Autowired
    private ShortenerService shortenerService;

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private DictionaryManager dictionaryManager;

    @Autowired
    protected RedisManager redisManager;

    @Before
    public void setup() throws IOException {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
        dictionaryManager.clear();
    }

    @After
    public void flushTable() {
        dictionaryManager.clear();
        redisManager.clear();
    }

    @Test
    public void shouldReturnAllShortenedUrls() throws Exception {
        dictionaryManager.fill(WordNetHelper.loadDirectory("WordNet"));

        addUrlToRepository("http://www.pivotal.io");
        addUrlToRepository("http://www.pivotallabs.com");

        MvcResult mvcResult = getMvcResultForSuccessfulGet("/api/admin/shortened_urls");
        List<ShortenerResponse> responses = mapResponseStringToShortenerResponses(mvcResult);

        assertThat(responses, hasSize(2));
    }

    @Test
    public void shouldReturnAllAvailableWords() throws Exception {
        shortenerService.populate(10);

        MvcResult mvcResult = mockMvc.perform(get("/api/admin/words")).andReturn();
        List<DictionaryHash> dictionaryHashes = mapResponseStringToDictionaryHashes(mvcResult);

        assertThat(dictionaryHashes.size(), equalTo(10));
        assertThat(dictionaryHashes, Matchers.<DictionaryHash>everyItem(hasProperty("available", equalTo(true))));
    }

    @Test
    public void shouldReturnAllUnavailableWords() throws Exception {
        shortenerService.populate(10);
        addUrlToRepository("http://www.pivotal.io");

        MvcResult mvcResult = mockMvc.perform(get("/api/admin/words")).andReturn();
        List<DictionaryHash> dictionaryHashes = mapResponseStringToDictionaryHashes(mvcResult);

        assertThat(dictionaryHashes, Matchers.<DictionaryHash>hasItem(hasProperty("available", equalTo(false))));
    }

    @Test
    public void shouldNotOverwriteTakenWordsWhenImportingNewWords() throws Exception {
        shortenerService.populate(10);
        addUrlToRepository("http://www.pivotal.io");
        shortenerService.populate(10);
        shortenerService.populate(10);
        shortenerService.populate(10);
        shortenerService.populate(10);

        MvcResult mvcResult = mockMvc.perform(get("/api/admin/words?size=50")).andReturn();
        List<DictionaryHash> dictionaryHashes = mapResponseStringToDictionaryHashes(mvcResult);

        assertThat(dictionaryHashes, Matchers.<DictionaryHash>hasItem(hasProperty("available", equalTo(false))));
    }

    @Test
    public void shouldRemoveAllUnusedWords() throws Exception {
        shortenerService.populate(10);
        shortenerService.shorten(new URI("http://www.google.com"));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/admin/words/remove_unused")).andReturn();

        assertThat(shortenerService.getRemainingCount(), equalTo(0L));
        assertThat(shortenerService.getShortenedCount(), equalTo(1L));
    }

    private List<DictionaryHash> mapResponseStringToDictionaryHashes(MvcResult mvcResult) throws IOException {
        String bodyContent = mvcResult.getResponse().getContentAsString();
        DictionaryHashPage o = OBJECT_MAPPER.readValue(bodyContent, new TypeReference<DictionaryHashPage>() {
        });
        return o.getContent();
    }

    private List<ShortenerResponse> mapResponseStringToShortenerResponses(MvcResult mvcResult) throws IOException {
        String bodyString = mvcResult.getResponse().getContentAsString();
        return OBJECT_MAPPER.readValue(bodyString, new TypeReference<List<ShortenerResponse>>() {
        });
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