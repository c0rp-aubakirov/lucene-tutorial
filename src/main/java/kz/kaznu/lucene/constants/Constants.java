package kz.kaznu.lucene.constants;

/**
 * Globally shared constants should be here
 */
public final class Constants {

    private Constants() {
    }

    public static String TMP_DIR = System.getProperty("java.io.tmpdir");
    public static final int DEFAULT_LIMIT = 10;
    public static final String AUTOCOMPLETE_SCORE_FIELD = "scoreValue";
    public static final String AUTOCOMPLETE_FIELD = "term";
}
