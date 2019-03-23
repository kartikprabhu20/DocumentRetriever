import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;

import java.io.IOException;

/**
 * @author kartikprabhu  14/12/18 7:33 PM
 */


/**
 * Class that is used to search a query based on the index file which is already indexed.
 */
public class Searcher {
    private Directory indexDirectory;
    private IndexSearcher indexSearcher;

    public Searcher(Directory indexDirectoryPath) throws IOException {
        this.indexDirectory = indexDirectoryPath;
        IndexReader indexReader = DirectoryReader.open(indexDirectory);
        indexSearcher = new IndexSearcher(indexReader);
    }

    /**
     * Method returns top docs in indexed file
     * @param query the query which needs to be indexed
     * @param maxHits Maximum number of documents which needs to be retrieved.
     * @return top @link{maxHits} documents.
     * @throws IOException
     */
    public TopDocs search(Query query, int maxHits) throws IOException {
        return indexSearcher.search(query,maxHits);
    }

    /**
     * Method to get the document of given docId
     * @param docId ID for which document is retrieved.
     * @return document with matching ID.
     * @throws IOException
     */
    public Document getDocument(int docId) throws IOException {
        return indexSearcher.doc(docId);
    }
}
