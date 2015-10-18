package benjamin.groehbiel.ch.api;

import benjamin.groehbiel.ch.shortener.ShortenerService;
import benjamin.groehbiel.ch.shortener.admin.AdminShortenerRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping("/api/admin")
public class AdminApi {

    @Autowired
    ShortenerService shortenerService;

    @RequestMapping(value = "/reset", method = POST, produces = APPLICATION_JSON_VALUE)
    public Map<String, String> resetDictionary() {
        shortenerService.reset();
        return null;
    }

    @RequestMapping(value = "/import", method = POST, produces = APPLICATION_JSON_VALUE)
    public Map<String, String> importDictionary() throws IOException {
        shortenerService.populate();
        return null;
    }

    @RequestMapping(value = "/shorten", method = POST, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public Map<String, String> shorten(@RequestBody AdminShortenerRequest adminShortenerRequest) throws URISyntaxException, JsonProcessingException {
        shortenerService.insert(adminShortenerRequest);
        return null;
    }

}
