package kz.kaznu.lucene.utils;

import org.apache.commons.math3.linear.ArrayRealVector;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * User: Sanzhar Aubakirov
 * Date: 4/11/16
 */
public class Word2Vec {

    /**
     * Read word2vec file into Map and return this Map
     * @return return map of strings and their vector representation
     * @throws URISyntaxException
     * @throws IOException
     */
    public static Map<String, ArrayRealVector> word2vecRead() throws URISyntaxException, IOException {
        final Map<String, ArrayRealVector> word2vec = new HashMap<>();
        try (Stream<String> lines = Files.lines(Paths.get(ClassLoader.getSystemResource("word2vec.bin").toURI()),
                                                Charset.defaultCharset())) {
            lines.forEachOrdered(line -> {
                final double[] vec = new double[200];
                final String[] splited = line.split(" ");
                for (int i = 1; i < splited.length - 1; i++) {
                    vec[i] = Double.parseDouble(splited[i]);
                }
                word2vec.put(splited[0], new ArrayRealVector(vec));
            });
        }
        return word2vec;
    }

    /**
     * Extract most similar words from word2vec map
     * <p>Method uses cosine distance as measure between vectors (words)</p>
     *
     * @param word2vec should map of strings and vectors
     * @param query should be presented in word2vec map
     * @param threshold cosine distance threshold
     * @return list of words similar to <b>query</b>
     */
    public static List<String> getMostWordsSimilarByCosineDistance(final Map<String, ArrayRealVector> word2vec, final String query, final Double threshold) {
        final ArrayRealVector arrayRealVector = word2vec.get(query);
        final Map<Double, String> sortedCosineDistances = new TreeMap<>((Comparator) (o1, o2) -> ((Double)o2).compareTo((Double)(o1)));
        if (arrayRealVector != null) {
            word2vec.entrySet()
                    .stream()
                    .filter(w -> !w.getKey().equals(query))
                    .filter(w -> w.getValue().getNorm() != 0)
                    .forEach(w -> {
                        ArrayRealVector value = w.getValue();
                        double cosine = value.cosine(arrayRealVector);
                                 sortedCosineDistances.put(cosine, w.getKey());
                             }
                    );
        }

        return sortedCosineDistances.entrySet()
                .stream()
                .filter(e -> e.getKey() > threshold)
                .map(doubleStringEntry -> doubleStringEntry.getValue())
                .collect(
                        Collectors.toList());
    }

    public static List<String> getMostWordsSimilarByCosineDistance(final Map<String, ArrayRealVector> word2vec, final String query) {
        final double defaultThreshold = 0.7D;
        return getMostWordsSimilarByCosineDistance(word2vec, query, defaultThreshold);
    }
}
