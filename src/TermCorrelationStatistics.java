import java.util.Objects;

public class TermCorrelationStatistics {
    Integer numOfCorrelations;
    Integer weightInCorrelations;

    public TermCorrelationStatistics(Integer numOfCorrelations, Integer weightInCorrelations) {
        this.numOfCorrelations = numOfCorrelations;
        this.weightInCorrelations = weightInCorrelations;
    }

    public TermCorrelationStatistics() {
    }

    public Integer getNumOfCorrelations() {
        return numOfCorrelations;
    }

    public void setNumOfCorrelations(Integer numOfCorrelations) {
        this.numOfCorrelations = numOfCorrelations;
    }

    public Integer getWeightInCorrelations() {
        return weightInCorrelations;
    }

    public void setWeightInCorrelations(Integer weightInCorrelations) {
        this.weightInCorrelations = weightInCorrelations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TermCorrelationStatistics that = (TermCorrelationStatistics) o;
        return Objects.equals(numOfCorrelations, that.numOfCorrelations) && Objects.equals(weightInCorrelations, that.weightInCorrelations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numOfCorrelations, weightInCorrelations);
    }
}
