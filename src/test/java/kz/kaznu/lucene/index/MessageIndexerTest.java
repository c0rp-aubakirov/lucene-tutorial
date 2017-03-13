package kz.kaznu.lucene.index;

import kz.kaznu.lucene.constants.Constants;
import org.apache.commons.io.FileUtils;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.util.Random;

public class MessageIndexerTest {
    private final Random rnd = new Random(); // to generate safe name for index folder. After tests we removing folders

    private final MessageIndexer indexer = new MessageIndexer(Constants.TMP_DIR + "/tutorial_test" + rnd.nextInt());
    private final String body = "Это тестовое тело документа.";
    private final String title = "Это тестовый загаловок документа.";

    private Document document = null;

    @Test
    @BeforeClass
    public void testIndex() throws Exception {
        document = MessageToDocument.createWith(title, body);
        Assert.assertNotNull(document,"Created document should not be null");
        Assert.assertEquals(body, document.get("body"));
        Assert.assertEquals(title, document.get("title"));

    }

    @Test
    @AfterClass
    public void testReadIndex() throws Exception {
        indexer.index(true, document); // should create index without any errors

        final IndexReader indexReader = indexer.readIndex();
        Assert.assertNotNull(indexReader, "IndexReader should not be null");

        FileUtils.deleteQuietly(new File(indexer.getPathToIndexFolder())); // remove indexes
    }
}