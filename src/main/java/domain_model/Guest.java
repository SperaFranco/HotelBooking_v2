package domain_model;
import utilities.CreditCard;

import java.util.ArrayList;

public class Guest extends User {
    //Region fields
    private ArrayList<Reservation> reservations;
    private CreditCard card;

    //TODO forse va aggiunta pure la carta d'identità(?)
    //end Region

    public Guest(String id, String name, String surname, String email, String password, String telephone) {
        super(id, name, surname, email, password, telephone);
        this.reservations = new ArrayList<>();
        this.card = null;
    }

    public CreditCard getCard() {
        return card;
    }

    public void setCard(String cardHolderName, String cardNumber, String expiryDate, int CVV) {
        this.card = new CreditCard(cardHolderName, cardNumber, expiryDate, CVV); //TODO rivedere il setCard
        // --> magari durante le registrazioni nel caso di clienti gli viene chiesto di inserire le info della carta di credito

    }
}
