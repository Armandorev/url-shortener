package benjamin.groehbiel.ch.shortener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

@Service
public class ShortenerService {

    @Autowired
    WordRepository wordRepository;

    public ShortenerHandle shorten(URI originalURI) throws URISyntaxException {
        return wordRepository.add(originalURI);
    }

    public URI expand(String hash) throws URISyntaxException {
        return wordRepository.get(hash);
    }

    public Map<URI, ShortenerHandle> getAllUrls() {
        return wordRepository.get();
    }

    public Integer getCount() {
        return wordRepository.getCount();
    }

}
