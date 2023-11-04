package domain_model;

import utilities.Observer;
import utilities.Subject;

import java.util.ArrayList;
import java.util.Objects;

public class Guest extends User implements Observer {
    //Region fields
    private ArrayList<Reservation> reservations;
    private CreditCard card;
    private String idCard;
    //end Region

    public Guest(String id, String name, String surname, String email, String telephone, String password, CreditCard card, String idCard) {
        super(id, name, surname, email, telephone, password);
        this.card = null;
        this.idCard = null;
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
    public void updateReservations(Subject subject, Object argument, String message) {
        Reservation newReservation = (Reservation)argument;
        if(newReservation.getClient() == this) {
            if(message.equals("Add reservation"))
                reservations.add(newReservation);
            else if (message.equals("Delete reservation"))
                reservations.remove(newReservation);
            else if (message.contains("Update reservation")) {
                for (Reservation res : reservations) {
                    if (Objects.equals(res.getId(), newReservation.getId()))
                    {
                        if(message.contains("Check in Date")) {
                            res.setCheckIn(newReservation.getCheckIn());
                        } else if (message.contains("Check out Date")) {
                            res.setCheckOut(newReservation.getCheckOut());
                        } else if (message.contains("Description")) {
                            res.setDescription(newReservation.getDescription());
                        }
                    }
                }
            }
        }
    }

}
