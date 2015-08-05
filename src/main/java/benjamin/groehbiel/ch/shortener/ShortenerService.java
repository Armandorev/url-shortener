package benjamin.groehbiel.ch.shortener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

@Service
public class ShortenerService {

    @Autowired
    WordRepository wordRepository;

    public ShortenerHandle shorten(URI originalURI) throws URISyntaxException, IOException {
        return wordRepository.add(originalURI);
    }

    public ShortenerHandle expand(String hash) throws URISyntaxException, IOException {
        return wordRepository.get(hash);
    }

    public Map<URI, ShortenerHandle> getAllUrls() {
        return wordRepository.get();
    }

    public Long getCount() {
        return wordRepository.getCount();
    }

}
