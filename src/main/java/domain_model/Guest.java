package domain_model;

import utilities.Observer;
import utilities.Subject;

import java.util.ArrayList;

public class Guest extends User implements Observer {
    //Region fields
    private ArrayList<Reservation> reservations;
    private CreditCard card;

    //TODO forse va aggiunta pure la carta d'identità(?)
    //end Region

    public Guest(String id, String name, String surname, String email, String password, String telephone) {
        super(id, name, surname, email, password, telephone);
        this.card = null;
        this.reservations = new ArrayList<>();
    }

    public CreditCard getCard() {
        return card;
    }

    public void setCard(String cardHolderName, String cardNumber, String expiryDate, int CVV) {
        this.card = new CreditCard(cardHolderName, cardNumber, expiryDate, CVV); //TODO rivedere il setCard
        // --> magari durante le registrazioni nel caso di clienti gli viene chiesto di inserire le info della carta di credito
    }

    @Override
    public void updateAvailability(Subject subject, Object argument) {
        //Fake implementation?
    }

    @Override
    public void updateReservations(Subject subject, Object argument) {
        Reservation newReservation = (Reservation)argument;
        reservations.add(newReservation);
    }

}
