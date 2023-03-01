import java.util.List;
import java.util.Map;
import java.util.Objects;

public class SimilarityEntry {
    String correlatedDocument;
    double correlationValue;
    List<Map.Entry<Term,Term>> correlatedTermPair;

    public SimilarityEntry(String correlatedDocument, double correlationValue, List<Map.Entry<Term, Term>> correlatedTermPair) {
        this.correlatedDocument = correlatedDocument;
        this.correlationValue = correlationValue;
        this.correlatedTermPair = correlatedTermPair;
    }

    public SimilarityEntry() {
    }

    public String getCorrelatedDocument() {
        return correlatedDocument;
    }

    public void setCorrelatedDocument(String correlatedDocument) {
        this.correlatedDocument = correlatedDocument;
    }

    public double getCorrelationValue() {
        return correlationValue;
    }

    public void setCorrelationValue(double correlationValue) {
        this.correlationValue = correlationValue;
    }

    public List<Map.Entry<Term, Term>> getCorrelatedTermPair() {
        return correlatedTermPair;
    }

    public void setCorrelatedTermPair(List<Map.Entry<Term, Term>> correlatedTermPair) {
        this.correlatedTermPair = correlatedTermPair;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SimilarityEntry that = (SimilarityEntry) o;
        return Double.compare(that.correlationValue, correlationValue) == 0 && Objects.equals(correlatedDocument, that.correlatedDocument) && Objects.equals(correlatedTermPair, that.correlatedTermPair);
    }

    @Override
    public int hashCode() {
        return Objects.hash(correlatedDocument, correlationValue, correlatedTermPair);
    }
}