package benjamin.groehbiel.ch.shortener.hash;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;


public class URLShortenerTest {
    HashBasedURLShortener hashBasedUrlShortener = new HashBasedURLShortener();

    @Test
    public void shouldEncode0ToHash() throws Exception {
        String hash = hashBasedUrlShortener.decode(0L);
        MatcherAssert.assertThat(hash, Matchers.equalTo("a"));
    }

    @Test
    public void shouldEncode1ToHash() throws Exception {
        String hash = hashBasedUrlShortener.decode(1L);
        MatcherAssert.assertThat(hash, Matchers.equalTo("b"));
    }

    @Test
    public void shouldEncode11ToHash() throws Exception {
        String hash = hashBasedUrlShortener.decode(10L);
        MatcherAssert.assertThat(hash, Matchers.equalTo("ba"));
    }

    @Test
    public void shouldEncode101ToHash() throws Exception {
        String hash = hashBasedUrlShortener.decode(101L);
        MatcherAssert.assertThat(hash, Matchers.equalTo("bab"));
    }

    @Test
    public void shouldDecode() {
        String hash = "bba";
        Long id = hashBasedUrlShortener.encode(hash);
        MatcherAssert.assertThat(id, Matchers.equalTo(110L));
    }

    @Test
    public void shouldDecodeAZeroHash() {
        String hash = "a";
        Long id = hashBasedUrlShortener.encode(hash);
        MatcherAssert.assertThat(id, Matchers.equalTo(0L));
    }

}
