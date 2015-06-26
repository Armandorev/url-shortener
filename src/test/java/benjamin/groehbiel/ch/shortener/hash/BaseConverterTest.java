package benjamin.groehbiel.ch.shortener.hash;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

import java.util.List;

public class BaseConverterTest {

    @Test
    public void convertBase10ToBase2NumberNotPowerOf2() throws Exception {
        List<Integer> convertedNumber = BaseConverter.convert(9L, 10, 2);
        MatcherAssert.assertThat(convertedNumber, Matchers.contains(1, 0, 0, 1));
    }

    @Test
    public void convertBase10ToBase2NumberIsPowerOf2() throws Exception {
        List<Integer> convertedNumber = BaseConverter.convert(16L, 10, 2);
        MatcherAssert.assertThat(convertedNumber, Matchers.contains(1, 0, 0, 0, 0));
    }

    @Test
    public void convertBase10ToBase2NumberIsZero() throws Exception {
        List<Integer> convertedNumber = BaseConverter.convert(0L, 10, 2);
        MatcherAssert.assertThat(convertedNumber, Matchers.hasSize(1));
    }

    @Test
    public void convertBase1031ToBase2() throws Exception {
        List<Integer> convertedNumber = BaseConverter.convert(31L, 10, 2);
        MatcherAssert.assertThat(convertedNumber, Matchers.contains(1, 1, 1, 1, 1));
    }

    @Test
    public void convertBase10ToBase8() {
        List<Integer> convertedNumber = BaseConverter.convert(31L, 10, 8);
        MatcherAssert.assertThat(convertedNumber, Matchers.contains(3, 7));
    }

    @Test
    public void convertBase10ToBase10() {
        List<Integer> convertedNumber = BaseConverter.convert(0L, 10, 10);
        MatcherAssert.assertThat(convertedNumber, Matchers.contains(0));
    }
}
