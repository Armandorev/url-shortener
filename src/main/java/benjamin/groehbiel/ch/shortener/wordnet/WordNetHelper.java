package benjamin.groehbiel.ch.shortener.wordnet;

import benjamin.groehbiel.ch.shortener.db.DictionaryHash;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.nio.file.Files.lines;
import static java.util.stream.Collectors.toList;

public class WordNetHelper {

    public static List<WordDefinition> parseFile(String filename) throws IOException {
        Stream<String> lines = lines(Paths.get(filename), Charset.defaultCharset());
        List<WordDefinition> words = lines
                .map(line -> {
                    Pattern validLinePattern = Pattern.compile("[0-9]{8,8} [^ ]* [^ ]* [^ ]* ([a-z]*).*\\| (.*)$");
                    Matcher matcher = validLinePattern.matcher(line);
                    if (matcher.find()) {
                        String word = matcher.group(1);

                        if (word.length() > 20) return null;

                        String description = matcher.group(2);
                        String desc = (description.length() > 250) ? description.substring(0, 249) : description;

                        if ("".equals(word) || "".equals(desc) || word == null || desc == null) return null;
                        return new WordDefinition(word, desc);
                    } else {
                        return null;
                    }
                })
                .filter(w -> Objects.nonNull(w))
                .collect(Collectors.toList());

        return words;
    }


    public static List<WordDefinition> loadDirectory(String wordNetDirectory) throws IOException {
        List<WordDefinition> allWords = new ArrayList<>();
        URL resource = WordNetHelper.class.getClassLoader().getResource(wordNetDirectory);
        File[] wordNetFiles = new File(resource.getPath()).listFiles();

        for (int i = 0; i < wordNetFiles.length; ++i) {
            List<WordDefinition> wordsInDocument = parseFile(wordNetFiles[i].toString());
            allWords.addAll(wordsInDocument);
        }

        int cap = allWords.size() > 15000 ? 15000 : allWords.size();
        return allWords.subList(0, cap);
    }

    public static List<DictionaryHash> turnIntoDictionaryHashes(List<WordDefinition> words) {
        return words.stream().map(w ->
                        new DictionaryHash(w.getWord(), "en", w.getDescription(), true)
        ).collect(toList());
    }

}