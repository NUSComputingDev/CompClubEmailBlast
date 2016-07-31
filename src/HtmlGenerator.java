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
 * template.
 * </p>
 */
public class HtmlGenerator {

    private static final String TITLE_PREFIX = "[Comp Club] ";
    private static final String MAIN_TXT = "main.txt";

    private static HtmlGenerator theHtmlGenerator;

    private BufferedWriter bufferedOutput;
    private BufferedReader bufferedInput;

    private String titles;

    private HtmlGenerator() {
    }

    /**
     * @return HtmlGenerator object (Singleton)
     */
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

    private String setContentEndAndStart(String html, char index) {
        if (index != '1') {
            // indicates that the previous item has ended, except when it's the
            // first item
            html += HtmlConstants.CONTENT_END;
        }
        html += HtmlConstants.CONTENT_START;
        return html;
    }

    private String setTitle(String html, String title, int index) {
        html += HtmlConstants.CONTENT_TITLE.replace("title", title);
        if (index != 1) {
            titles += " | ";
        }
        titles += title;

        return html;
    }

    private String setImage(String folderPath, String html, int index) {
        if ((new File(folderPath + "img" + index + ".png")).exists()) {
            html += HtmlConstants.CONTENT_IMG.replace("image", "img" + index);
        }
        return html;
    }

    private String setText(String folderPath, String html, int index) {
        if ((new File(folderPath + "txt" + index + ".html")).exists()) {
            html += "txt" + index;
        }
        return html;
    }

    private String setLink(String html, String link) throws IOException {
        String linkText = bufferedInput.readLine();
        html +=
                HtmlConstants.CONTENT_LINK.replace("link", link).replace(
                        "lnk-txt", linkText.substring(11, linkText.length()));
        return html;
    }

    private String updateTexts(String folderPath, String html, int index)
            throws IOException {
        String line;
        for (int i = 1; i < index; i++) {
            if (html.contains("txt" + i)) {
                String text = "";
                bufferedInput =
                        new BufferedReader(new InputStreamReader(
                                new FileInputStream(folderPath + "txt" + i
                                        + ".html")));
                while ((line = bufferedInput.readLine()) != null) {
                    text += "                    " + line + "\n";
                }
                bufferedInput.close();
                html = html.replace("txt" + i, text);
            }
        }
        return html;
    }

    private String generateContent(String folderPath) throws IOException {
        String html = "";
        titles = TITLE_PREFIX;

        bufferedInput =
                new BufferedReader(new InputStreamReader(new FileInputStream(
                        folderPath + MAIN_TXT)));
        String line;
        char index = '1';
        while ((line = bufferedInput.readLine()) != null) {
            if (line.charAt(0) == index) {
                html = setContentEndAndStart(html, index);
                index++;
            } else if (line.contains("title: ")) {
                html =
                        setTitle(html, line.substring(7, line.length()),
                                index - '1');
                html = setImage(folderPath, html, index - '1');
                html = setText(folderPath, html, index - '1');
            } else {
                html = setLink(html, line.substring(6, line.length()));
            }
        }
        html += HtmlConstants.CONTENT_END;
        bufferedInput.close();

        html = updateTexts(folderPath, html, index - '0');

        return html;
    }

    /**
     * generate the html file using the files available in the given folder<br>
     * The file "main.txt" is needed in the folder to indicate the no. of items<br>
     * The method then generates the html file based on the no. of individual
     * components of each item available
     * 
     * @param folderPath
     *            The path to the folder where the contents are
     * @return the html string representing the expected html file
     *
     * @throws IOException
     *             If the files are not readable
     */
    public String generateHtml(String folderPath, String outputPath,
            String outputName) throws IOException {
        String html = "";

        File files = new File(outputPath);
        files.mkdirs();

        bufferedOutput =
                new BufferedWriter(new OutputStreamWriter(new FileOutputStream(
                        outputPath + outputName)));

        html += HtmlConstants.OPENING;
        html += getDate();
        html += HtmlConstants.AFTER_DATE;
        html += generateContent(folderPath);
        html += HtmlConstants.ENDING;

        bufferedOutput.write(html);
        bufferedOutput.close();

        return html;
    }

    public String getTitles() {
        return titles;
    }

    public static void main(String[] args) {
        HtmlGenerator htmlGenerator = new HtmlGenerator();
        try {
            String generatedHTML =
                    htmlGenerator.generateHtml("sample/", "sampleOut/",
                            "out.html");
            System.out.println(generatedHTML);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}
