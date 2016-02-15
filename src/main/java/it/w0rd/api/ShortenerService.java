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

    public ShortenerHandle shorten(URI originalURI) throws URISyntaxException, IOException {
        String wordHash = redisManager.getHashFor(originalURI.toString());

        if (wordHash != null) {
            return redisManager.getHandleFor(wordHash);
        } else {
            return createShortenerHandleFor(originalURI, dictionaryManager.nextHash());
        }
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

    public void resetWordsAndHashes() {
        redisManager.clear();
        dictionaryManager.clear();
    }

    public void clearUnusedWords() {
        dictionaryManager.clearUnusedWords();
    }

    public void populateDictionary(int count) throws IOException {
        dictionaryManager.shuffleAndFill(WordNetHelper.loadDirectory("WordNet"), count);
    }

    //TODO to be moved and improved, hack.
    public void insert(AdminShortenerRequest adminShortenerRequest) throws URISyntaxException, JsonProcessingException {
//        DictionaryHash dictionaryToken = dictionaryManager.getDictionaryToken(adminShortenerRequest.getHash());
        createShortenerHandleFor(new URI(adminShortenerRequest.getUrl()), new DictionaryHash(adminShortenerRequest.getHash(), "en", "something...", false));
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

    private ShortenerHandle createShortenerHandleFor(URI url, DictionaryHash token) throws URISyntaxException, JsonProcessingException {
        ShortenerHandle shortenerHandle = new ShortenerHandle(url, token.getHash(), token.getDescription());
        redisManager.storeHash(shortenerHandle);
        return shortenerHandle;
    }

    public ShortenerHandle remove(String hashToDelete) throws IOException {
        ShortenerHandle hashHandle = redisManager.getHandleFor(hashToDelete);
        redisManager.removeHash(hashToDelete);
        dictionaryManager.resetHash(hashToDelete);
        return hashHandle;
    }
}