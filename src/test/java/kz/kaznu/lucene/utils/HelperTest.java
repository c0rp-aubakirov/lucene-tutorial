package kz.kaznu.lucene.utils;

import org.apache.lucene.document.Document;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.util.List;

/**
 * User: Sanzhar Aubakirov
 * Date: 2/18/16
 */
public class HelperTest {

    @Test
    public void testReadDocumentsFromFile() throws Exception {
        final ClassLoader classLoader = getClass().getClassLoader();
        final File file = new File(classLoader.getResource("tutorial.json").getFile());
        final List<Document> documents = Helper.readDocumentsFromFile(file);
        Assert.assertEquals("Should successfully read 17 documents", 17, documents.size());
    }
}