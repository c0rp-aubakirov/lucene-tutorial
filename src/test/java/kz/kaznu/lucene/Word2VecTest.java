package kz.kaznu.lucene;

import kz.kaznu.lucene.utils.Word2Vec;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Stream;

/**
 * User: Sanzhar Aubakirov
 * Date: 2/28/16
 */
public class Word2VecTest {
    private final Map<String, ArrayRealVector> word2vec = new HashMap<>();

    @Before
    public void word2vecRead() throws URISyntaxException, IOException {
        word2vec.putAll(Word2Vec.word2vecRead());
    }

    @Test
    public void cosineDistance() {
        final String test = "пожар";
        final ArrayRealVector arrayRealVector = word2vec.get(test);
        final Map<Double, String> sortedCosineDistances = new TreeMap<>((Comparator) (o1, o2) -> ((Double)o2).compareTo((Double)(o1)));
        if (arrayRealVector != null) {
            word2vec.entrySet()
                    .stream()
                    .filter(w -> !w.getKey().equals(test))
                    .forEach(w -> sortedCosineDistances.put(w.getValue().cosine(arrayRealVector), w.getKey())
            );
        }

        sortedCosineDistances.entrySet().stream().filter(e-> e.getKey() > 0.7D).forEach(System.out::println);
    }

}
