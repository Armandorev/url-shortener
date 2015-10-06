package benjamin.groehbiel.ch.shortener;

import benjamin.groehbiel.ch.JsonHelper;
import benjamin.groehbiel.ch.shortener.admin.AdminShortenerRequest;
import benjamin.groehbiel.ch.shortener.db.DictionaryHash;
import benjamin.groehbiel.ch.shortener.db.DictionaryManager;
import benjamin.groehbiel.ch.shortener.redis.RedisManager;
import benjamin.groehbiel.ch.shortener.wordnet.WordDefinition;
import benjamin.groehbiel.ch.shortener.wordnet.WordNetHelper;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class ShortenerService {

    @Autowired
    private DictionaryManager dictionaryManager;

    @Autowired
    private RedisManager redisManager;

    public ShortenerHandle shorten(URI originalURI) throws URISyntaxException, IOException {
        String wordHash = redisManager.getValue(originalURI.toString());

        if (wordHash != null) {
            String json = redisManager.getValue(wordHash);
            return JsonHelper.unserialize(json);
        } else {
            DictionaryHash nextToken = dictionaryManager.takeNextAvailableWord();
            return createShortenerHandleFor(originalURI, nextToken);
        }
    }

    private ShortenerHandle createShortenerHandleFor(URI url, DictionaryHash token) throws URISyntaxException, JsonProcessingException {
        ShortenerHandle shortenerHandle = new ShortenerHandle(url, token.getHash(), token.getDescription());

        redisManager.setValue(shortenerHandle.getHash(), JsonHelper.serialize(shortenerHandle));
        redisManager.setValue(url.toString(), shortenerHandle.getHash());
        redisManager.incrementByOne("$count");

        return shortenerHandle;
    }

    public ShortenerHandle expand(String hash) throws URISyntaxException, IOException {
        String json = redisManager.getValue(hash);
        return JsonHelper.unserialize(json);
    }

    public Map<URI, ShortenerHandle> getAllUrls() throws IOException {
        Set<String> uriKeys = redisManager.getValuesFor("*:\\/\\/*");

        HashMap<URI, ShortenerHandle> shortenedUris = new HashMap<>();
        for (String uriKey : uriKeys) {
            String hash = redisManager.getValue(uriKey);
            String json = redisManager.getValue(hash);
            ShortenerHandle shortenerHandle = JsonHelper.unserialize(json);
            shortenedUris.put(shortenerHandle.getOriginalURI(), shortenerHandle);
        }

        return shortenedUris;
    }

    public Long getShortenedCount() {
        String shortenedSoFar = redisManager.getValue("$count");
        if (shortenedSoFar == null) {
            return 0L;
        } else {
            return Long.parseLong(shortenedSoFar);
        }
    }

    public Long getRemainingCount() {
        return dictionaryManager.getWordsAvailableSize();
    }

    public void clear() {
        redisManager.clear();
    }

    public void reset() {
        redisManager.clear();
        dictionaryManager.clear();
    }

    // TODO tests
    public void populate() throws IOException {
        List<WordDefinition> words = WordNetHelper.load("src/main/resources/WordNet/");
        dictionaryManager.fill(WordNetHelper.turnIntoDictionaryHashes(words));
    }

    //TODO to be moved and improved, hack.
    public void insert(AdminShortenerRequest adminShortenerRequest) throws URISyntaxException, JsonProcessingException {
//        DictionaryHash dictionaryToken = dictionaryManager.getDictionaryToken(adminShortenerRequest.getHash());
        createShortenerHandleFor(new URI(adminShortenerRequest.getUrl()), new DictionaryHash(adminShortenerRequest.getHash(), "en", "something...", false));
    }

}
