package kz.kaznu.lucene.model;

/**
 * User: Sanzhar Aubakirov
 * Date: 2/28/16
 */
public class Autocomplete {
    private String value;
    private Double score;

    public Autocomplete(Double score, String value) {
        this.score = score;
        this.value = value;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
