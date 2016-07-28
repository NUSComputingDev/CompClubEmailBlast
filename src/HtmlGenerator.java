import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * <p>
 * class HtmlGenerator generates the complete html file using the Email blast
 * template and based on the files in the folder.
 * </p>
 */
public class HtmlGenerator {

    private static HtmlGenerator theHtmlGenerator;

    private static BufferedWriter bufferedOutput;
    private static BufferedReader bufferedInput;

    public HtmlGenerator HtmlGenerator() {
        if (theHtmlGenerator == null) {
            theHtmlGenerator = new HtmlGenerator();
        }
        return theHtmlGenerator;
    }

    public String generateHtml(String path) throws IOException {
        String html = "";
        bufferedOutput =
                new BufferedWriter(new OutputStreamWriter(new FileOutputStream(
                        path + "out.html")));

        html += HtmlConstants.OPENING;
        html += getDate();
        html += HtmlConstants.AFTER_DATE;
        // the body here
        html += HtmlConstants.ENDING;
        bufferedOutput.write(html);
        bufferedOutput.close();
        return html;
    }

    private String getDate() {
        DateFormat dateFormat = new SimpleDateFormat("dd MMMM YYYY");
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static void main(String[] args) {
        HtmlGenerator htmlGenerator = new HtmlGenerator();
        try {
            String generatedHTML = htmlGenerator.generateHtml("sample/");
            System.out.println(generatedHTML);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}
