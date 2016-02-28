package kz.kaznu.lucene;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static kz.kaznu.lucene.constants.Constants.DEFAULT_LIMIT;

public class AutocompleteExamples {
    private final String SEARCH_FIELD = "term";
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
    public List<String> withTermQuery(final String toSearch) throws IOException, ParseException {
        final IndexSearcher indexSearcher = new IndexSearcher(reader);

        final Term term = new Term(SEARCH_FIELD, toSearch);
        final Query query = new TermQuery(term);
        final Sort sortByScore = new Sort(new SortField("scoreValue", SortField.Type.DOUBLE)); // sort by tfidf
        final TopDocs search = indexSearcher.search(query, SEARCH_LIMIT, sortByScore);
        final ScoreDoc[] hits = search.scoreDocs;

        final List<String> result = new ArrayList<>();
        for (ScoreDoc hit : hits) {
            result.add(reader.document(hit.doc).get(SEARCH_FIELD));
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

        final Term term = new Term(SEARCH_FIELD, toSearch);

        final Query query = new FuzzyQuery(term, fuzziness);
        final Sort sortByScore = new Sort(new SortField("scoreValue", SortField.Type.DOUBLE)); // sort by tfidf
        final TopDocs search = indexSearcher.search(query, DEFAULT_LIMIT, sortByScore);
        final ScoreDoc[] hits = search.scoreDocs;

        final List<String> result = new ArrayList<>();
        for (ScoreDoc hit : hits) {
            result.add(reader.document(hit.doc).get(SEARCH_FIELD));
        }

        return result;
    }

}
