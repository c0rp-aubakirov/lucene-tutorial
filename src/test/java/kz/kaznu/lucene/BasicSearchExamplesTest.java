package kz.kaznu.lucene;

import kz.kaznu.lucene.constants.Constants;
import kz.kaznu.lucene.index.MessageIndexer;
import kz.kaznu.lucene.utils.Helper;
import org.apache.commons.io.FileUtils;
import org.apache.lucene.document.Document;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Random;


public class BasicSearchExamplesTest {
    private final Random rnd = new Random(); // to generate safe name for index folder. After tests we removing folders
    private final MessageIndexer indexer = new MessageIndexer(Constants.TMP_DIR + "/tutorial_test" + rnd.nextInt());
    private final ClassLoader classLoader = getClass().getClassLoader();
    private final File file = new File(classLoader.getResource("tutorial.json").getFile());

    private List<Document> documents;

    @BeforeClass
    public void init() throws FileNotFoundException {
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

    @AfterClass
    public void removeIndexes() {
        FileUtils.deleteQuietly(new File(indexer.getPathToIndexFolder())); // remove indexes
    }
}