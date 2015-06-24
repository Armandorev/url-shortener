package benjamin.groehbiel.ch;

import benjamin.groehbiel.ch.shortener.ShortenerHandle;
import benjamin.groehbiel.ch.shortener.ShortenerRequest;
import benjamin.groehbiel.ch.shortener.ShortenerService;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
class APIController {

    @Autowired
    ShortenerService shortenerService;

    @RequestMapping(value = "/all", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ShortenerResponse> latest() {
        Map<URI, ShortenerHandle> allShortenedURLs = shortenerService.getAllUrls();

        return allShortenedURLs.entrySet()
                .stream()
                .map((Entry<URI, ShortenerHandle> entry) -> ShortenerResponse.summarise(entry.getValue()))
                .collect(Collectors.toList());
    }

    // TODO: serialize RequestBody Params as Java object
    @RequestMapping(value = "/shorten", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Object shortenURL(@RequestBody ShortenerRequest request) throws URISyntaxException, MalformedURLException {
        System.out.println(" ---------------------------------- caught the request!!!!!!!!!!!");
        URI uri = validateURL(request);
        ShortenerHandle shortenerHandle = shortenerService.shorten(uri);
        return new ShortenerResponse(shortenerHandle.getOriginalURI(), shortenerHandle.getShortenedURI());
    }

    private URI validateURL(ShortenerRequest url) throws URISyntaxException, MalformedURLException {
        URL validateURL = new URI(url.getUrl()).toURL();
        return new URI(validateURL.toString());
    }
}