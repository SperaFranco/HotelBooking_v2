package service_layer;
import domain_model.Reservation;
import utilities.Subject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ReservationManager extends Subject {

    private Map<String, Reservation> reservationMap;

    public ReservationManager() {
        reservationMap = new HashMap<>();
    }

    //Notare che tutte queste operazioni mi vanno a fare delle notifiche agli observer!
    public Reservation findReservationById(String id) {
        //Implemento il recupero della prenotazione dal database
        return null;
    }

    public void insertReservation(Reservation reservation) {
        //Dopo aver chiesto di inserire tutti i dati della prenotazione (bisogna
        // a quel punto tramite il database andrò a inserire la mia prenotazione
        String idReservation = reservation.getId();
        reservationMap.put(idReservation, reservation);
        setChanged();
        notifyObservers(this);
    }

    public void updateReservation(String id) {
        //Recupero inzialmente la prenotazione richiesta
        // e a seconda di cosa voglio modificare nella prenotazione la modifico nel db
        //TODO capire come e quando chiedere come modificare la prenotazione
    }

    public void deleteReservation(String id) {
        //Allo stesso modo recupero la prenotazione e la elimino
    }



}
