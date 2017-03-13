package kz.kaznu.lucene;

import kz.kaznu.lucene.utils.Word2Vec;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: Sanzhar Aubakirov
 * Date: 2/28/16
 */
public class Word2VecTest {
    private final Map<String, ArrayRealVector> word2vec = new HashMap<>();

    @BeforeClass
    public void word2vecRead() throws URISyntaxException, IOException {
        word2vec.putAll(Word2Vec.word2vecRead());
    }

    @Test
    public void cosineDistance() {
        final String test = "огонь";
        final List<String> mostWordsSimilarByCosineDistance = Word2Vec.getMostWordsSimilarByCosineDistance(word2vec,
                                                                                                           test);
        mostWordsSimilarByCosineDistance.forEach(System.out::println);
    }

}
