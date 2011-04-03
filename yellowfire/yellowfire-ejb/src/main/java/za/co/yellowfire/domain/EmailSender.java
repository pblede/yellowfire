package za.co.yellowfire.domain;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * @author Mark P Ashworth
 * @version 0.0.1
 */
@Stateless(name = "EmailSender")
public class EmailSender {
    
    @Resource(mappedName = "mail/bluefire.mail")
    private Session session;

    public void send(String greeting) throws MessagingException {
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress("mp.ashworth@gmail.com"));
        message.addRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress("mp.ashworth@gmail.com"));
        message.setSubject("New greeting has arrived!");
        message.setText(greeting);
        Transport.send(message);
    }
}
