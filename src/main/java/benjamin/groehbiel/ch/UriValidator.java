package benjamin.groehbiel.ch;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

public class UriValidator {

    private List<String> validProtocols = Arrays.asList("http", "https");

    public boolean validate(String uri) {
        return absoluteUri(uri) && validProtocol(uri) && globallyAccessibleUri(uri);
    }

    private boolean absoluteUri(String uri) {
        try {
            new URI(uri);
        } catch (URISyntaxException ex) {
            return false;
        }
        return true;
    }

    /**
     * The following URIs are local and should not be shortened:
     * - Anything on localhost
     * - 192.168.*.*
     * - 0.0.0.0
     * - 127.0.0.1
     *
     * @param uri
     * @return whether the URI is globally accessible
     */
    private boolean globallyAccessibleUri(String uri) {
        if (uri.matches("\\w+://localhost.*")) {
            return false;
        } else if (uri.matches("\\w+://192\\.168(?:\\.\\d{1,3}){2}.*")) {
            return false;
        } else if (uri.matches("\\w+://(127\\.0\\.0\\.1|0\\.0\\.0\\.0)\\D?.*")) {
            return false;
        }
        return true;
    }

    private boolean validProtocol(String uri) {
        return validProtocols.stream().anyMatch(uri::startsWith);
    }

}
