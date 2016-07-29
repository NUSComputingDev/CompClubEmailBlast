import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
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

    
    private HtmlGenerator() {
    }

    public static HtmlGenerator getInstance() {
        if (theHtmlGenerator == null) {
            theHtmlGenerator = new HtmlGenerator();
        }
        return theHtmlGenerator;
    }

    private String getDate() {
        DateFormat dateFormat = new SimpleDateFormat("dd MMMM YYYY");
        Date date = new Date();
        return dateFormat.format(date);
    }

    public String generateContent(String path) throws IOException {
        String html = "";

        bufferedInput =
                new BufferedReader(new InputStreamReader(new FileInputStream(
                        path + "main.txt")));
        String line;
        char index = '1';
        while ((line = bufferedInput.readLine()) != null) {
            if (line.charAt(0) == index) {
                if (index != '1') {
                    html += HtmlConstants.CONTENT_END;
                }
                html += HtmlConstants.CONTENT_START;
                index++;
            } else if (line.contains("title: ")) {
                html +=
                        HtmlConstants.CONTENT_TITLE.replace("title",
                                line.substring(7, line.length()));
                if ((new File(path + "img" + (index - '1') + ".png")).exists()) {
                    html +=
                            HtmlConstants.CONTENT_IMG.replace("image", "img"
                                    + (index - '1'));
                }
                if ((new File(path + "txt" + (index - '1') + ".html")).exists()) {
                    html += "txt" + (index - '1');
                }
            } else {
                String linkText = bufferedInput.readLine();
                html +=
                        HtmlConstants.CONTENT_LINK.replace("link",
                                line.substring(6, line.length())).replace(
                                "lnk-txt",
                                linkText.substring(11, linkText.length()));
            }
        }
        html += HtmlConstants.CONTENT_END;
        bufferedInput.close();

        for (int i = 1; i < (index - '0'); i++) {
            System.out.println("txt" + i);
            if (html.contains("txt" + i)) {
                String text = "";
                bufferedInput =
                        new BufferedReader(
                                new InputStreamReader(new FileInputStream(path
                                        + "txt" + i + ".html")));
                while ((line = bufferedInput.readLine()) != null) {
                    text += "                    " + line + "\n";
                }
                bufferedInput.close();
                html = html.replace("txt" + i, text);
            }
        }

        return html;
    }

    public String generateHtml(String path) throws IOException {
        String html = "";
        bufferedOutput =
                new BufferedWriter(new OutputStreamWriter(new FileOutputStream(
                        path + "out.html")));

        html += HtmlConstants.OPENING;
        html += getDate();
        html += HtmlConstants.AFTER_DATE;
        html += generateContent(path);
        html += HtmlConstants.ENDING;
        bufferedOutput.write(html);
        bufferedOutput.close();
        return html;
    }

    }

    public static void main(String[] args) {
        HtmlGenerator htmlGenerator = new HtmlGenerator();
        try {
            String generatedHTML = htmlGenerator.generateHtml("sample/");
            // System.out.println(generatedHTML);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}
