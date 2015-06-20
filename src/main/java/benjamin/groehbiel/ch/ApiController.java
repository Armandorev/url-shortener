package benjamin.groehbiel.ch;

import benjamin.groehbiel.ch.shortener.ShortenerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class APIController {

    @Autowired
    ShortenerService shortenerService;

    @RequestMapping(
            value = "/all",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ShortenedURIResponse> latest() {
        Map<URI, Long> allShortenedURLs = shortenerService.getShortenedURIs();
        List<ShortenedURIResponse> response = allShortenedURLs.entrySet().stream()
                .map((Map.Entry<URI, Long> entry) -> {
                    return new ShortenedURIResponse(entry.getKey(), entry.getValue());
                })
                .collect(Collectors.toList());

        return response;
    }

}