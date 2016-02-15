package it.w0rd.api;

import it.w0rd.persistence.ShortenerHandle;
import it.w0rd.persistence.ShortenerService;
import it.w0rd.persistence.db.DictionaryHash;
import it.w0rd.persistence.db.DictionaryManager;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    // TODO: This does not scale, paging of shortened urls will be necessary to make scalable.
    // Redis does not support paging out of the box and needs to be implemented by hand.
    @RequestMapping(value = "/shortened_urls", method = GET, produces = APPLICATION_JSON_VALUE)
    public List<ShortenerResponse> listShortenedUrls() throws IOException {
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
    public Iterable<DictionaryHash> listWordsByPage(Pageable pageable) {
        return dictionaryManager.getWords(pageable);
    }

    @RequestMapping(value = "/reset", method = POST, produces = APPLICATION_JSON_VALUE)
    public Map<String, String> clearEverything() {
        shortenerService.resetWordsAndHashes();
        return null;
    }

    @RequestMapping(value = "/words/remove_unused", method = POST, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity clearUnusedWords() {
        shortenerService.clearUnusedWords();
        return new ResponseEntity<String>(HttpStatus.OK);
    }

    @RequestMapping(value = "/words/import", method = POST, produces = APPLICATION_JSON_VALUE)
    public Integer importWords(@RequestBody AdminImportRequest importRequest) throws IOException {
        return shortenerService.importWordsWithLength(importRequest.getNumberOfWords(), importRequest.getWordLength());
    }

    @RequestMapping(value = "/words/remove", method = POST, produces = APPLICATION_JSON_VALUE)
    public ShortenerHandle deleteHash(@RequestBody AdminDeleteRequest deleteRequest) throws IOException {
        return shortenerService.remove(deleteRequest.getHash());
    }

}
