import java.util.*;

/**
 *
 */
public class Document {
    //The document unique ID as in the original Excel table
    String docName = null; //Unique ID
    //The list of preprocessed terms (see Excel file for details) and their weights, for this specific term
    List<Map.Entry<Term, Integer>> preprocessTermVector = null;
    //The list of direct correlations with other docs (equality or containment of terms)
    List<SimilarityEntry> directCorrelations = null;
    //The list of masked correlations with other docs (synonym, is-a or stem relation for terms)
    List<SimilarityEntry> maskedCorrelations = null;
    //The list of pathways linking two documents via "common" terms (ergo correlated), where direct and masked correlation == 0
    //  Searched doc |  Intermediate doc | Linking term
    HashMap<String, List<HashMap<String, String>>> correlationPathways = null;

    public Document() {
    }

    public Document(String docName, List<Map.Entry<Term, Integer>> preprocessTerms, List<SimilarityEntry> directCorrelations, List<SimilarityEntry> maskedCorrelations, HashMap<String, List<HashMap<String, String>>> correlationPathways) {
        this.docName = docName;
        this.preprocessTermVector = preprocessTerms;
        this.directCorrelations = directCorrelations;
        this.maskedCorrelations = maskedCorrelations;
        this.correlationPathways = correlationPathways;
    }

    public String getDocName() {
        return docName;
    }

    public void setDocName(String docName) {
        this.docName = docName;
    }

    public List<Map.Entry<Term, Integer>> getPreprocessTerms() {
        return preprocessTermVector;
    }

    public void setPreprocessTerms(List<Map.Entry<Term, Integer>> preprocessTerms) {
        this.preprocessTermVector = preprocessTerms;
    }

    public List<SimilarityEntry> getDirectCorrelations() {
        return directCorrelations;
    }

    public void setDirectCorrelations(List<SimilarityEntry> directCorrelations) {
        this.directCorrelations = directCorrelations;
    }

    public List<SimilarityEntry> getMaskedCorrelations() {
        return maskedCorrelations;
    }

    public void setMaskedCorrelations(List<SimilarityEntry> maskedCorrelations) {
        this.maskedCorrelations = maskedCorrelations;
    }

    public HashMap<String, List<HashMap<String, String>>> getCorrelationPathways() {
        return correlationPathways;
    }

    public void setCorrelationPathways(HashMap<String, List<HashMap<String, String>>> correlationPathways) {
        this.correlationPathways = correlationPathways;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Document document = (Document) o;
        return Objects.equals(docName, document.docName) && Objects.equals(preprocessTermVector, document.preprocessTermVector) && Objects.equals(directCorrelations, document.directCorrelations) && Objects.equals(maskedCorrelations, document.maskedCorrelations) && Objects.equals(correlationPathways, document.correlationPathways);
    }

    @Override
    public int hashCode() {
        return Objects.hash(docName, preprocessTermVector, directCorrelations, maskedCorrelations, correlationPathways);
    }
}



