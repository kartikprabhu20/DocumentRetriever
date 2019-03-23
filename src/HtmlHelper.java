import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.File;
import java.io.IOException;

/**
 * @author kartikprabhu  15/12/18 9:55 PM
 */


/**
 * Helper class to get the title of the html file using jsoup library.
 */
public class HtmlHelper {

    /**
     * Method used to get the title of the html file.
     * @param file html file
     * @return
     */
    public static  String getHtmlTitle(File file){
        try {
            Document doc = Jsoup.parse(file,"utf-8");
            String title = doc.title();
//            System.out.println("Html title is: " + title);
            return title;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
