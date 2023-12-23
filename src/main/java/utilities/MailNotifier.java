package utilities;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import domain_model.User;
import domain_model.Reservation;

public class MailNotifier {
    public static void sendEmail(User sender, User recipient, String subject, Reservation reservation) throws AddressException {
        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.ssl.protocols", "TLSv1.2");


        Authenticator authenticator = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(sender.getEmail(), "wgek dnnl pwuu yhvi");
            }
        };

        Session session = Session.getInstance(properties, authenticator);
        try {
            Message message = new MimeMessage(session); //Estende Message e offre più funzionalità
            message.setFrom(new InternetAddress(sender.getEmail()));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient.getEmail()));
            message.setSubject(subject);
            message.setText(getBody(recipient, reservation));
            Transport.send(message);
            System.out.println("Email sent correctly.");
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    private static String getBody(User recipient, Reservation reservation) {
        String summary =  "\nClient: " + recipient.getName() + " " + recipient.getSurname() +
                "\nHotel Reserved: " + reservation.getHotel() + //sarebbe più bellino stamparne il nome
                "\nRoom Reserved: " + reservation.getRoomReserved() +
                "\nFor dates: " + reservation.getCheckIn() + " to " + reservation.getCheckOut() +
                "\nNumber of guest/guests: " + reservation.getNumOfGuests() +
                "\nNotes: "+ reservation.getNotes();

        switch (recipient.getType()) {
            case GUEST -> {
                return  "Thank you for your reservation :-)" +
                        "\nBelow the summary:" + summary;

            }
            case HOTEL_DIRECTOR -> {
                return "Reservation added for: " + summary;
            }
            default -> {
                return " :-( ";
            }
        }
    }
}
