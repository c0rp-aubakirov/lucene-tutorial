package kz.kaznu.lucene.index;

import kz.kaznu.lucene.analyzer.CustomizableRussianAnalyzer;
import kz.kaznu.lucene.constants.Constants;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.ngram.EdgeNGramTokenizer;
import org.apache.lucene.analysis.ngram.NGramTokenizer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.*;
import org.apache.lucene.util.BytesRef;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * User: Sanzhar Aubakirov
 * Date: 2/28/16
 */
public class TokenizerTest {
    private static final String teaserBody = "демонстрация ngram токенизатора";

    /**
     * Тесты в этом классе показывают разницу между NGramTokenizer и EdgeNgramTokenizer
     * Поиграйте с тестами, меняйте аргументы конструкторов NGramTokenizer и EdgeNgramTokenizer
     */

    @Test
    public void ngramTokenizerShowCase() throws IOException {
        final Document ngram = MessageToDocument.createAutocompleteWith(teaserBody, 0D);
        final MessageIndexer indexer = new MessageIndexer("/tmp/ngram_index");

        // Key point of this test. Creating Analyzer with NGramTokenizer.
        final Analyzer analyzer = new CustomizableRussianAnalyzer(new NGramTokenizer(2, 15)).ifNeedLengthFilter(false);

        indexer.index(true, ngram, analyzer);
        System.out.println(getAllTermsFromIndex(indexer.readIndex()));

        indexer.deleteIndexFolder();
    }

    @Test
    public void edgeNgramTokenizerShowCase() throws IOException {
        final Document edgeNgram = MessageToDocument.createAutocompleteWith(teaserBody, 0D);
        final MessageIndexer indexer = new MessageIndexer("/tmp/edge_ngram_index");

        // Key point of this test. Creating Analyzer with EdgeNGramTokenizer.
        final Analyzer analyzer = new CustomizableRussianAnalyzer(new EdgeNGramTokenizer(2, 15)).ifNeedLengthFilter(false);

        indexer.index(true, edgeNgram, analyzer);
        System.out.println(getAllTermsFromIndex(indexer.readIndex()));

        indexer.deleteIndexFolder();
    }


    private List<String> getAllTermsFromIndex(final IndexReader reader) throws IOException {
        final List<String> result = new ArrayList<>();

        final Fields fields = MultiFields.getFields(reader);
        final Terms bodyTerms = fields.terms(Constants.AUTOCOMPLETE_FIELD);
        final TermsEnum iterator = bodyTerms.iterator();
        BytesRef term;
        while ((term = iterator.next()) != null) {
            result.add(term.utf8ToString());
        }
        return result;
    }
}
