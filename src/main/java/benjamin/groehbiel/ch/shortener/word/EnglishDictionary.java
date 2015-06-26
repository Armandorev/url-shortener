package benjamin.groehbiel.ch.shortener.word;

import org.springframework.stereotype.Repository;

import java.util.LinkedList;
import java.util.List;

@Repository
public class EnglishDictionary {

    private List<WordDefinition> words;

    public EnglishDictionary () {
        words = load();
    }

    public WordDefinition get() {
        return words.remove(0);
    }

    public void set(List<WordDefinition> words) {
        this.words = words;
    }

    public void clear() {
        words.clear();
    }

    public int size() {
        return words.size();
    }

    public List<WordDefinition> load() {
        List<WordDefinition> words = new LinkedList<>();
        words.add(new WordDefinition("fun", "enjoyment, amusement, or light-hearted pleasure."));
        words.add(new WordDefinition("eloquence", "fluent or persuasive speaking or writing."));
        words.add(new WordDefinition("elephant", "a very large plant-eating mammal with a prehensile trunk, long curved ivory tusks, and large ears, native to Africa and southern Asia."));
        words.add(new WordDefinition("rock", "the solid mineral material forming part of the surface of the earth and other similar planets, exposed on the surface or underlying the soil."));
        words.add(new WordDefinition("music", "vocal or instrumental sounds (or both) combined in such a way as to produce beauty of form, harmony, and expression of emotion."));
        words.add(new WordDefinition("society", "the aggregate of people living together in a more or less ordered community."));
        words.add(new WordDefinition("money", "a current medium of exchange in the form of coins and banknotes; coins and banknotes collectively."));
        words.add(new WordDefinition("thing", "an action; \"how could you do such a thing?\""));
        words.add(new WordDefinition("abort", "the act of terminating a project or procedure before it is completed; \"I wasted a year of my life working on an abort\";"));
        words.add(new WordDefinition("brush", "contact with something dangerous or undesirable; \"I had a brush with danger on my way to work\";"));
        words.add(new WordDefinition("egress", "the act of coming (or going) out; becoming apparent."));
        return words;
    }
}
