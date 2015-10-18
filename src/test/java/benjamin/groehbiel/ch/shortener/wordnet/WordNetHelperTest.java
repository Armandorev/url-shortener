package benjamin.groehbiel.ch.shortener.wordnet;

import benjamin.groehbiel.ch.shortener.db.DictionaryHash;
import org.hamcrest.MatcherAssert;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;

public class WordNetHelperTest {

    @Test
    public void shouldReturnAListOfAllWordDefinitions() throws Exception {
        List<DictionaryHash> words = WordNetHelper.loadDirectory("WordNet");
        MatcherAssert.assertThat(words, hasSize(21));
    }

    @Test
    public void shouldParseWordNetFileAndReturnDictioary() throws Exception {
        String filePath = WordNetHelper.class.getClassLoader().getResource("WordNet/test_data.adj").getPath();
        List<DictionaryHash> dict = WordNetHelper.parseFile(filePath);
        assertThat(dict, hasSize(20));
    }

}
