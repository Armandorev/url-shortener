package benjamin.groehbiel.ch.shortener;

import benjamin.groehbiel.ch.shortener.alphabets.hash.IntegerBasedHashingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

@Service
public class ShortenerService {

    public static final String SHORTENER_HOST = "http://www.shortener.com/";

    // TODO: this must be customisable
    @Autowired
    IntegerBasedHashingRepository integerBasedHashingRepository;

    public ShortenerHandle shorten(URI originalURI) throws URISyntaxException {
        return integerBasedHashingRepository.add(originalURI);
    }

    public URI expand(String hash) throws URISyntaxException {
        return integerBasedHashingRepository.get(hash);
    }

    public Map<URI, ShortenerHandle> getAllUrls() {
        return integerBasedHashingRepository.get();
    }

    public Integer getCount() {
        return integerBasedHashingRepository.getCount();
    }

}
