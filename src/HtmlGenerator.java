import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
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

    private static final String ACAD_ADVISORY = "Academic Advisory";
    private static final String CC_NEWSLETTER = "Computing Club Newsletter";
    
    private static final String TITLE_PREFIX_CC = "[Comp Club] ";
    private static final String TITLE_PREFIX_AA = "[Acad Advisory] ";
    private static final String MAIN_TXT = "main.txt";

    private static final String FLAG_EMAIL_BLAST = "emailBlast";
    private static final String FLAG_ACAD_ADVISORY = "acadAdvisory";

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

    private String setTitle(String title, int index, boolean isSponsor) {
        if (!isSponsor) {
            if (index != 1) {
                titles += " | ";
            }
            titles += title;
        }
        
        return String.format(HtmlConstants.CONTENT_TITLE, title);
    }

    private String setImage(String folderPath, String outputPath, int index) throws IOException {
        if ((new File(folderPath + "img" + index + ".png")).exists()) {
            Files.copy(Paths.get(folderPath + "img" + index + ".png"), 
                    Paths.get(outputPath + "contents/img" + index + ".png"));
            int urlStart = outputPath.indexOf("newsletters.nuscomputing.com");
            return String.format(HtmlConstants.CONTENT_IMG, "http://" 
                    + outputPath.substring(urlStart, outputPath.length()) 
                    + "contents/img" + index);
        }
        return "";
    }

    private String setText(String folderPath, int index) {
        if ((new File(folderPath + "txt" + index + ".html")).exists()) {
            return "txt" + index;
        }
        return "";
    }

    private String setLink(String html, String link) throws IOException {
        String linkText = bufferedInput.readLine();
        return String.format(HtmlConstants.CONTENT_LINK, link, 
                linkText.substring(11, linkText.length()));
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

    private String generateContent(String folderPath, String outputPath,
            String emailType) throws IOException {
        String html = "";
        
        if (emailType.equals(FLAG_EMAIL_BLAST)) {
            titles = TITLE_PREFIX_CC;
        } else if (emailType.equals(FLAG_ACAD_ADVISORY)) {
            titles = TITLE_PREFIX_AA;
        }

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
                html += setTitle(line.substring(7, line.length()),
                                index - '1', isSponsor);
                html += setImage(folderPath, outputPath, index - '1');
                html += setText(folderPath, index - '1');
            } else if (line.equals("Sponsor")) {
                html += HtmlConstants.CONTENT_END;
                html += HtmlConstants.SPONSOR_TITLE;
                html += HtmlConstants.CONTENT_START;
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
            String outputName, String emailType) throws IOException {
        String html = "";
        File files = new File(outputPath);
        files.mkdirs();
        html += HtmlConstants.OPENING;
        html += getDate();
        if (emailType.equals(FLAG_EMAIL_BLAST)) {
            html += String.format(HtmlConstants.AFTER_DATE, CC_NEWSLETTER);
        } else if (emailType.equals(FLAG_ACAD_ADVISORY)) {
            html += String.format(HtmlConstants.AFTER_DATE, ACAD_ADVISORY);
            html += HtmlConstants.CONTENT_START;
            html += HtmlConstants.ACAD_LOGO;
            html += HtmlConstants.CONTENT_END;
        }
        html += generateContent(folderPath, outputPath, emailType);
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
        bufferedOutput =
                new BufferedWriter(new OutputStreamWriter(new FileOutputStream(
                        outputPath + outputName)));
        int urlStart = outputPath.indexOf("newsletters.nuscomputing.com");
        bufferedOutput =
                new BufferedWriter(new OutputStreamWriter(new FileOutputStream(
                        outputPath.substring(0, urlStart 
                                + "newsletters.nuscomputing.com".length()) 
                                + outputName)));
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
                            "out.html", "emailBlast");
            System.out.println(generatedHTML);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
