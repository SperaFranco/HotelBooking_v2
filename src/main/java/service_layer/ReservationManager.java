package service_layer;
import domain_model.Reservation;
import utilities.Subject;

import java.time.LocalDate;
import java.util.*;

public class ReservationManager extends Subject {

    private Map<String, Reservation> reservationMap;

    public ReservationManager() {
        reservationMap = new HashMap<>();
    }

    //Notare che TUTTE queste operazioni mi vanno a fare delle notifiche agli observer!
    public Reservation findReservationById(String id) {
        //TODO Da Implementare facendo il recupero della prenotazione dal database
        return reservationMap.get(id);
    }

    public void insertReservation(Reservation reservation) {
        //TODO Da implementare aggiungendo l'oggetto nel database
        //Dopo aver chiesto di inserire tutti i dati della prenotazione (bisogna
        // a quel punto tramite il database andrò a inserire la mia prenotazione
        String idReservation = reservation.getId();
        reservationMap.put(idReservation, reservation);
        setChanged();
        notifyObservers( reservation,"Add reservation");
    }

    public void updateReservation(String id) {
        //TODO Da implementare cambiando l'oggetto nel database
        //TODO capire come modificare l'oggetto nel db --> tolgo e inserisco oppure cambio sul posto?
        Scanner scanner = new Scanner(System.in);
        String choice;
        boolean modified = false;

        //Recupero inzialmente la prenotazione richiesta
        Reservation reservation = reservationMap.get(id);
        System.out.print("Please enter what you to modify:" +
                "\n1) Check in Date" +
                "\n2) Check out Date " +
                "\n3) Description " +
                "\nChoice:");
        choice = scanner.nextLine();
        switch (choice) {
            case "Check in Date":
                LocalDate newCheckInDate;
                System.out.print("Then please insert the new check in date(for example 2007-12-01): ");
                newCheckInDate = LocalDate.parse(scanner.nextLine());
                reservation.setCheckIn(newCheckInDate);
                modified = true;
                break;
            case "Check out Date":
                LocalDate newCheckOutDate;
                System.out.print("Then please insert the new check out date(for example 2007-10-1): ");
                newCheckOutDate = LocalDate.parse(scanner.nextLine());
                reservation.setCheckIn(newCheckOutDate);
                modified = true;
                break;
            case "Description":
                String description;
                System.out.print("Then please insert the new description:");
                description = scanner.nextLine();
                reservation.setDescription(description);
                modified = true;
                break;
            default:
                System.out.println("Other options are not editable... ");
        }
        // e a seconda di cosa voglio modificare nella prenotazione la modifico nel db
        if (modified)
            setChanged();
        notifyObservers(reservation, "Update reservation" + choice);
    }

    public void deleteReservation(String id) {
        //Allo stesso modo recupero la prenotazione e la elimino
        Reservation reservationRemoved = findReservationById(id);
        reservationMap.remove(id);
        setChanged();
        notifyObservers(reservationRemoved, "Delete reservation");
    }



}
