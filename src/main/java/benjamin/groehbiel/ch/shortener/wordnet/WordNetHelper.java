package benjamin.groehbiel.ch.shortener.wordnet;

import benjamin.groehbiel.ch.shortener.db.DictionaryHash;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.nio.file.Files.lines;
import static java.util.stream.Collectors.toList;

public class WordNetHelper {

    public static List<WordDefinition> parse(String filename) throws IOException {
        Stream<String> lines = lines(Paths.get(filename), Charset.defaultCharset());
        List<WordDefinition> words = lines
                .map(line -> {
                    Pattern validLinePattern = Pattern.compile("[0-9]{8,8} [^ ]* [^ ]* [^ ]* ([a-z]*).*\\| (.*)$");
                    Matcher matcher = validLinePattern.matcher(line);
                    if (matcher.find()) {
                        String word = matcher.group(1);
                        String desc = matcher.group(2);
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


    public static List<Path> scan(String wordNetDirectoryPath) throws IOException {
        File wordNetDirectory = new File(wordNetDirectoryPath);

        ArrayList<Path> dictFiles = new ArrayList<>();
        for (File f : wordNetDirectory.listFiles()) {
            dictFiles.add(Paths.get(f.getPath()));
        }

        return dictFiles;
    }


    public static List<WordDefinition> load(String wordNetDirectory) throws IOException {
        List<WordDefinition> allWords = new ArrayList<>();

        List<Path> wordNetFiles = scan(wordNetDirectory);
        for (Path dictFile : wordNetFiles) {
            List<WordDefinition> wordsInDocument = parse(dictFile.toString());
            allWords.addAll(wordsInDocument);
        }

        return allWords;
    }

    public static List<DictionaryHash> turnIntoDictionaryHashes(List<WordDefinition> words) {
        return words.stream().map(w ->
            new DictionaryHash(w.getWord(), "en", w.getDescription(), true)
        ).collect(toList());
    }

}