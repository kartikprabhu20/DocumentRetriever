import org.apache.lucene.document.Document;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

/**
 * @author kartikprabhu  27/11/18 3:22 PM
 */

public class Executor {

    public static void main(String args[]) {

        /**
         * Path of the directory to be indexed
         */
        String documentPath = args[0];
//        String documentPath = "/Users/kartikprabhu/Desktop/input/";


        /**
         * Path of the directory where indecies have to be stored
         */
        String indexFoldePath = args[1];
//        String indexFoldePath = "/Users/kartikprabhu/Desktop/indexFolder/";


        /**
         * Indexing models can be either of the below two.
         * {@link Indexer.VECTOR_SPACE_MODEL}
         * {@link Indexer.OKAPI_MODEL}
         */
        String indexingModel = args[2];
//        String indexingModel = Indexer.VECTOR_SPACE_MODEL;

        /**
         * Query string
         */
        String query = args[3];
//        String query = "title";

        /**
         * Top 10 documents to be shown
         */
        int maxHits = 10;

        try {

            //            System.out.println("Indexing...");
            Directory indexDirectory = FSDirectory.open(Paths.get(indexFoldePath));

            DocumentParser documentParser = new DocumentParser(documentPath);
            Indexer indexer = Indexer.getIndexer(indexDirectory, indexingModel);

            //Get all the files with the extensions .txt .html and .htm and index it with the help of Indexer
            System.out.println("*** LIST OF PARSED INPUT FILES ***");
            for (File file : documentParser.parseDocument()) {
                System.out.println("File:"+ file.getName()+ " path:"+ file.getAbsolutePath());
                indexer.indexDocument(indexer.createDocument(file.getCanonicalPath()));
            }

            indexer.commit();
            indexer.close();

            //Searcher will get the top 10 documents depending on the query.
            Searcher searcher = new Searcher(indexDirectory);
            QueryParser parser = new QueryParser("contents",new CustomisedAnalyser());

            TopDocs topDocs = searcher.search(parser.parse(query),maxHits);
            ScoreDoc[] topScoreDocs = topDocs.scoreDocs;

            System.out.println("\n *** RANKING ***");
            for (ScoreDoc hit: topScoreDocs){
                int docId = hit.doc;
                Document document = searcher.getDocument(docId);

                String filePath = document.get(Indexer.FILE_PATH);
                String fileName = new File(filePath).getName();
                String title = DocumentParser.isHtmlExtension(filePath)? HtmlHelper.getHtmlTitle(new File(filePath)) : "";
                System.out.println("Search: SCORE : " + hit.score + " FILE_NAME : "+ fileName + (!title.isEmpty()  ? " TITLE : " + title : "") + " PATH : " + filePath);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


}


