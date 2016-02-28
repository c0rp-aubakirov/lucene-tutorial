package kz.kaznu.lucene;

import kz.kaznu.lucene.analyzer.CustomizableRussianAnalyzer;
import kz.kaznu.lucene.constants.Constants;
import kz.kaznu.lucene.model.Autocomplete;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static kz.kaznu.lucene.constants.Constants.*;

public class AutocompleteExamples {
    private final int SEARCH_LIMIT = 7;
    private final IndexReader reader;

    public AutocompleteExamples(IndexReader reader) {
        this.reader = reader;
    }

    /**
     * Search using TermQuery
     *
     * @param toSearch string to search
     * @throws IOException
     * @throws ParseException
     */
    public List<Autocomplete> withTermQuery(final String toSearch) throws IOException, ParseException {
        final IndexSearcher indexSearcher = new IndexSearcher(reader);

        final QueryParser queryParser = new QueryParser(AUTOCOMPLETE_FIELD, new CustomizableRussianAnalyzer()
                .ifNeedRemoveShort(false)
                .ifNeedStemming(false));
        final Query query = queryParser.parse(toSearch);
        final Sort sortByScore = new Sort(new SortField(AUTOCOMPLETE_SCORE_FIELD, SortField.Type.DOUBLE, true)); // sort by tfidf
        final TopDocs search = indexSearcher.search(query, SEARCH_LIMIT, sortByScore);
        final ScoreDoc[] hits = search.scoreDocs;

        final List<Autocomplete> result = new ArrayList<>();
        for (ScoreDoc hit : hits) {
            final String value = reader.document(hit.doc).get(AUTOCOMPLETE_FIELD);
            final Double score = Double.valueOf(reader.document(hit.doc).get(AUTOCOMPLETE_SCORE_FIELD));
            result.add(new Autocomplete(score, value));
        }

        return result;
    }


    /**
     * Search using FuzzyQuery.
     *
     * @param toSearch  string to search
     * @param fuzziness regulates maxEdits param in FuzzyQuery
     * @throws IOException
     * @throws ParseException
     */
    public List<String> fuzzySearch(final String toSearch, final int fuzziness) throws IOException, ParseException {
        final IndexSearcher indexSearcher = new IndexSearcher(reader);

        final Term term = new Term(AUTOCOMPLETE_FIELD, toSearch);

        final Query query = new FuzzyQuery(term, fuzziness);
        final Sort sortByScore = new Sort(new SortField(AUTOCOMPLETE_SCORE_FIELD, SortField.Type.DOUBLE)); // sort by tfidf
        final TopDocs search = indexSearcher.search(query, DEFAULT_LIMIT, sortByScore);
        final ScoreDoc[] hits = search.scoreDocs;

        final List<String> result = new ArrayList<>();
        for (ScoreDoc hit : hits) {
            result.add(reader.document(hit.doc).get(AUTOCOMPLETE_FIELD));
        }

        return result;
    }

}
