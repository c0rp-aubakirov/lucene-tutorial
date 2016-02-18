package kz.kaznu.lucene.utils;

import org.apache.lucene.document.Document;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * User: Sanzhar Aubakirov
 * Date: 2/18/16
 */
public class HelperTest {

    @Test
    public void testReadDocumentsFromFile() throws Exception {
        final List<Document> documents = Helper.readDocumentsFromFile("src/test/resources/tutorial.json");
        Assert.assertEquals("Should successfully read 17 documents", 17, documents.size());
    }
}