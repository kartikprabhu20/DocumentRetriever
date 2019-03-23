import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.search.similarities.ClassicSimilarity;
import org.apache.lucene.search.similarities.Similarity;
import org.apache.lucene.store.Directory;

import java.io.*;

/**
 * @author kartikprabhu  14/12/18 6:14 PM
 */

/**
 * Class used for indexing the files in the directory
 */
public class Indexer {

    /**
     * Constants used to save the data while indexing or retrieving the scores.
     */
    public static final String FILE_PATH = "file_path";
    public static final String CONTENTS = "contents";
    public static final String TITLE = "title";

    /**
     * Ranking models considered in this program.
     */
    public static final String VECTOR_SPACE_MODEL = "VS";
    public static final String OKAPI_MODEL = "OK";

    private Analyzer analyzer;
    private Directory indexDirectory;
    private IndexWriter indexWriter;

    private static Indexer indexer;

    public static Indexer getIndexer(Directory indexDirectoryPath, String indexingModel) {
        //this is singleton so that multiple instances don't get created (IndexWriter must be created just once)
        if (indexer != null){
            return indexer;
        }

        try {
            return new Indexer(indexDirectoryPath, indexingModel);
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * Constructor to get the indexer.
     * @param indexDirectoryPath
     * @param indexingModel
     * @throws IOException
     */
    public Indexer(Directory indexDirectoryPath, String indexingModel) throws IOException {
        this.indexDirectory = indexDirectoryPath;
        this.analyzer = new CustomisedAnalyser();

        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);
        indexWriterConfig.setSimilarity(getSimilarity(indexingModel));

        indexWriterConfig.setOpenMode(IndexWriterConfig.OpenMode.CREATE);

        this.indexWriter = new IndexWriter(indexDirectory, indexWriterConfig);
    }

    /**
     * Get the similarity which has to be used depending on User's selection
     * @param indexingModel
     * @return @link{ClassicSimilarity.class} if indexingModel is "VS" else @link{BM25Similarity.class} for "OK" indexModel
     */
    private Similarity getSimilarity(String indexingModel) {
        return VECTOR_SPACE_MODEL.equalsIgnoreCase(indexingModel)? new ClassicSimilarity() : new BM25Similarity();
    }

    /**
     * Method used to create a document corresponding to lucene.
     * @param filePath
     * @return
     * @throws IOException
     */
    public Document createDocument(String filePath) throws IOException {
        Document document = new Document();

        FieldType fieldType = new FieldType();
        fieldType.setTokenized(true);
        fieldType.setStored(true);

        document.add(new TextField(CONTENTS, new FileReader(new File(filePath))));
        document.add(new Field(FILE_PATH, filePath, fieldType));

        if (DocumentParser.isHtmlExtension(filePath)){
            document.add(new Field(TITLE, HtmlHelper.getHtmlTitle(new File(filePath)), fieldType));
        }

        return document;
    }


    /**
     * Method used to index the created document.
     * @param document The document to be indexed.
     * @throws IOException
     */
    public void indexDocument( Document document) throws IOException {
        indexWriter.addDocument(document);
    }

    public void close() throws IOException {
        indexWriter.close();
    }

    public void commit() throws IOException {
        indexWriter.commit();
    }

    public int getNumberOfDocsIndexed(){
        return indexWriter.numDocs();
    }

}
