package benjamin.groehbiel.ch.shortener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

@Service
public class ShortenerService {

    public static final String SHORTENER_HOST = "http://www.shortener.com/";

    @Autowired
    UrlRepository urlRepository;

    public ShortenerHandle shorten(URI originalURI) throws URISyntaxException {
        return urlRepository.add(originalURI);
    }

    public URI expand(String hash) throws URISyntaxException {
        return urlRepository.get(hash);
    }

    public Map<URI, ShortenerHandle> getAllUrls() {
        return urlRepository.getAll();
    }

    public Integer getCount() {
        return urlRepository.getCount();
    }

}
