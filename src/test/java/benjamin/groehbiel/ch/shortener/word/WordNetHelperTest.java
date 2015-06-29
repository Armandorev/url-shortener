package benjamin.groehbiel.ch.shortener.word;

import benjamin.groehbiel.ch.shortener.word.WordDefinition;
import benjamin.groehbiel.ch.shortener.word.WordNetHelper;
import org.hamcrest.MatcherAssert;
import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasSize;


public class WordNetHelperTest {

    @Test
    public void shouldParseWordNetFileAndReturnDictioary() throws Exception {
        List<WordDefinition> dict = WordNetHelper.parse("src/test/resources/WordNet/test_data.adj");
        assertThat(dict, hasSize(20));
    }

    @Test
    public void shouldListAllWordNetFilesInResources() throws Exception {
        List<Path> wordnetFiles = WordNetHelper.scan("src/test/resources/WordNet/");
        assertThat(wordnetFiles, hasSize(2));
        assertThat(wordnetFiles, contains(
                Paths.get("src/test/resources/WordNet/test_data.adj"),
                Paths.get("src/test/resources/WordNet/test_data2.adj")
        ));
    }

    @Test
    public void shouldReturnAListOfAllWordDefinitions() throws Exception {
        List<WordDefinition> words = WordNetHelper.load("src/test/resources/WordNet/");
        MatcherAssert.assertThat(words, hasSize(21));
    }

}
