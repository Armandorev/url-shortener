package benjamin.groehbiel.ch;

import com.fasterxml.jackson.databind.deser.DataFormatReaders;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;


public class URLShortenerTest {
    URLShortener urlShortener = new URLShortener();

    @Test
    public void shouldEncode0ToHash() throws Exception {
        String hash = urlShortener.decode(0L);
        MatcherAssert.assertThat(hash, Matchers.equalTo("a"));
    }

    @Test
    public void shouldEncode1ToHash() throws Exception {
        String hash = urlShortener.decode(1L);
        MatcherAssert.assertThat(hash, Matchers.equalTo("b"));
    }

    @Test
    public void shouldEncode11ToHash() throws Exception {
        String hash = urlShortener.decode(10L);
        MatcherAssert.assertThat(hash, Matchers.equalTo("ba"));
    }

    @Test
    public void shouldEncode101ToHash() throws Exception {
        String hash = urlShortener.decode(101L);
        MatcherAssert.assertThat(hash, Matchers.equalTo("bab"));
    }

    @Test
    public void shouldDecode() {
        String hash = "bba";
        Integer id = urlShortener.encode(hash);
        MatcherAssert.assertThat(id, Matchers.equalTo(110));
    }

    @Test
    public void shouldDecodeAZeroHash() {
        String hash = "a";
        Integer id = urlShortener.encode(hash);
        MatcherAssert.assertThat(id, Matchers.equalTo(0));
    }


}
