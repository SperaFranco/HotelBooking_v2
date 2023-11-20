package domain_model;

import utilities.Observer;
import utilities.Subject;

import java.util.ArrayList;
import java.util.Objects;

public class Guest extends User implements Observer {
    //Region fields
    private final ArrayList<String> reservations;
    private CreditCard card;
    private String idCard;
    //end Region

    public Guest(String id, String name, String surname, String email, String telephone, String password, CreditCard card, String idCard) {
        super(id, name, surname, email, telephone, password);
        this.card = null;
        this.idCard = null;
        this.reservations = new ArrayList<>();
    }

    //Region getters and setters
    public CreditCard getCard() {
        return card;
    }

    public void setCard(String cardHolderName, String cardNumber, String expiryDate, int CVV) {
        this.card = new CreditCard(cardHolderName, cardNumber, expiryDate, CVV); //TODO rivedere il setCard
        // --> magari durante le registrazioni nel caso di clienti gli viene chiesto di inserire le info della carta di credito
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public ArrayList<String> getReservations() {
        return reservations;
    }

    //end Region

    @Override
    public void update(Subject subject, Object argument, String message) {
        if(argument instanceof Reservation) {
            Reservation reservation = (Reservation) argument;
            this.updateReservations(reservation, message);
        }
    }

    private void updateReservations(Reservation reservation, String message) {
        //Controllare se tenere
        if(reservation.getClient().equals(getId())) {
            if(message.equals("Add reservation"))
                reservations.add(reservation.getId());
            else if (message.equals("Delete reservation"))
                reservations.remove(reservation.getId());
            }
        }
    }

}
