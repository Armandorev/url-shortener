package benjamin.groehbiel.ch.shortener;

import benjamin.groehbiel.ch.shortener.URLShortener;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;


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
        Long id = urlShortener.encode(hash);
        MatcherAssert.assertThat(id, Matchers.equalTo(110L));
    }

    @Test
    public void shouldDecodeAZeroHash() {
        String hash = "a";
        Long id = urlShortener.encode(hash);
        MatcherAssert.assertThat(id, Matchers.equalTo(0L));
    }


}
