package kz.kaznu.lucene;

import kz.kaznu.lucene.constants.Constants;
import kz.kaznu.lucene.index.MessageIndexer;
import kz.kaznu.lucene.utils.Helper;
import org.apache.commons.io.FileUtils;
import org.apache.lucene.document.Document;
import org.junit.After;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.*;

/**
 * User: Sanzhar Aubakirov
 * Date: 2/18/16
 */
public class BasicSearchExamplesTest {
    private final Random rnd = new Random(); // to generate safe name for index folder. After tests we removing folders
    private final MessageIndexer indexer = new MessageIndexer(Constants.TMP_DIR + "/tutorial_test" + rnd.nextInt());
    final ClassLoader classLoader = getClass().getClassLoader();
    final File file = new File(classLoader.getResource("tutorial.json").getFile());
    final List<Document> documents;

    public BasicSearchExamplesTest() throws FileNotFoundException {
        documents = Helper.readDocumentsFromFile(file);
    }

    @Test
    public void testSearch() throws Exception {
        indexer.index(true, documents); // create index

        final BasicSearchExamples searchWith = new BasicSearchExamples(indexer.readIndex());
        searchWith.searchInBody("корреспондент");
    }

    @Test
    public void testSearchWithMistake() throws Exception {
        indexer.index(true, documents); // create index

        final BasicSearchExamples searchWith = new BasicSearchExamples(indexer.readIndex());
        searchWith.searchInBody("кореспондент");
    }

    @Test
    public void fuzzySearchWithMistake() throws Exception {
        indexer.index(true, documents); // create index

        final BasicSearchExamples searchWith = new BasicSearchExamples(indexer.readIndex());
        searchWith.fuzzySearch("кореспондент");
    }

    @Test
    public void fuzzySearch() throws Exception {
        indexer.index(true, documents); // create index

        final BasicSearchExamples searchWith = new BasicSearchExamples(indexer.readIndex());
        searchWith.fuzzySearch("дорога");
    }

    @After
    public void removeIndexes() {
        FileUtils.deleteQuietly(new File(indexer.getPathToIndexFolder())); // remove indexes
    }
}