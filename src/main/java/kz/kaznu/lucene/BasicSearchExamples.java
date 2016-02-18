package kz.kaznu.lucene;

import org.apache.lucene.analysis.ru.RussianAnalyzer;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;

import java.io.IOException;

/**
 * User: Sanzhar Aubakirov
 * Date: 2/18/16
 */
public class BasicSearchExamples {
    public static final int DEFAULT_LIMIT = 10;
    private final IndexReader reader;

    public BasicSearchExamples(IndexReader reader) {
        this.reader = reader;
    }

    /**
     * Search using TermQuery
     * @param toSearch string to search
     * @param searchField field where to search. We have "body" and "title" fields
     * @param limit how many results to return
     * @throws IOException
     * @throws ParseException
     */
    public void searchIndexWithTermQuery(final String toSearch, final String searchField, final int limit) throws IOException, ParseException {
        final IndexSearcher indexSearcher = new IndexSearcher(reader);

        final Term term = new Term(searchField, toSearch);
        final Query query = new TermQuery(term);
        final TopDocs search = indexSearcher.search(query, limit);
        final ScoreDoc[] hits = search.scoreDocs;
        showHits(hits);
    }

    /**
     * This is wrapper to searchIndexWithTermQuery
     * It executes searchIndexWithTermQuery using "body" field and limiting to 10 results
     *
     * @param toSearch string to search in the "body" field
     * @throws IOException
     * @throws ParseException
     */
    public void searchIndexWithTermQueryByBody(final String toSearch) throws IOException, ParseException {
        searchIndexWithTermQuery(toSearch, "body", DEFAULT_LIMIT);
    }

    /**
     * Search in body using QueryParser
     * @param toSearch string to search
     * @param limit how many results to return
     * @throws IOException
     * @throws ParseException
     */
    public void searchInBody(final String toSearch, final int limit) throws IOException, ParseException {
        final IndexSearcher indexSearcher = new IndexSearcher(reader);

        final QueryParser queryParser = new QueryParser("body", new RussianAnalyzer());
        final Query query = queryParser.parse(toSearch);
        System.out.println("Type of query: " + query.getClass().getSimpleName());

        final TopDocs search = indexSearcher.search(query, limit);
        final ScoreDoc[] hits = search.scoreDocs;
        showHits(hits);
    }

    /**
     * This is wrapper to searchInBody function
     * it executes searchInBody with default limiting to 10 results
     *
     * @param toSearch
     * @throws IOException
     * @throws ParseException
     */
    public void searchInBody(final String toSearch) throws IOException, ParseException {
        searchInBody(toSearch, DEFAULT_LIMIT);
    }

    /**
     * Search using FuzzyQuery.
     * @param toSearch string to search
     * @param searchField field where to search. We have "body" and "title" fields
     * @param limit how many results to return
     * @throws IOException
     * @throws ParseException
     */
    public void fuzzySearch(final String toSearch, final String searchField, final int limit) throws IOException, ParseException {
        final IndexSearcher indexSearcher = new IndexSearcher(reader);

        final Term term = new Term(searchField, toSearch);

        final int maxEdits = 2; // This is very important variable. It regulates fuzziness of the query
        final Query query = new FuzzyQuery(term, maxEdits);
        final TopDocs search = indexSearcher.search(query, limit);
        final ScoreDoc[] hits = search.scoreDocs;
        showHits(hits);
    }

    /**
     * Wrapper to fuzzySearch function.
     * It executed fuzzySearch with default limit and body field as target field
     *
     * @param toSearch string to search
     * @throws IOException
     * @throws ParseException
     */
    public void fuzzySearch(final String toSearch) throws IOException, ParseException {
        fuzzySearch(toSearch, "body", DEFAULT_LIMIT);
    }

    private void showHits(final ScoreDoc[] hits) throws IOException {
        if (hits.length == 0) {
            System.out.println("\n\tНичего не найдено");
            return;
        }
        System.out.println("\n\tРезультаты поиска:");
        for (ScoreDoc hit : hits) {
            final String title = reader.document(hit.doc).get("title");
            final String body = reader.document(hit.doc).get("body");
            System.out.println("\n\tDocument Id = " + hit.doc + "\n\ttitle = " + title + "\n\tbody = " + body);
        }
    }
}
