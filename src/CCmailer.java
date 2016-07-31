import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 * <p>
 * Reads in the required files and send an e-mail based on the template in used
 * for Computing Club's e-mail blast
 * </p>
 * <p>
 * <b>Main References:</b><br>
 * http://stackoverflow.com/questions/46663/how-can-i-send-an-email-
 * by-java-application-using-gmail-yahoo-or-hotmail<br>
 * http://www.tutorialspoint.com
 * /javamail_api/javamail_api_send_inlineimage_in_email.htm<br>
 * http://www.codejava.net/java-ee/javamail/embedding-images-into-e-mail-with-
 * javamail
 * </p>
 */
public class CCmailer {

    private static final String SERVER_CONNECTED = "Mail server connected";

    private static final String SERVER_CONNECTING =
            "Attempting to connect to the mail server";

    private String privateInfoFile;
    private String folderPath;
    private String outputPath;
    private String outputName;

    private HashMap<String, String> infoMap = new HashMap<String, String>();

    private ArrayList<InternetAddress> to = new ArrayList<InternetAddress>();
    private ArrayList<InternetAddress> cc = new ArrayList<InternetAddress>();
    private ArrayList<InternetAddress> bcc = new ArrayList<InternetAddress>();
    private ArrayList<InternetAddress> replyTo =
            new ArrayList<InternetAddress>();

    private BufferedReader bufferedInput;

    private HtmlGenerator htmlGenerator;

    public CCmailer() {
        htmlGenerator = HtmlGenerator.getInstance();
    }

    private void readPrivateInfo() throws IOException {
        bufferedInput =
                new BufferedReader(new InputStreamReader(new FileInputStream(
                        privateInfoFile)));
        String line;
        int colonIndex;
        while ((line = bufferedInput.readLine()) != null) {
            colonIndex = line.indexOf(": ");
            infoMap.put(line.substring(0, colonIndex),
                    line.substring(colonIndex + 2, line.length()));
        }
        bufferedInput.close();
    }

    private void populateEmailAddresses() throws UnsupportedEncodingException,
            AddressException {
        if (infoMap.containsKey("to")) {
            to.add(new InternetAddress(infoMap.get("to"), infoMap
                    .get("to-name")));
        }
        if (infoMap.containsKey("cc")) {
            cc.add(new InternetAddress(infoMap.get("cc")));
        }
        if (infoMap.containsKey("bcc")) {
            bcc.add(new InternetAddress(infoMap.get("bcc")));
        }
        if (infoMap.containsKey("reply-to")) {
            replyTo.add(new InternetAddress(infoMap.get("reply-to"), infoMap
                    .get("reply-to-name")));
        }
    }

    private void setEmailAddresses(MimeMessage message)
            throws MessagingException, UnsupportedEncodingException {

        InternetAddress internetAddressArray[] = {};
        if (infoMap.containsKey("from")) {
            message.setFrom(new InternetAddress(infoMap.get("from"), infoMap
                    .get("from-name")));
        }
        if (!to.isEmpty()) {
            message.setRecipients(Message.RecipientType.TO,
                    to.toArray(internetAddressArray));
        }
        if (!cc.isEmpty()) {
            message.setRecipients(Message.RecipientType.CC,
                    cc.toArray(internetAddressArray));
        }
        if (!bcc.isEmpty()) {
            message.setRecipients(Message.RecipientType.BCC,
                    bcc.toArray(internetAddressArray));
        }
        if (!replyTo.isEmpty()) {
            message.setReplyTo(replyTo.toArray(internetAddressArray));
        }
    }

    private void processImages(MimeMultipart multipart)
            throws MessagingException, IOException {
        BodyPart messageBodyPart;
        try (DirectoryStream<Path> stream =
                Files.newDirectoryStream(Paths.get(folderPath), "*.png")) {
            for (Path entry : stream) {
                String entryName = entry.getFileName().toString();

                messageBodyPart = new MimeBodyPart();
                DataSource fds = new FileDataSource(folderPath + entryName);

                messageBodyPart.setDataHandler(new DataHandler(fds));
                messageBodyPart.setHeader("Content-ID",
                        "<" + entryName.substring(0, entryName.length() - 4)
                                + ">");

                multipart.addBodyPart(messageBodyPart);
            }
        }
    }

    private MimeMultipart generateHtml() throws MessagingException, IOException {
        MimeMultipart multipart = new MimeMultipart("related");
        BodyPart messageBodyPart = new MimeBodyPart();

        String htmlText =
                htmlGenerator.generateHtml(folderPath, outputPath, outputName);

        messageBodyPart.setContent(htmlText, "text/html");
        multipart.addBodyPart(messageBodyPart);

        processImages(multipart);

        return multipart;
    }

    private void sendEmail() throws MessagingException, IOException {
        Properties properties = System.getProperties();

        properties.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(properties);
        MimeMessage message = new MimeMessage(session);

        setEmailAddresses(message);

        message.setContent(generateHtml());
        message.setSubject(htmlGenerator.getTitles());

        System.out.println(SERVER_CONNECTING);
        Transport transport = session.getTransport("smtp");
        transport.connect(infoMap.get("host"), infoMap.get("username"),
                infoMap.get("password"));
        System.out.println(SERVER_CONNECTED);
        transport.sendMessage(message, message.getAllRecipients());
        transport.close();
    }

    public static void main(String[] args) throws IOException {
        CCmailer ccMailer = new CCmailer();
        ccMailer.privateInfoFile = args[0];
        ccMailer.folderPath = args[1];
        ccMailer.outputPath = args[2];
        ccMailer.outputName = args[3];
        try {
            ccMailer.readPrivateInfo();
            ccMailer.populateEmailAddresses();
            ccMailer.sendEmail();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}