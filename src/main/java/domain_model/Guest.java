package domain_model;

import service_layer.ReservationManager;
import utilities.Observer;
import utilities.Subject;
import utilities.UserType;

import java.util.ArrayList;
import java.util.Objects;

public class Guest extends User implements Observer {
    //Region fields
    private final ArrayList<String> reservations;
    private final CreditCard card;
    //end Region

    public Guest(String id, String name, String surname, String email, String telephone, String password, CreditCard card, ReservationManager reservationManager, UserType type) {
        super(id, name, surname, email, telephone, password, type);
        this.card = card;
        this.reservations = new ArrayList<>();
        reservationManager.addObserver(this);
    }

    public Guest(String id, String name, String surname, String telephone, CreditCard card, UserType type) {
        //Cliente senza account
        super(id, name, surname, null, telephone, null, type);
        this.card = card;
        this.reservations = new ArrayList<>();
    }

    //Region getters and setters
    public CreditCard getCard() {
        return card;
    }

    public ArrayList<String> getReservations() {
        return reservations;
    }

    //end Region

    @Override
    public void update(Subject subject, Object argument, String message) {
        //Devo aggiornare la lista delle prenotazioni quando l'utente aggiunge o toglie una prenotazione
        if(argument instanceof Reservation reservation) {
            if (reservation.getClient().equals(this.getId())) //controllo che sia l'utente stesso
                updateReservations(reservation, message);
        }
    }

    private void updateReservations(Reservation reservation, String message) {
        if(message.equals("Add reservation"))
            reservations.add(reservation.getId());

        else if (message.equals("Delete reservation"))
            reservations.remove(reservation.getId());
    }
}
