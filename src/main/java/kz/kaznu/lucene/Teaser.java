package kz.kaznu.lucene;

import kz.kaznu.lucene.index.MessageIndexer;
import kz.kaznu.lucene.index.MessageToDocument;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryparser.classic.ParseException;

import java.io.IOException;
import java.util.Scanner;

public class Teaser {
    private static final String teaserTitle = "Привет Хабр!";
    private static final String teaserBody = "Это демонстрация работы простейшего нечёткого поиска";

    public static void main(String[] args) throws IOException, ParseException {
        final Document teaserDoc = MessageToDocument.createWith(teaserTitle, teaserBody);
        final MessageIndexer indexer = new MessageIndexer("/tmp/teaser_index");
        indexer.index(true, teaserDoc);

        final BasicSearchExamples search = new BasicSearchExamples(indexer.readIndex());

        final Scanner reader = new Scanner(System.in);  // Reading from System.in
        System.out.print("Введите запрос:\t");
        final String toSearch = reader.nextLine(); // Scans the next token

        search.fuzzySearch(toSearch);
    }
}
