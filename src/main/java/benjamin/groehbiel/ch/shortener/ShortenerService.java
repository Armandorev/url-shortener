package benjamin.groehbiel.ch.shortener;

import benjamin.groehbiel.ch.shortener.admin.AdminShortenerRequest;
import benjamin.groehbiel.ch.shortener.db.DictionaryHash;
import benjamin.groehbiel.ch.shortener.db.DictionaryManager;
import benjamin.groehbiel.ch.shortener.redis.RedisManager;
import benjamin.groehbiel.ch.shortener.wordnet.WordNetHelper;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Service
public class ShortenerService {

    @Autowired
    private DictionaryManager dictionaryManager;

    @Autowired
    private RedisManager redisManager;

    public ShortenerHandle shorten(URI originalURI) throws URISyntaxException, IOException {
        String wordHash = redisManager.getHashFor(originalURI.toString());

        if (wordHash != null) {
            return redisManager.getHandleFor(wordHash);
        } else {
            return createShortenerHandleFor(originalURI, dictionaryManager.nextHash());
        }
    }

    private ShortenerHandle createShortenerHandleFor(URI url, DictionaryHash token) throws URISyntaxException, JsonProcessingException {
        ShortenerHandle shortenerHandle = new ShortenerHandle(url, token.getHash(), token.getDescription());
        redisManager.storeHash(shortenerHandle);
        return shortenerHandle;
    }

    public ShortenerHandle expand(String hash) throws URISyntaxException, IOException {
        return redisManager.getHandleFor(hash);
    }

    public Map<URI, ShortenerHandle> getAllUrls() throws IOException {
        Set<String> uriKeys = redisManager.getHashes();

        HashMap<URI, ShortenerHandle> shortenedUris = new HashMap<>();
        for (String hash : uriKeys) {
            ShortenerHandle shortenerHandle = redisManager.getHandleFor(hash);
            shortenedUris.put(shortenerHandle.getOriginalURI(), shortenerHandle);
        }

        return shortenedUris;
    }

    public Long getShortenedCount() {
        return redisManager.getHashCount();
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

    public void clearUnused() {
        dictionaryManager.clearUnused();
    }

    public void populate(int count) throws IOException {
        dictionaryManager.fill(WordNetHelper.loadDirectory("WordNet"), count);
    }

    public void populate() throws IOException {
        populate(3000);
    }

    //TODO to be moved and improved, hack.
    public void insert(AdminShortenerRequest adminShortenerRequest) throws URISyntaxException, JsonProcessingException {
//        DictionaryHash dictionaryToken = dictionaryManager.getDictionaryToken(adminShortenerRequest.getHash());
        createShortenerHandleFor(new URI(adminShortenerRequest.getUrl()), new DictionaryHash(adminShortenerRequest.getHash(), "en", "something...", false));
    }

}
