package benjamin.groehbiel.ch;

import benjamin.groehbiel.ch.shortener.ShortenerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
class APIController {

    @Autowired
    ShortenerService shortenerService;

    @RequestMapping(
            value = "/all",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ShortenerResponse> latest() {
        Map<URI, Long> allShortenedURLs = shortenerService.getShortenedURIs();
        List<ShortenerResponse> response = allShortenedURLs.entrySet().stream()
                .map((Map.Entry<URI, Long> entry) -> {
                    return new ShortenerResponse(entry.getKey(), entry.getValue());
                })
                .collect(Collectors.toList());

        return response;
    }

    // TODO: serialize RequestBody Params as Java object
    @RequestMapping(
            value = "/shorten",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public Object shortenURL(@RequestBody String url) throws URISyntaxException {
        URI uri = new URI(url);
        URI shortened = shortenerService.shorten(uri);
        return new ShortenerResponse(new URI(url), shortened);
    }
}