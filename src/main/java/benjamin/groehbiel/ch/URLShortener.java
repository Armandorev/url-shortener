package benjamin.groehbiel.ch;

import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class URLShortener {

    private Map<Integer, Character> alphabet;

    public URLShortener() {
        alphabet = new HashMap<>();
        alphabet.put(0, 'a');
        alphabet.put(1, 'b');
        alphabet.put(2, 'c');
        alphabet.put(3, 'd');
        alphabet.put(4, 'e');
        alphabet.put(5, 'f');
        alphabet.put(6, 'g');
        alphabet.put(7, 'h');
        alphabet.put(8, 'i');
        alphabet.put(9, 'j');
    }

    protected String decode(Long id) {

        List<Integer> digits = BaseConverter.convert(id, 10, 10);

        String hash = digits.stream()
                .map(d -> alphabet.get(d).toString())
                .collect(Collectors.joining());

        return hash;
    }

    protected URI restore(String hash) {
        return null;
    }

}
