import java.io.*;
import java.util.*;

import javax.mail.*;
import javax.mail.internet.*;

/**
 * <p>
 * class CCmailer reads in the required files and send an e-mail based on the
 * template in used for Computing Club's e-mail blast
 * </p>
 * <p>
 * Reference to
 * http://stackoverflow.com/questions/46663/how-can-i-send-an-email-
 * by-java-application-using-gmail-yahoo-or-hotmail
 * </p>
 * 
 * @author Benedict
 *
 */
public class CCmailer {

    private static final String FILE_PRIVATE_INFO = "privateinfo.txt";

    private static HashMap<String, String> infoMap =
            new HashMap<String, String>();

    private static String host;
    private static String username;
    private static String password;
    private static InternetAddress from;
    private static ArrayList<InternetAddress> to =
            new ArrayList<InternetAddress>();
    private static ArrayList<InternetAddress> cc =
            new ArrayList<InternetAddress>();
    private static ArrayList<InternetAddress> bcc =
            new ArrayList<InternetAddress>();
    private static ArrayList<InternetAddress> replyTo =
            new ArrayList<InternetAddress>();

    private static String subject;
    private static String body;

    private static BufferedReader bufferedInput;

    private static void sendEmail() {
        Properties properties = System.getProperties();

        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.user", username);
        properties.put("mail.smtp.password", to);
        properties.put("mail.smtp.auth", "true");

        Session session = Session.getInstance(properties);
        MimeMessage message = new MimeMessage(session);
        InternetAddress internetAddressArray[] = {};

        try {
            message.setFrom(from);
            message.setReplyTo(replyTo.toArray(internetAddressArray));
            message.setRecipients(Message.RecipientType.TO,
                    to.toArray(internetAddressArray));
            /*
             * message.setRecipients(Message.RecipientType.CC,
             * cc.toArray(internetAddressArray));
             * message.setRecipients(Message.RecipientType.BCC,
             * bcc.toArray(internetAddressArray));
             */

            message.setSubject(subject);
            message.setContent(body, "text/html");
            Transport transport = session.getTransport("smtp");
            transport.connect(host, username, password);
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

            host = infoMap.get("host");
            username = infoMap.get("username");
            password = infoMap.get("password");
            to.add(new InternetAddress(infoMap.get("to"), infoMap
                    .get("to-name")));
            // cc.add(new InternetAddress(infoMap.get("cc")));
            // bcc.add(new InternetAddress(infoMap.get("bcc")));
            replyTo.add(new InternetAddress(infoMap.get("reply-to"), infoMap
                    .get("reply-to-name")));
            from =
                    new InternetAddress(infoMap.get("from"),
                            infoMap.get("from-name"));
            subject = infoMap.get("subject");
            body = "<h1>Hello world</h1><a href=\"nuscomputing.com\">link</a>";

            sendEmail();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}