package it.w0rd.api;

import it.w0rd.api.requests.ShortenerRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping("/api")
public class UserApi {

    @Autowired
    ShortenerService shortenerService;

    @RequestMapping(value = "/stats", method = GET, produces = APPLICATION_JSON_VALUE)
    public ShortenerStats getStats() {
        Long shortenedCount = shortenerService.getShortenedCount();
        Long remainingCount = shortenerService.getRemainingCount();
        return new ShortenerStats(shortenedCount, remainingCount);
    }

    @RequestMapping(value = "/shorten", method = POST, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ShortenerResponse shortenURL(@RequestBody ShortenerRequest shortenerRequest) throws IOException, URISyntaxException {
        URI uri = validatedURL(shortenerRequest);
        ShortenerHandle shortenerHandle = shortenerService.shorten(uri);
        return ShortenerResponse.summarise(shortenerHandle);
    }

    private URI validatedURL(ShortenerRequest url) throws URISyntaxException, MalformedURLException {
        UriValidator validator = new UriValidator();
        String uri = url.getUrl();
        if (!validator.validate(uri)) {
            throw new MalformedURLException("Invalid URI: " + uri); // TODO: better exception?
        }
        return new URI(uri);
    }

}