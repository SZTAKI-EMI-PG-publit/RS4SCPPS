import java.util.Objects;

public class Term {
    String original;
    String stemmed;
    Integer weight;

    public Term(String original, String stemmed, Integer weight) {
        this.original = original;
        this.stemmed = stemmed;
        this.weight = weight;
    }

    public String getOriginal() {
        return original;
    }

    public void setOriginal(String original) {
        this.original = original;
    }

    public String getStemmed() {
        return stemmed;
    }

    public void setStemmed(String stemmed) {
        this.stemmed = stemmed;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Term term = (Term) o;
        return Objects.equals(original, term.original) && Objects.equals(stemmed, term.stemmed) && Objects.equals(weight, term.weight);
    }

    @Override
    public int hashCode() {
        return Objects.hash(original, stemmed, weight);
    }
}
