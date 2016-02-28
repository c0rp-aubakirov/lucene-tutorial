package kz.kaznu.web.bean;

/**
 * User: Sanzhar Aubakirov
 * Date: 2/28/16
 */

import kz.kaznu.lucene.AutocompleteExamples;
import kz.kaznu.lucene.analyzer.CustomizableRussianAnalyzer;
import kz.kaznu.lucene.constants.Constants;
import kz.kaznu.lucene.index.MessageIndexer;
import kz.kaznu.lucene.index.MessageToDocument;
import kz.kaznu.lucene.utils.Helper;
import org.apache.commons.io.FileUtils;
import org.apache.lucene.analysis.ngram.EdgeNGramTokenizer;
import org.apache.lucene.analysis.ru.RussianAnalyzer;
import org.apache.lucene.analysis.shingle.ShingleAnalyzerWrapper;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.*;
import org.apache.lucene.search.DocIdSetIterator;
import org.apache.lucene.search.similarities.DefaultSimilarity;
import org.apache.lucene.search.similarities.TFIDFSimilarity;
import org.apache.lucene.util.BytesRef;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class IndexesBean {
    private final MessageIndexer msgIndexer = new MessageIndexer(Constants.TMP_DIR + "/tutorial_messages");
    private final MessageIndexer autocompleteIndexer = new MessageIndexer(Constants.TMP_DIR + "/tutorial_autocomplete");
    private final AutocompleteExamples autocomplete;

    public IndexesBean() throws IOException {
        final ClassLoader classLoader = getClass().getClassLoader();
        final File file = new File(classLoader.getResource("tutorial.json").getFile());
        final CustomizableRussianAnalyzer msgAnalyzer = new CustomizableRussianAnalyzer().ifNeedStemming(false);
        final ShingleAnalyzerWrapper wrapper = new ShingleAnalyzerWrapper(msgAnalyzer, 2, 3, " ", true, false,
                                                                                   "");
        msgIndexer.index(true, Helper.readDocumentsFromFile(file),
                         wrapper); // create messages index

        final Map<String, Double> allTermMap = initAutocompleteDictionary(msgIndexer.readIndex());
        // get list of documents from terms
        final List<Document> autocompletes = allTermMap
                .entrySet().stream()
                .map(entry -> MessageToDocument.createAutocompleteWith(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());

        final CustomizableRussianAnalyzer analyzer = new CustomizableRussianAnalyzer(
                new EdgeNGramTokenizer(1, 20)).ifNeedRemoveShort(false).ifNeedStemming(false);
        autocompleteIndexer.index(true, autocompletes,
                                  analyzer);

        autocomplete = new AutocompleteExamples(autocompleteIndexer.readIndex());
    }

    @PreDestroy
    private void clearIndexFolders() {
        FileUtils.deleteQuietly(new File(msgIndexer.getPathToIndexFolder())); // remove indexes
        FileUtils.deleteQuietly(new File(autocompleteIndexer.getPathToIndexFolder())); // remove indexes
    }

    /**
     * Map that consists of term and its TFIDF value.
     * Map Key is term
     * Map Value is TFIDF value that is used for sorting
     */
    private Map<String, Double> initAutocompleteDictionary(final IndexReader reader) throws IOException {
        final Map<String, Double> allTermMap = new HashMap<>();

        final TFIDFSimilarity tfidfSIM = new DefaultSimilarity();
        final Fields fields = MultiFields.getFields(reader);
        final Terms bodyTerms = fields.terms("body");
        final TermsEnum iterator = bodyTerms.iterator();
        BytesRef term;
        while ((term = iterator.next()) != null) {
            final String termString = term.utf8ToString();
            final double tf = tfidfSIM.tf(iterator.docFreq());
            int numDocs = 0;
            if (iterator.seekExact(term)) {
                final PostingsEnum docsEnum = iterator.postings(null);
                while (docsEnum.nextDoc() != DocIdSetIterator.NO_MORE_DOCS) {
                    numDocs++;
                }
            }
            final double idf = tfidfSIM.idf(numDocs, reader.numDocs());
            final double tfidf = idf * tf;
            // exclude terms that shorter than 4 characters
            if (termString.length() > 4) {
                allTermMap.put(termString, tfidf);
            }
        }

        return allTermMap;
    }

    public AutocompleteExamples autocomplete() {
        return autocomplete;
    }

    public MessageIndexer getAutocompleteIndexer() {
        return autocompleteIndexer;
    }

    public MessageIndexer getMsgIndexer() {
        return msgIndexer;
    }
}
