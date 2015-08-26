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
        return wordRepository.getShortenerHandleFor(hash);
    }

    public Map<URI, ShortenerHandle> getAllUrls() throws IOException {
        return wordRepository.getAllShortenerHandles();
    }

    public Long getShortenedCount() {
        return wordRepository.size();
    }

    public Long getRemainingCount() {
        return Long.valueOf(wordRepository.getRemainingHashCount());
    }
}
