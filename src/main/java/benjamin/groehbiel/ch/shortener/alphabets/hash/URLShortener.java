package benjamin.groehbiel.ch.shortener.alphabets.hash;

import org.springframework.stereotype.Service;

import java.util.List;

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
                .collect(joining());

        return hash;
    }

    protected Long encode(String hash) {
        List<Integer> mappedToInts = hash.chars()
                .mapToObj(characterValue -> alphabet.getIndexFor((char) characterValue))
                .collect(toList());

        return longify(mappedToInts);
    }

    private Long longify(List<Integer> listOfInts) {
        String number = listOfInts.stream()
                .map(item -> new String("" + item))
                .collect(joining());

        return Long.parseLong(number);
    }
}
