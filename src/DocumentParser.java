import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author kartikprabhu  12/12/18 6:44 PM
 */

/**
 * Class to parse the documents
 */
public class DocumentParser {

    /**
     * Expected file extensions
     */
    private static final String TEXT_EXTENSION = ".txt";
    private static final String HTML_EXTENSION = ".html";
    private static final String HTM_EXTENSION = ".htm";

    private String documentPath;

    public DocumentParser(String documentPath) {
        this.documentPath = documentPath;
    }

    /**
     * Method that gives the file list in the given directory path.
     * @return file list
     */
    public List<File> parseDocument(){
        return  getFileList(documentPath);
    }

    /**
     * Method to get all the files in the specified path.
     * @param filePath directory path
     * @return list of files.
     */
    private List<File> getFileList(String filePath) {
        ArrayList<File> fileList = new ArrayList<>();
        File parentFile = new File(filePath);
        if (parentFile.isDirectory()){
            for (File file : parentFile.listFiles()){
                fileList.addAll(getFileList(file.getPath()));
            }
        }else if(hasRequiredExtenstion(filePath)){
            fileList.add(parentFile);
        }
        return fileList;
    }

    /**
     * Method to check if the file in the direcotry has the required extension
     * @param filePath path of the file
     * @return true if the extension is acceptable else false.
     */
    private boolean hasRequiredExtenstion(String filePath) {
        String extension = filePath.substring(filePath.lastIndexOf("."));
        return TEXT_EXTENSION.equalsIgnoreCase(extension) || isHtmlExtension(filePath);
    }

    /**
     * Method to check is the file is html/htm
     * @param filePath file path
     * @return true if the file is html or htm else return false.
     */
    public static boolean isHtmlExtension(String filePath) {
        String extension = filePath.substring(filePath.lastIndexOf("."));
        return  HTML_EXTENSION.equalsIgnoreCase(extension) || HTM_EXTENSION.equalsIgnoreCase(extension);
    }
}
