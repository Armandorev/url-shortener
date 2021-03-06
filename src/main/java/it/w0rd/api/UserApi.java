package it.w0rd.api;

import it.w0rd.api.requests.user.CreateRequest;
import it.w0rd.api.responses.ShortenedUrlResponse;
import it.w0rd.api.responses.StatsResponse;
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
    public StatsResponse getStats() {
        Long shortenedCount = shortenerService.getShortenedCount();
        Long remainingCount = shortenerService.getRemainingCount();
        return new StatsResponse(shortenedCount, remainingCount);
    }

    @RequestMapping(value = "/shorten", method = POST, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ShortenedUrlResponse shortenURL(@RequestBody CreateRequest createRequest) throws IOException, URISyntaxException {
        URI uri = validatedURL(createRequest);
        ShortenedUrl shortenedUrl = shortenerService.shorten(uri);
        return ShortenedUrlResponse.summarise(shortenedUrl);
    }

    private URI validatedURL(CreateRequest url) throws URISyntaxException, MalformedURLException {
        UriValidator validator = new UriValidator();
        String uri = url.getUrl();
        if (!validator.validate(uri)) {
            throw new MalformedURLException("Invalid URI: " + uri); // TODO: better exception?
        }
        return new URI(uri);
    }

}