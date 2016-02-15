package it.w0rd.persistence;

import it.w0rd.persistence.db.DictionaryHash;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.nio.file.Files.lines;

public class WordNetHelper {

    public static List<DictionaryHash> parseFile(String filename) throws IOException {
        Stream<String> lines = lines(Paths.get(filename), Charset.defaultCharset());
        List<DictionaryHash> words = lines
                .map(line -> {
                    Pattern validLinePattern = Pattern.compile("[0-9]{8,8} [^ ]* [^ ]* [^ ]* ([a-z]*).*\\| (.*)$");
                    Matcher matcher = validLinePattern.matcher(line);
                    if (matcher.find()) {
                        String word = matcher.group(1);

                        if (word.length() > 20 || word.length() < 2) return null;

                        String description = matcher.group(2);
                        String trimmedDescription = (description.length() > 250) ? description.substring(0, 246) + "..." : description;

                        if ("".equals(word) || "".equals(trimmedDescription) || word == null || trimmedDescription == null)
                            return null;
                        return new DictionaryHash(word, "en", trimmedDescription, true);
                    } else {
                        return null;
                    }
                })
                .filter(w -> Objects.nonNull(w))
                .collect(Collectors.toList());

        return words;
    }

    private static File[] getFilesInDirectory(String wordNetDirectory) {
        URL resource = WordNetHelper.class.getClassLoader().getResource(wordNetDirectory);
        return new File(resource.getPath()).listFiles();
    }

    public static List<DictionaryHash> loadAllWordsMatching(String wordNetDirectory, Predicate predicate) throws IOException {
        File[] wordNetFiles = getFilesInDirectory(wordNetDirectory);

        List<DictionaryHash> allWords = new ArrayList<>();
        for (int i = 0; i < wordNetFiles.length; ++i) {
            List<DictionaryHash> wordsInDocument = parseFile(wordNetFiles[i].toString());
            for (DictionaryHash dictionaryHash : wordsInDocument) {
                if (predicate.test(dictionaryHash)) {
                    allWords.add(dictionaryHash);
                }
            }
        }

        return allWords;
    }

    public static List<DictionaryHash> loadDirectoryForTests(String wordNetDirectory) throws IOException {
        File[] wordNetFiles = getFilesInDirectory(wordNetDirectory);

        List<DictionaryHash> allWords = new ArrayList<>();
        for (int i = 0; i < wordNetFiles.length; ++i) {
            List<DictionaryHash> wordsInDocument = parseFile(wordNetFiles[i].toString());
            allWords.addAll(wordsInDocument);
        }

        int cap = allWords.size() > 15000 ? 15000 : allWords.size();
        return allWords.subList(0, cap);
    }

}