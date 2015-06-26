package benjamin.groehbiel.ch.shortener;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

public interface ShortenerRepository {

    ShortenerHandle add(URI originalURI) throws URISyntaxException;

    URI get(String hash);
    Map<URI,ShortenerHandle> get();

    Integer getCount();

    void clear();

}
