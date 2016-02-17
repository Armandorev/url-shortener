package it.w0rd.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import it.w0rd.api.requests.AdminShortenerRequest;
import it.w0rd.persistence.WordNetHelper;
import it.w0rd.persistence.db.DictionaryHash;
import it.w0rd.persistence.db.DictionaryManager;
import it.w0rd.persistence.redis.RedisManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.function.Predicate;

@Service
public class ShortenerService {

    @Autowired
    private DictionaryManager dictionaryManager;

    @Autowired
    private RedisManager redisManager;

    public ShortenedUrl shorten(URI originalURI) throws URISyntaxException, IOException {
        String wordHash = redisManager.getHashFor(originalURI.toString());

        if (wordHash != null) {
            return redisManager.getHandleFor(wordHash);
        } else {
            return createShortenedUrl(originalURI, dictionaryManager.nextHash());
        }
    }

    public ShortenedUrl expand(String hash) throws URISyntaxException, IOException {
        return redisManager.getHandleFor(hash);
    }

    public Map<URI, ShortenedUrl> getAllUrls() throws IOException {
        Set<String> uriKeys = redisManager.getHashes();

        HashMap<URI, ShortenedUrl> shortenedUris = new HashMap<>();
        for (String hash : uriKeys) {
            ShortenedUrl shortenedUrl = redisManager.getHandleFor(hash);
            shortenedUris.put(shortenedUrl.getOriginalURI(), shortenedUrl);
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

    public void resetWordsAndHashes() {
        redisManager.clear();
        dictionaryManager.clear();
    }

    public void clearUnusedWords() {
        dictionaryManager.clearUnusedWords();
    }

    public void populateDictionary(int count) throws IOException {
        dictionaryManager.shuffleAndFill(WordNetHelper.loadDirectoryForTests("WordNet"), count);
    }

    // TODO to be moved and improved, hack.
    public void insert(AdminShortenerRequest adminShortenerRequest) throws URISyntaxException, JsonProcessingException {
        createShortenedUrl(new URI(adminShortenerRequest.getUrl()), new DictionaryHash(adminShortenerRequest.getHash(), "en", "something...", false));
    }

    public Integer importWordsWithLength(Integer numberOfWords, Integer wordLength) throws IOException {
        List<DictionaryHash> allHashesMatchingLengthCriteria = WordNetHelper.loadAllWordsMatching("WordNet", new Predicate<DictionaryHash>() {
            @Override
            public boolean test(DictionaryHash dictionaryHash) {
                if (dictionaryHash.getHash().length() <= wordLength) return true;
                return false;
            }
        });

        Collections.shuffle(allHashesMatchingLengthCriteria);
        return dictionaryManager.fill(numberOfWords, allHashesMatchingLengthCriteria);
    }

    public ShortenedUrl remove(String hashToDelete) throws IOException {
        ShortenedUrl shortenedUrl = redisManager.getHandleFor(hashToDelete);
        redisManager.removeHash(hashToDelete);
        dictionaryManager.resetHash(hashToDelete);
        return shortenedUrl;
    }

    private ShortenedUrl createShortenedUrl(URI url, DictionaryHash token) throws URISyntaxException, JsonProcessingException {
        ShortenedUrl shortenedUrl = new ShortenedUrl(url, token.getHash(), token.getDescription());
        redisManager.storeHash(shortenedUrl);
        return shortenedUrl;
    }

}
