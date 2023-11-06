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

    public ArrayList<Reservation> getReservations() {
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
        if(reservation.getClient() == this) {
            if(message.equals("Add reservation"))
                reservations.add(reservation);
            else if (message.equals("Delete reservation"))
                reservations.remove(reservation);
            else if (message.contains("Update reservation")) {
                for (Reservation res : reservations) {
                    if (Objects.equals(res.getId(), reservation.getId()))
                    {
                        if(message.contains("Check in Date")) {
                            res.setCheckIn(reservation.getCheckIn());
                        } else if (message.contains("Check out Date")) {
                            res.setCheckOut(reservation.getCheckOut());
                        } else if (message.contains("Description")) {
                            res.setNotes(reservation.getNotes());
                        }
                    }
                }
            }
        }
    }

}
