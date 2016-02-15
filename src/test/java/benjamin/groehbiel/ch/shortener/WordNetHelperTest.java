package benjamin.groehbiel.ch.shortener;

import benjamin.groehbiel.ch.shortener.WordNetHelper;
import benjamin.groehbiel.ch.shortener.db.DictionaryHash;
import org.hamcrest.MatcherAssert;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.function.Predicate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.IsEqual.equalTo;

public class WordNetHelperTest {

    @Test
    public void shouldReturnAListOfAllWordDefinitions() throws Exception {
        List<DictionaryHash> words = WordNetHelper.loadDirectory("WordNet");
        MatcherAssert.assertThat(words, hasSize(21));
    }

    @Test
    public void shouldReturnAListOfAllWordsLength8() throws IOException {
        List<DictionaryHash> words = WordNetHelper.loadAllWordsMatching("WordNet", new Predicate<DictionaryHash>() {
            @Override
            public boolean test(DictionaryHash dictionaryHash) {
                String word = dictionaryHash.getHash();

                if (word.length() <= 8) return true;
                return false;
            }
        });

        assertThat(words.size(), equalTo(15));
    }

    @Test
    public void shouldParseWordNetFileAndReturnDictioary() throws Exception {
        String filePath = WordNetHelper.class.getClassLoader().getResource("WordNet/test_data.adj").getPath();
        List<DictionaryHash> dict = WordNetHelper.parseFile(filePath);
        assertThat(dict, hasSize(20));
    }

}
