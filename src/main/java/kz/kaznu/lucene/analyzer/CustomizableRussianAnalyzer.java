package kz.kaznu.lucene.analyzer;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.miscellaneous.LengthFilter;
import org.apache.lucene.analysis.snowball.SnowballFilter;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.standard.std40.StandardTokenizer40;
import org.apache.lucene.analysis.util.CharArraySet;
import org.apache.lucene.analysis.util.StopwordAnalyzerBase;
import org.apache.lucene.analysis.util.WordlistLoader;
import org.apache.lucene.util.IOUtils;
import org.apache.lucene.util.Version;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * User: Sanzhar Aubakirov
 * Date: 1/12/16
 */
public class CustomizableRussianAnalyzer extends StopwordAnalyzerBase {

    private Boolean ifNeedRemoveShort = true;
    private Boolean ifNeedStemming = true;
    private Tokenizer tokenizer = null;

    /**
     * File containing default Russian stopwords.
     */
    public final static String DEFAULT_STOPWORD_FILE = "russian_stop.txt";

    private static class DefaultSetHolder {
        static final CharArraySet DEFAULT_STOP_SET;

        static {
            try {
                DEFAULT_STOP_SET = WordlistLoader.getSnowballWordSet(IOUtils.getDecodingReader(SnowballFilter.class,
                                                                                               DEFAULT_STOPWORD_FILE,
                                                                                               StandardCharsets.UTF_8));
            } catch (IOException ex) {
                // default set should always be present as it is part of the
                // distribution (JAR)
                throw new RuntimeException("Unable to load default stopword set", ex);
            }
        }
    }

    private final CharArraySet stemExclusionSet;

    /**
     * Returns an unmodifiable instance of the default stop-words set.
     *
     * @return an unmodifiable instance of the default stop-words set.
     */
    public static CharArraySet getDefaultStopSet() {
        return DefaultSetHolder.DEFAULT_STOP_SET;
    }

    public CustomizableRussianAnalyzer(final Tokenizer tokenizer) {
        this(DefaultSetHolder.DEFAULT_STOP_SET);
        this.tokenizer = tokenizer;
    }

    public CustomizableRussianAnalyzer() {
        this(DefaultSetHolder.DEFAULT_STOP_SET);
    }

    /**
     * Builds an analyzer with the given stop words
     *
     * @param stopwords a stopword set
     */
    public CustomizableRussianAnalyzer(CharArraySet stopwords) {
        this(stopwords, CharArraySet.EMPTY_SET);
    }

    /**
     * Builds an analyzer with the given stop words
     *
     * @param stopwords        a stopword set
     * @param stemExclusionSet a set of words not to be stemmed
     */
    public CustomizableRussianAnalyzer(CharArraySet stopwords, CharArraySet stemExclusionSet) {
        super(stopwords);
        this.stemExclusionSet = CharArraySet.unmodifiableSet(CharArraySet.copy(stemExclusionSet));
    }

    /**
     * Creates
     * {@link org.apache.lucene.analysis.Analyzer.TokenStreamComponents}
     * used to tokenize all the text in the provided {@link Reader}.
     *
     * @return {@link org.apache.lucene.analysis.Analyzer.TokenStreamComponents}
     * built from a {@link StandardTokenizer} filtered with
     * {@link StandardFilter}, {@link LowerCaseFilter}, {@link StopFilter}
     * , {@link SetKeywordMarkerFilter} if a stem exclusion set is
     * provided, and {@link SnowballFilter}
     */
    @Override
    protected TokenStreamComponents createComponents(String fieldName) {
        final Tokenizer source;
        if (tokenizer != null) {
            source = tokenizer;
        } else {
            if (getVersion().onOrAfter(Version.LUCENE_4_7_0)) {
                source = new StandardTokenizer();
            } else {
                source = new StandardTokenizer40();
            }
        }
        TokenStream result = new StandardFilter(source);
        result = new LowerCaseFilter(result);
        result = new StopFilter(result, stopwords);
        if (ifNeedRemoveShort) {
            result = new LengthFilter(result, 5, 15);
        }
        if (ifNeedStemming) {
            result = new SnowballFilter(result, new org.tartarus.snowball.ext.RussianStemmer());
        }
        return new TokenStreamComponents(source, result);
    }

    public CustomizableRussianAnalyzer ifNeedStemming(final Boolean ifNeedStemming) {
        this.ifNeedStemming = ifNeedStemming;
        return this;
    }

    public CustomizableRussianAnalyzer ifNeedRemoveShort(final Boolean ifNeedRemoveShort) {
        this.ifNeedRemoveShort = ifNeedRemoveShort;
        return this;
    }


}
