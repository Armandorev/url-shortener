package benjamin.groehbiel.ch.api;

import benjamin.groehbiel.ch.shortener.ShortenerHandle;
import benjamin.groehbiel.ch.shortener.ShortenerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping("/api")
class ApiController {

    @Autowired
    ShortenerService shortenerService;

    @RequestMapping(value = "/all", method = GET, produces = APPLICATION_JSON_VALUE)
    public List<ShortenerResponse> latest() {
        Map<URI, ShortenerHandle> allShortenedURLs = shortenerService.getAllUrls();

        return allShortenedURLs.entrySet()
                .stream()
                .map((Entry<URI, ShortenerHandle> entry) -> ShortenerResponse.summarise(entry.getValue()))
                .collect(Collectors.toList());
    }

    @RequestMapping(value = "/stats", method = GET, produces = APPLICATION_JSON_VALUE)
    public ShortenerStats getStats() {
        int count = shortenerService.getCount();
        return new ShortenerStats(count);
    }

    @RequestMapping(value = "/shorten", method = POST, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ShortenerResponse shortenURL(@RequestBody ShortenerRequest shortenerRequest) throws MalformedURLException, URISyntaxException {
        URI uri = validateURL(shortenerRequest);
        ShortenerHandle shortenerHandle = shortenerService.shorten(uri);
        ShortenerResponse shortenerResponse = new ShortenerResponse(shortenerHandle.getOriginalURI(), shortenerHandle.getShortenedURI());
        return shortenerResponse;
    }

    private URI validateURL(ShortenerRequest url) throws URISyntaxException, MalformedURLException {
        URL validateURL = new URI(url.getUrl()).toURL();
        return new URI(validateURL.toString());
    }

}