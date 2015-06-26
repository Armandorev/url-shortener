package benjamin.groehbiel.ch.shortener;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

public abstract class ShortenerRepository {

    protected abstract ShortenerHandle add(URI originalURI) throws URISyntaxException;

    protected abstract URI get(String hash);
    protected abstract Map<URI,ShortenerHandle> get();

    protected abstract Integer getCount();

    protected abstract void clear();

}
