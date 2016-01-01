package benjamin.groehbiel.ch.api;

import benjamin.groehbiel.ch.shortener.ShortenerHandle;
import benjamin.groehbiel.ch.shortener.ShortenerService;
import benjamin.groehbiel.ch.shortener.admin.AdminShortenerRequest;
import benjamin.groehbiel.ch.shortener.db.DictionaryHash;
import benjamin.groehbiel.ch.shortener.db.DictionaryManager;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping("/api/admin")
public class AdminApi {

    @Autowired
    ShortenerService shortenerService;

    @Autowired
    DictionaryManager dictionaryManager;

    @RequestMapping(value = "/shorten", method = POST, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public Map<String, String> shorten(@RequestBody AdminShortenerRequest adminShortenerRequest) throws URISyntaxException, JsonProcessingException {
        shortenerService.insert(adminShortenerRequest);
        return null;
    }

    @RequestMapping(value = "/shortened_urls", method = GET, produces = APPLICATION_JSON_VALUE)
    public List<ShortenerResponse> list() throws IOException {
        Map<URI, ShortenerHandle> allShortenedURLs = shortenerService.getAllUrls();

        return allShortenedURLs.entrySet()
                .stream()
                .map((Map.Entry<URI, ShortenerHandle> entry) -> {
                    try {
                        return ShortenerResponse.summarise(entry.getValue());
                    } catch (URISyntaxException e) {
                        return null;
                    }
                })
                .collect(Collectors.toList());
    }

    @RequestMapping(value = "/words", method = GET, produces = APPLICATION_JSON_VALUE)
    public Iterable<DictionaryHash> words(Pageable pageable) {
        return dictionaryManager.getWords(pageable);
    }

    @RequestMapping(value = "/reset", method = POST, produces = APPLICATION_JSON_VALUE)
    public Map<String, String> resetDictionary() {
        shortenerService.reset();
        return null;
    }

    @RequestMapping(value = "/words/remove_unused", method = POST, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity removeUnusedWords() {
        shortenerService.clearUnused();
        return new ResponseEntity<String>(HttpStatus.OK);
    }

    @RequestMapping(value = "/words/import", method = POST, produces = APPLICATION_JSON_VALUE)
    public Integer importWords(@RequestBody AdminImportRequest importRequest) throws IOException {
        return shortenerService.importFreshWordsByWordLength(importRequest.getNumberOfWords(), importRequest.getWordLength());
    }

}
