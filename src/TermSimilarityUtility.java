import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TermSimilarityUtility {

    public static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("0.00");
    public static final DecimalFormat DECIMAL_FORMAT_GRAPH = new DecimalFormat("0");

    public static final Map<String, Integer> DOCUMENT_GROUP_IDS = Stream.of(new Object[][] {
            { "SB", 0 },
            { "SP", 1 },
            { "CPPS", 2 },
            { "ICT", 3 },
            { "CE", 4 },
            { "UN5P", 5 },
            { "UNSDG", 6 },
    }).collect(Collectors.toMap(data -> (String) data[0], data -> (Integer) data[1]));

    static final String[] TOO_GENERAL_WORDS = {"term", "publication", "area", "year"};

    public static final List<String> TOO_GENERAL_WORD_LIST = new ArrayList<>(List.of(TOO_GENERAL_WORDS));

    // function to sort hashmap by values
    public static HashMap<Term, Integer> SortByValueAscending(HashMap<Term, Integer> hm)
    {
        // Create a list from elements of HashMap
        List<Map.Entry<Term, Integer> > list =
                new LinkedList<Map.Entry<Term, Integer> >(hm.entrySet());

        // Sort the list
        Collections.sort(list, new Comparator<Map.Entry<Term, Integer> >() {
            public int compare(Map.Entry<Term, Integer> o1,
                               Map.Entry<Term, Integer> o2)
            {
                return (o1.getValue()).compareTo(o2.getValue());
            }
        });

        // put data from sorted list to hashmap
        HashMap<Term, Integer> temp = new LinkedHashMap<Term, Integer>();
        for (Map.Entry<Term, Integer> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }
        return temp;
    }

    public static HashMap<Term, Integer> SortByValueDescending(HashMap<Term, Integer> hm)
    {
        // Create a list from elements of HashMap
        List<Map.Entry<Term, Integer> > list =
                new LinkedList<Map.Entry<Term, Integer> >(hm.entrySet());

        // Sort the list
        Collections.sort(list, new Comparator<Map.Entry<Term, Integer> >() {
            public int compare(Map.Entry<Term, Integer> o1,
                               Map.Entry<Term, Integer> o2)
            {
                return (o2.getValue()).compareTo(o1.getValue());
            }
        });

        // put data from sorted list to hashmap
        HashMap<Term, Integer> temp = new LinkedHashMap<Term, Integer>();
        for (Map.Entry<Term, Integer> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }
        return temp;
    }

    public static HashMap<Term, Integer> SortByKeyAscending(HashMap<Term, Integer> hm)
    {
        // Create a list from elements of HashMap
        List<Map.Entry<Term, Integer> > list =
                new LinkedList<Map.Entry<Term, Integer> >(hm.entrySet());

        // Sort the list
        Collections.sort(list, new Comparator<Map.Entry<Term, Integer> >() {
            public int compare(Map.Entry<Term, Integer> o1,
                               Map.Entry<Term, Integer> o2)
            {
                return (o1.getKey().getOriginal()).compareTo(o2.getKey().getOriginal());
            }
        });

        // put data from sorted list to hashmap
        HashMap<Term, Integer> temp = new LinkedHashMap<Term, Integer>();
        for (Map.Entry<Term, Integer> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }
        return temp;
    }

    public static HashMap<Term, Integer> SortByKeyDescending(HashMap<Term, Integer> hm)
    {
        // Create a list from elements of HashMap
        List<Map.Entry<Term, Integer> > list =
                new LinkedList<Map.Entry<Term, Integer> >(hm.entrySet());

        // Sort the list
        Collections.sort(list, new Comparator<Map.Entry<Term, Integer> >() {
            public int compare(Map.Entry<Term, Integer> o1,
                               Map.Entry<Term, Integer> o2)
            {
                return (o2.getKey().getOriginal()).compareTo(o1.getKey().getOriginal());
            }
        });

        // put data from sorted list to hashmap
        HashMap<Term, Integer> temp = new LinkedHashMap<Term, Integer>();
        for (Map.Entry<Term, Integer> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }
        return temp;
    }

    public static HashMap<Term, Integer> ToHashMap (String docRowText, String docRowTextStemmed, String SPLIT_CHAR)
    {
        //ATTENTION: we assume the SAME ORDER of terms in the original and stemmed vector
        String[] splittedDoc = docRowText.split(SPLIT_CHAR);
        String[] splittedDocStemmed = docRowText.split(SPLIT_CHAR);

        //System.out.println("String[] splittedDoc-splittedDocStemmed dimensions: "+splittedDoc.length+"-"+splittedDocStemmed.length);

        HashMap<Term, Integer> term_vector_hmap = new HashMap<Term, Integer>();
        //The sequence in the row texts is: key1"space"value1"space"key2"space"value2.....
        for (int i = 0; i< splittedDoc.length; i=i+2) {
            //New term: the first parameter is the original form of term, the second the stemmed one
            Term theTerm = new Term(
                    splittedDoc[i].replace("(","").replace(")",""),
                    splittedDocStemmed[i].replace("(","").replace(")",""),
                    Integer.valueOf(splittedDoc[i+1]));
            term_vector_hmap.put(theTerm, Integer.valueOf(splittedDoc[i+1]));
        }

        //Completing until 100 terms
        if ((splittedDoc.length == splittedDocStemmed.length) && splittedDoc.length<200) { //need to complete the vector up to 100 terms
            int missingTerms = (200 - splittedDoc.length)/2;
            //System.out.println ("Missing terms: "+missingTerms);
            for (int i=0; i<missingTerms; i++) {
                Term theTerm = new Term("z"+i+"_lemma", "z"+i+"_stem",0);
                term_vector_hmap.put(theTerm, 0);
            }
        }

        return term_vector_hmap;
    }

    public static HashMap<String, Integer> MergeHashMaps (HashMap<String, Integer> hm1, HashMap<String, Integer> hm2, int maxNumOfTerms)
    {
        System.out.println("...Merging Hash Maps up to ("+maxNumOfTerms+") elements");

        int counter =0;
        HashMap<String, Integer> orderedHM = new HashMap<String, Integer>();
        Map.Entry<String, Integer> entry2Enter = null;

        for (Map.Entry<String, Integer> entry1 : hm1.entrySet()) {
            for (Map.Entry<String, Integer> entry2 : hm2.entrySet()) {
                if (counter<maxNumOfTerms) {
                    //chose the item with higher value between the two lists
                    if (entry1.getValue()>=entry2.getValue()) {
                        entry2Enter = entry1;
                    } else {
                        entry2Enter = entry2;
                    }
                    //check whether the item already exists
                    int value2Enter = -1;
                    if (orderedHM.containsKey(entry2Enter.getKey())) {
                        //if it exists, then replace the value with max between them
                        value2Enter = Math.max(value2Enter, orderedHM.get(entry2Enter.getKey()));
                        orderedHM.replace(entry2Enter.getKey(), value2Enter);
                        //System.out.println(">>> Updating: "+entry2Enter.getKey()+ " with value:"+value2Enter);
                    } else {
                        //otherwise insert it
                        value2Enter = entry2Enter.getValue();
                        orderedHM.put(entry2Enter.getKey(), value2Enter);
                        //advance hash map position counter by 1
                        counter++;
                        //System.out.println("*** Inserting: "+entry2Enter.getKey()+ " with value:"+value2Enter);
                    }
                }
            }
        }
        return orderedHM;
    }

    public static String ReadURLContent (String theURL, String term) {
        StringBuilder content = new StringBuilder();
        // Use try and catch to avoid the exceptions
        try
        {
            URL url = new URL(theURL + term); // creating a url object
            URLConnection urlConnection = url.openConnection(); // creating a urlconnection object

            // wrapping the urlconnection in a bufferedreader
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line;
            // reading from the urlconnection using the bufferedreader
            while ((line = bufferedReader.readLine()) != null)
            {
                content.append(line + "\n");
            }
            bufferedReader.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return content.toString();
    }

    public static List<Map.Entry<String, HashMap<Term, Integer>>> ScaleDocsToMaxWeight(List<Map.Entry<String, HashMap<Term, Integer>>> lhmDocList2Scale) {
        int localMaxWeight = -1;
        int localMaxIndex = -1;
        List<Integer> lDocWeights = new ArrayList<>();
        //Iterating on all the doc rows
        for (int index=0; index < lhmDocList2Scale.size(); index++) {
            int tempWeight = -1;
            //Retrieve the i-th doc
            Map.Entry<String, HashMap<Term, Integer>> aDoc = lhmDocList2Scale.get(index);
            String docName = aDoc.getKey();
            HashMap<Term, Integer> docTermVector = aDoc.getValue();
            //System.out.println ("--------------------------------------------------------");
            //System.out.println ("docName: "+docName+" | Terms: "+docTermVector.toString());
            for (Map.Entry<Term, Integer> keyWord : docTermVector.entrySet()) {
                tempWeight += keyWord.getValue();
            }
            lDocWeights.add(index, tempWeight);
            //System.out.println ("doc weight: "+tempWeight);
            //store the new max weight and the position of the doc in the array
            if (tempWeight > localMaxWeight) {
                localMaxWeight = tempWeight;
                localMaxIndex = index;
                //System.out.println ("Resetting localMaxWeight to: "+localMaxWeight+" index("+localMaxIndex+")");
            }
        }
        /*
        Given:
        a) TW(i,j) = TermWeight(i, j) = weight of the i-th term in j-th document. i =1..NumOfDocs, and j=1…Dim(terms-space) -> dimension of the union of all terms in all documents
        b) DW(j) = DocWeight(j) = Sum[TW(i,j)], for a given document j (i,j from previous point)
        b) HD = Heaviest Document = Max {Sum[W(i,j)]}, for a given i, and j=1…dimension of the union of all terms in all documents
        c) TSR(j) = Term Scaling Rate(j) = HD / DW(j)
        c) STW(i,j) = ScaledTermWeight(i,j) = TW(i,j) * TSR(j)
         */
        //Now rescaling to the max weighted doc
        for (int index=0; index < lhmDocList2Scale.size(); index++) {
            //We do not want to rescale the max weighted doc to itself
            if (index != localMaxIndex) {
                //Retrieve the i-th doc
                Map.Entry<String, HashMap<Term, Integer>> theDoc2Scale = lhmDocList2Scale.get(index);
                String docName = theDoc2Scale.getKey();
                HashMap<Term, Integer> docTermVector2Scale = theDoc2Scale.getValue();
                Double docLocalWeight = Double.valueOf(lDocWeights.get(index));
                //System.out.println ("Rescaling docName: "+docName+" (L/M): "+docLocalWeight+"/"+localMaxWeight);
                for (Map.Entry<Term, Integer> keyWord2Scale : docTermVector2Scale.entrySet()) {
                    double scaledTermWeight;
                    if (keyWord2Scale.getValue()>0) {
                        scaledTermWeight = Double.valueOf(keyWord2Scale.getValue()) * ((double) localMaxWeight / docLocalWeight);
                        //We want to make sure to round the double to the next higher integer, always
                        scaledTermWeight += 0.5;
                    } else {
                        scaledTermWeight=0.0;
                    }
                    //System.out.println("Scaled (O/S) o: "+keyWord2Scale.getValue()+"/"+scaledTermWeight);
                    //System.out.println("Scaled (O/S) s: "+keyWord2Scale.getValue()+"/"+(int)Math.round(scaledTermWeight));
                    keyWord2Scale.setValue((int)Math.round(scaledTermWeight));
                }
            }
        }
        return lhmDocList2Scale;
    }

    public static String toExcelString (Map.Entry<String, HashMap<String, Integer>> docToString, boolean forCorrelation) {
        StringBuilder theString = new StringBuilder();
        final String BLANK_SPACE = " ";
        theString.append("(").append(docToString.getValue().entrySet().size()).append(")").append(BLANK_SPACE).append(docToString.getKey()).append(":");
        for (Map.Entry<String, Integer> aDoc : docToString.getValue().entrySet()) {
            theString.append(BLANK_SPACE).append(aDoc.getKey()).append(BLANK_SPACE).append(aDoc.getValue());
        }
        if (forCorrelation) {
            theString.append(";");
        }
        return theString.toString();
    }

    //sim(x,y)=x⋅y||x||||y||
    public static SimilarityEntry calculateCosineSimilarity (Map.Entry<String, List<Map.Entry<Term, Integer>>> vector1, Map.Entry<String, List<Map.Entry<Term, Integer>>> vector2) {

        String docID = vector1.getKey();
        String withDocID = vector2.getKey();
        List<Map.Entry<Term, Integer>> termVector1 = vector1.getValue();
        List<Map.Entry<Term, Integer>> termVector2 = vector2.getValue();

        double geoMeanVector1 = CalculateVectorEuclideanNorm(termVector1);
        double geoMeanVector2 = CalculateVectorEuclideanNorm(termVector2);
        double cosineSimilarity = -1;
        double scalarProduct = 0;

        SimilarityEntry similEntry = new SimilarityEntry();
        similEntry.setCorrelatedDocument(withDocID);

        String termPairs = "";
        List<Map.Entry<Term,Term>> correlatedTermPairs = new ArrayList<>();

        if (termVector1.size()==termVector2.size()) {
            //For all the terms contained in term-vector one
            for (Map.Entry<Term, Integer> firstTermEntry : termVector1) {
                String keyvec1 = firstTermEntry.getKey().getStemmed().trim().toLowerCase();
                //We iterate on all the terms contained in term vector two
                for (Map.Entry<Term, Integer> secondTermEntry : termVector2) {
                    String keyvec2 = secondTermEntry.getKey().getStemmed().trim().toLowerCase();
                    //Equality on stemmed form for the two terms in each other
                    if ( keyvec1.equals(keyvec2)
                            && (!TOO_GENERAL_WORD_LIST.contains(keyvec1))
                            && (!TOO_GENERAL_WORD_LIST.contains(keyvec2)) ) {
                        scalarProduct += (firstTermEntry.getValue() * secondTermEntry.getValue());

                        //Adding the new matching pair to the correlation list
                        correlatedTermPairs.add(Map.entry(firstTermEntry.getKey(), secondTermEntry.getKey()));
                    }
                }
            }
            //Cosine Similarity final value
            cosineSimilarity = scalarProduct / (geoMeanVector1 * geoMeanVector2);
            similEntry.setCorrelationValue(cosineSimilarity);
            similEntry.setCorrelatedTermPair(correlatedTermPairs);

            if (cosineSimilarity<=0.0) {
                //System.out.println("*********** FOUND Direct Similarity = ZERO ["+docID+":"+withDocID+"] ********************");
            }
        } else {
            //System.out.println("...Vectors dimension MISMATCH! "+docID+":"+termVector1.size()+" "+withDocID+":"+termVector2.size());
        }
        return similEntry;
    }

    public static double CalculateVectorEuclideanNorm (List<Map.Entry<Term, Integer>> preprocessTermVector) {
        double vectorGeoMean = 0;
        for (Map.Entry<Term, Integer> termEntry : preprocessTermVector) {
            vectorGeoMean += Math.pow(termEntry.getValue(), 2);
        }
        return Math.sqrt(vectorGeoMean);
    }

    public static void generateDirectCorrelationRowFile(DocumentBase docBase) {
        try (
                FileWriter fw = new FileWriter("direct-correlation-lists.txt", false);
                BufferedWriter bw = new BufferedWriter(fw);
                PrintWriter out = new PrintWriter(bw)) {
            //Let's check on the obtained calculations
            for (Document theDoc : docBase.getDocumentList()) {
                out.println("--------------------------------------------");
                out.println(theDoc.getDocName());
                out.println("--------------------------------------------");
                for (SimilarityEntry theSimilEntry : theDoc.getDirectCorrelations()) {
                    //out.println(theDoc.getDocName()+"|"+theSimilEntry.getCorrelatedDocument()+": "+ TermSimilarityUtility.DECIMAL_FORMAT.format(theSimilEntry.getCorrelationValue()));
                    //out.println(TermSimilarityUtility.DECIMAL_FORMAT.format(theSimilEntry.getCorrelationValue()));
                    out.print(TermSimilarityUtility.DECIMAL_FORMAT.format(theSimilEntry.getCorrelationValue())+";");
                }
            }
        } catch (IOException e) {
            //exception handling left as an exercise for the reader
        }
    }

    public static void generateForceGraphJSON(DocumentBase docBase) {
        StringBuilder jsonGraphStringBuilder = new StringBuilder();
        jsonGraphStringBuilder.append(
        "{\n"+
            "\"nodes\": [\n");

        //Adding initial doc groups to force directed graph
        for (Document theDoc : docBase.getDocumentList()) {
            int identifiedGroupID = -1;
            for (Map.Entry<String, Integer> docGroupEntry : DOCUMENT_GROUP_IDS.entrySet()) {
                if (theDoc.getDocName().trim().toLowerCase().contains(docGroupEntry.getKey().trim().toLowerCase()+"_")) {
                    identifiedGroupID = docGroupEntry.getValue();
                }
            }
            jsonGraphStringBuilder.append("{\"id\": \""+theDoc.getDocName()+"\", \"group\": "+identifiedGroupID+"},\n");
        }

        //Last item must NOT be followed by ',' to be a syntactically correct JSON
        jsonGraphStringBuilder.delete(jsonGraphStringBuilder.length()-2, jsonGraphStringBuilder.length());

        //Adding all the correlations between documents in the graph
        jsonGraphStringBuilder.append("\n],\n"+
            "\"links\": [\n");

        for (Document theDoc : docBase.getDocumentList()) {
            for (SimilarityEntry theSimilEntry : theDoc.getDirectCorrelations()) {
                jsonGraphStringBuilder.append("{\"source\": \""+theDoc.getDocName()
                        +"\", \"target\": \""+theSimilEntry.getCorrelatedDocument()
                        +"\", \"value\": "+TermSimilarityUtility.DECIMAL_FORMAT.format(theSimilEntry.getCorrelationValue())+"},\n");
            }
        }
        //Last item must NOT be followed by ',' to be a syntactically correct JSON
        jsonGraphStringBuilder.delete(jsonGraphStringBuilder.length()-2, jsonGraphStringBuilder.length());

        //Closing the json String
        jsonGraphStringBuilder.append("]\n" +
                "}");

        String finalJsonText = jsonGraphStringBuilder.toString().replace("0,","0.");

        try {
            FileWriter fw = new FileWriter("direct-correlation-graph.json", false);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw);

            //Let's check on the obtained calculations
            //System.out.println(finalJsonText);
            out.println(finalJsonText);
            
            //Closing the stream
            bw.close();
            out.close();
        } catch (IOException e) {
            //exception handling left as an exercise for the reader
        }
    }

    public static void calulateMostFrequentTerms(DocumentBase docBase) {
        HashMap<String, Integer> commonTermsFrequency = new HashMap<String, Integer>();
        for (Document theDoc: docBase.getDocumentList()) {
            for (SimilarityEntry theSimilEntry: theDoc.getDirectCorrelations()) {
                if (theSimilEntry.getCorrelatedTermPair()!=null) {
                    for (Map.Entry<Term, Term> theTermPair: theSimilEntry.getCorrelatedTermPair()) {
                        //No entry in the HashMap with the first term
                        if (commonTermsFrequency.get(theTermPair.getKey().getOriginal())==null) {
                            commonTermsFrequency.put(theTermPair.getKey().getOriginal(), 1);
                        } else { //Increment by one the first term frequency
                            commonTermsFrequency.replace(theTermPair.getKey().getOriginal(), commonTermsFrequency.get(theTermPair.getKey().getOriginal())+1);
                        }
                    }
                }
            }
        }
        System.out.println("Term : Frequency");
        for (Map.Entry<String, Integer> theFrequentTerm: commonTermsFrequency.entrySet()) {
            System.out.println(theFrequentTerm.getKey()+" : "+theFrequentTerm.getValue());
        }
    }

    //We can tune statistics on a specific domain or on all of them (null)
    public static void calulateTermStatistics (DocumentBase docBase, String specificDomain) {
        HashMap<String, TermCorrelationStatistics> termsStatistics = new HashMap<String, TermCorrelationStatistics>();
        for (Document theDoc: docBase.getDocumentList()) {
            boolean sameDoc = false; //A term weight is considered only once within all the correlations of the same doc
            for (SimilarityEntry theSimilEntry: theDoc.getDirectCorrelations()) {
                if (theSimilEntry.getCorrelatedTermPair()!= null) {
                    for (Map.Entry<Term, Term> theTermPair: theSimilEntry.getCorrelatedTermPair()) {
                        //System.out.println("specificDomain:"+specificDomain+" | "+
                        //        "theDoc: "+theDoc.getDocName().toLowerCase().trim()+" | "+
                        //        "theSimilEntry doc: "+theSimilEntry.getCorrelatedDocument().toLowerCase().trim());
                        if (specificDomain==null ||
                                theDoc.getDocName().toLowerCase().trim().contains(specificDomain) ||
                                theSimilEntry.getCorrelatedDocument().toLowerCase().trim().contains(specificDomain.trim().toLowerCase())
                        ) {
                            //No entry in the HashMap with the first term
                            if (termsStatistics.get(theTermPair.getKey().getOriginal())==null) {
                                termsStatistics.put(theTermPair.getKey().getOriginal(),
                                        new TermCorrelationStatistics(1,     //First occurrence of the term
                                                theTermPair.getKey().getWeight()+       //First term weight
                                                        theTermPair.getValue().getWeight()));   //Second term weight
                                sameDoc = true;
//                        System.out.println(">>>>>>"+theDoc.getDocName()+":: setting 'sameDoc=true' (1) for term:"+ theTermPair.getKey().getOriginal());
                            } else { //Increment by one the term frequency and update its correlation weights
                                TermCorrelationStatistics updateStatistics = new TermCorrelationStatistics();
                                Integer weightIncrement = termsStatistics.get(theTermPair.getKey().getOriginal())
                                        .getWeightInCorrelations()+             //The current weight
                                        theTermPair.getValue().getWeight();    //Plus the second term weight in correlations
                                if (!sameDoc) {
                                    //The first occurrence for the term in a new document
                                    weightIncrement += theTermPair.getKey().getWeight();
                                    sameDoc = true;
//                            System.out.println(">>>>>>"+theDoc.getDocName()+":: setting 'sameDoc=true' (2) for term:"+ theTermPair.getKey().getOriginal());
                                }
                                updateStatistics.setNumOfCorrelations(termsStatistics.get(theTermPair.getKey().getOriginal()).getNumOfCorrelations()+1);
                                updateStatistics.setWeightInCorrelations(weightIncrement);

                                termsStatistics.replace(theTermPair.getKey().getOriginal(),updateStatistics);
                            }
                        }
                    }
                }
            }
        }
        System.out.println();
        System.out.println("------------------------------------------------------------------");
        System.out.println(specificDomain==null?"ALL domains...":specificDomain+" domain...");
        System.out.println("Term : DC Occurrences : Total Weight");
        for (Map.Entry<String, TermCorrelationStatistics> theFrequentTerm: termsStatistics.entrySet()) {
            System.out.println(theFrequentTerm.getKey()+
                    " : "+theFrequentTerm.getValue().getNumOfCorrelations()+
                    " : "+theFrequentTerm.getValue().getWeightInCorrelations());
        }
    }
}
