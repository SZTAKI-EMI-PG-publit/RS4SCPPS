import javax.print.Doc;
import java.util.ArrayList;
import java.util.List;

public class DocumentBase {

    // Static variable reference of single_instance
    private static DocumentBase single_instance = null;
    private static List<Document> documentList = null;

    private DocumentBase() {
        documentList = new ArrayList<>();
    }

    public static DocumentBase getInstance () {
        if (single_instance==null) {
            single_instance = new DocumentBase();
            documentList = new ArrayList<>();
        }
        return single_instance;
    }

    public Document getDocumentByID (String docID) {
        Document foundDoc = null;
        for (int index=0; index<documentList.size();index++) {
            if (documentList.get(index).getDocName().trim().toLowerCase().equals(docID.trim().toLowerCase())) {
                foundDoc = documentList.get(index);
            }
        }
        return foundDoc;
    }

    public void addDocument (Document doc2Add) {
        this.getDocumentList().add(doc2Add);
    }

    public List<Document> getDocumentList() {
        return this.documentList;
    }
}
