import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 * <p>
 * class CCmailer reads in the required files and send an e-mail based on the
 * template in used for Computing Club's e-mail blast
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
 * 
 * @author Benedict
 *
 */
public class CCmailer {

    private static final String FILE_PRIVATE_INFO = "privateinfo.txt";

    private static HashMap<String, String> infoMap =
            new HashMap<String, String>();

    private static ArrayList<InternetAddress> to =
            new ArrayList<InternetAddress>();
    private static ArrayList<InternetAddress> cc =
            new ArrayList<InternetAddress>();
    private static ArrayList<InternetAddress> bcc =
            new ArrayList<InternetAddress>();
    private static ArrayList<InternetAddress> replyTo =
            new ArrayList<InternetAddress>();

    private static BufferedReader bufferedInput;

    private static void sendEmail() {
        Properties properties = System.getProperties();

        properties.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(properties);
        MimeMessage message = new MimeMessage(session);
        InternetAddress internetAddressArray[] = {};

        try {
            message.setFrom(new InternetAddress(infoMap.get("from"), infoMap
                    .get("from-name")));
            message.setReplyTo(replyTo.toArray(internetAddressArray));
            message.setRecipients(Message.RecipientType.TO,
                    to.toArray(internetAddressArray));
            /*
             * message.setRecipients(Message.RecipientType.CC,
             * cc.toArray(internetAddressArray));
             * message.setRecipients(Message.RecipientType.BCC,
             * bcc.toArray(internetAddressArray));
             */

            message.setSubject(infoMap.get("subject"));

            // This mail has 2 part, the BODY and the embedded image
            MimeMultipart multipart = new MimeMultipart("related");

            // first part (the html)
            BodyPart messageBodyPart = new MimeBodyPart();
            String htmlText =
                    "<H1>Hello</H1><img src=\"cid:image1\"><br><br><img src=\"cid:image2\">";
            messageBodyPart.setContent(htmlText, "text/html");
            // add it
            multipart.addBodyPart(messageBodyPart);

            // second part (the image)
            messageBodyPart = new MimeBodyPart();
            DataSource fds = new FileDataSource("img1.png");

            messageBodyPart.setDataHandler(new DataHandler(fds));
            messageBodyPart.setHeader("Content-ID", "<image1>");

            // add image to the multipart
            multipart.addBodyPart(messageBodyPart);

            // second part (the image2)
            messageBodyPart = new MimeBodyPart();
            fds = new FileDataSource("img2.png");

            messageBodyPart.setDataHandler(new DataHandler(fds));
            messageBodyPart.setHeader("Content-ID", "<image2>");

            // add image2 to the multipart
            multipart.addBodyPart(messageBodyPart);

            // put everything together
            message.setContent(multipart);

            Transport transport = session.getTransport("smtp");
            transport.connect(infoMap.get("host"), infoMap.get("username"),
                    infoMap.get("password"));
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {

        try {
            bufferedInput =
                    new BufferedReader(new InputStreamReader(
                            new FileInputStream(FILE_PRIVATE_INFO)));
            String line;
            int index;
            while ((line = bufferedInput.readLine()) != null) {
                index = line.indexOf(": ");
                infoMap.put(line.substring(0, index),
                        line.substring(index + 2, line.length()));
            }
            bufferedInput.close();

            to.add(new InternetAddress(infoMap.get("to"), infoMap
                    .get("to-name")));
            // cc.add(new InternetAddress(infoMap.get("cc")));
            // bcc.add(new InternetAddress(infoMap.get("bcc")));
            replyTo.add(new InternetAddress(infoMap.get("reply-to"), infoMap
                    .get("reply-to-name")));

            sendEmail();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}