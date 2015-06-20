package benjamin.groehbiel.ch;

import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

@Service
public class URLShortener {

    Alphabet alphabet;

    public URLShortener() {
        alphabet = new Base10Alphabet();
    }

    protected String decode(Long id) {

        List<Integer> digits = BaseConverter.convert(id, 10, 10);

        String hash = digits.stream()
                .map(d -> alphabet.getLetterFor(d).toString())
                .collect(Collectors.joining());

        return hash;
    }

    protected Integer encode(String hash) {
        List<Integer> mappedToInt = hash.chars()
                .mapToObj(characterValue -> alphabet.getIndexFor((char) characterValue))
                .collect(toList());

        return stringify(mappedToInt);
    }

    // TODO: verify upper bounds, Long is used elsewhere.
    private Integer stringify(List<Integer> listOfInts) {
        String concatInts = listOfInts.stream()
                .map(item -> new String("" + item))
                .collect(joining());

        return Integer.parseInt(concatInts);
    }
}
