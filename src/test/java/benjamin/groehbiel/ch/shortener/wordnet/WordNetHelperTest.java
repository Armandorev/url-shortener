package benjamin.groehbiel.ch.shortener.wordnet;

import org.hamcrest.MatcherAssert;
import org.junit.Test;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

public class WordNetHelperTest {

    @Test
    public void shouldReturnAListOfAllWordDefinitions() throws Exception {
        List<WordDefinition> words = WordNetHelper.loadDirectory("WordNet");
        MatcherAssert.assertThat(words, hasSize(21));
    }

    @Test
    public void shouldParseWordNetFileAndReturnDictioary() throws Exception {
        String filePath = WordNetHelper.class.getClassLoader().getResource("WordNet/test_data.adj").getPath();
        List<WordDefinition> dict = WordNetHelper.parseFile(filePath);
        assertThat(dict, hasSize(20));
    }

}
