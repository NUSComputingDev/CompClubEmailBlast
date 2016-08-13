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

    private String setTitle(String html, String title, int index, 
            boolean isSponsor) {
        html += HtmlConstants.CONTENT_TITLE.replace("title", title);
        if (!isSponsor) {
            if (index != 1) {
                titles += " | ";
            }
            titles += title;
        }
        
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
        boolean isSponsor = false;
        while ((line = bufferedInput.readLine()) != null) {
            if (line.charAt(0) == index) {
                html = setContentEndAndStart(html, index);
                index++;
            } else if (line.contains("title: ")) {
                html =
                        setTitle(html, line.substring(7, line.length()),
                                index - '1', isSponsor);
                html = setImage(folderPath, html, index - '1');
                html = setText(folderPath, html, index - '1');
            } else if (line.equals("Sponsor")) {
                html += HtmlConstants.SPONSOR_TITLE;
                isSponsor = true;
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
     * @param outputPath
     *            The path to the folder to be created where the output file
     *            will be written to
     * @param outputName
     *            The name of the output file
     * @return the html string representing the expected html file
     *
     * @throws IOException
     *             If the files are not readable
     */
    public String generateHtml(String folderPath, String outputPath,
            String outputName) throws IOException {
        String html = "";

        html += HtmlConstants.OPENING;
        html += getDate();
        html += HtmlConstants.AFTER_DATE;
        html += generateContent(folderPath);
        html += HtmlConstants.ENDING;

        return html;
    }

    /**
     * Replaces all inline images code to the relative variables and write the
     * html file
     * 
     * @param outputPath
     *            The path to the folder to be created where the output file
     *            will be written to
     * @param outputName
     *            The name of the output file
     * @param html
     *            The generated html file
     * @throws FileNotFoundException
     * @throws IOException
     */
    public void writeHtml(String outputPath, String outputName, String html)
            throws IOException {
        String imagePointer = "img src=\"cid:";
        String imagePrefix = "img src=\"";

        while (html.contains(imagePointer)) {
            int imageStartIndex =
                    html.indexOf(imagePointer) + imagePrefix.length();
            int extensionIndex =
                    html.substring(imageStartIndex, html.length()).indexOf(
                            "\">");
            html =
                    html.substring(0, imageStartIndex + extensionIndex)
                            + ".png"
                            + html.substring(imageStartIndex + extensionIndex,
                                    html.length());
            html = html.replaceFirst(imagePointer, imagePrefix);
        }

        File files = new File(outputPath);
        files.mkdirs();
        bufferedOutput =
                new BufferedWriter(new OutputStreamWriter(new FileOutputStream(
                        outputPath + outputName)));
        bufferedOutput.write(html);
        bufferedOutput.close();
    }

    /**
     * Assumes that generateHtml ran successfully
     * 
     * @return String of titles from the items
     */
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
            e.printStackTrace();
        }
    }

}
