package kz.kaznu.lucene.index;

import kz.kaznu.lucene.constants.Constants;
import org.apache.commons.io.FileUtils;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.Random;

public class MessageIndexerTest {
    private final Random rnd = new Random(); // to generate safe name for index folder. After tests we removing folders

    private final MessageIndexer indexer = new MessageIndexer(Constants.TMP_DIR + "/tutorial_test" + rnd.nextInt());
    private final String body = "Это тестовое тело документа.";
    private final String title = "Это тестовый загаловок документа.";

    private Document document = null;

    @Test
    @Before
    public void testIndex() throws Exception {
        document = MessageToDocument.createWith(title, body);
        Assert.assertNotNull("Created document should not be null", document);
        Assert.assertEquals(body, document.get("body"));
        Assert.assertEquals(title, document.get("title"));

    }

    @Test
    @After
    public void testReadIndex() throws Exception {
        indexer.index(true, document); // should create index without any errors

        final IndexReader indexReader = indexer.readIndex();
        Assert.assertNotNull("IndexReader should not be null", indexReader);

        FileUtils.deleteQuietly(new File(indexer.getPathToIndexFolder())); // remove indexes
    }
}