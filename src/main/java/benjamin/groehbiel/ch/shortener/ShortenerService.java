package benjamin.groehbiel.ch.shortener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

@Service
public class ShortenerService {

    @Autowired
    ShortenerRepository shortenerRepository;

    public ShortenerHandle shorten(URI originalURI) throws URISyntaxException {
        return shortenerRepository.add(originalURI);
    }

    public URI expand(String hash) throws URISyntaxException {
        return shortenerRepository.get(hash);
    }

    public Map<URI, ShortenerHandle> getAllUrls() {
        return shortenerRepository.get();
    }

    public Integer getCount() {
        return shortenerRepository.getCount();
    }

}
