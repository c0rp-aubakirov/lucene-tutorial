package kz.kaznu.lucene.utils;

import org.apache.commons.math3.linear.ArrayRealVector;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
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
}
