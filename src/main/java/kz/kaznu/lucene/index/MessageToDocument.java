package kz.kaznu.lucene.index;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.DoubleField;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.index.DocValuesType;
import org.apache.lucene.index.IndexOptions;

/**
 * We will use this class to convert messages to Lucene documents
 */
public class MessageToDocument {

    /**
     * Creates Lucene Document using two strings: body and title
     *
     * @return resulted document
     */
    public static Document createWith(final String titleStr, final String bodyStr) {
        final Document document = new Document();

        final FieldType textIndexedType = new FieldType();
        textIndexedType.setStored(true);
        textIndexedType.setIndexOptions(IndexOptions.DOCS);
        textIndexedType.setTokenized(true);

        //index title
        Field title = new Field("title", titleStr, textIndexedType);
        //index body
        Field body = new Field("body", bodyStr, textIndexedType);

        document.add(title);
        document.add(body);
        return document;
    }

    /**
     * Creates Lucene Document for autocomplete using two string and double: term and scoring value
     *
     * @return resulted document
     */
    public static Document createAutocompleteWith(final String termString, final Double scoreValue) {
        final Document document = new Document();

        final FieldType textIndexedType = new FieldType();
        textIndexedType.setStored(true);
        textIndexedType.setIndexOptions(IndexOptions.DOCS);
        textIndexedType.setTokenized(true);

        final FieldType scoreField = new FieldType();
        scoreField.setTokenized(false);
        scoreField.setDocValuesType(DocValuesType.NUMERIC);
        scoreField.setStored(true);
        scoreField.setNumericType(FieldType.NumericType.DOUBLE);

        //index term
        Field term = new Field("term", termString, textIndexedType);
        //index score
        Field score = new DoubleField("scoreValue", scoreValue, scoreField);

        document.add(term);
        document.add(score);
        return document;
    }
}
