package service_layer;
import domain_model.Guest;
import domain_model.Hotel;
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

    public void addReservation(Reservation reservation) {
        //TODO Da implementare aggiungendo l'oggetto nel database
        //TODO chiarire se questo metodo serve come ultimo aspetto di inserimento prenotazione (si simula prima)
        // oppure se in questo metodo si chiede di inserire tutte le info richieste

        String idReservation = reservation.getId();
        reservationMap.put(idReservation, reservation);
        setChanged();
        notifyObservers(reservation,"Add reservation");
    }

    public void updateReservation(String id) {
        //TODO Da implementare cambiando l'oggetto nel database
        //TODO capire come modificare l'oggetto nel db --> tolgo e inserisco oppure cambio sul posto?
        Scanner scanner = new Scanner(System.in);
        String choice;
        boolean modified = false;

        //Recupero inzialmente la prenotazione richiesta
        Reservation reservation = findReservationById(id);
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
                reservation.setNotes(description);
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
        //TODO da implementare facendo rimozione dal db
        //Allo stesso modo recupero la prenotazione e la elimino
        Reservation reservationRemoved = findReservationById(id);
        reservationMap.remove(id);
        setChanged();
        notifyObservers(reservationRemoved, "Delete reservation");
    }

    public void getReservations(Guest guest) {
        //TODO a seconda del guest ricavo tutte le sue prenotazioni e le stampo
        ArrayList<Reservation> reservations = guest.getReservations();
        System.out.println("These are " + guest.getName() + " reservations:");
        for (Reservation reservation:reservations) {
            System.out.print(reservation.getInfoReservation());
        }
    }

    public void getAllReservations(Hotel hotel) {
        //TODO qui invece a seconda dell'hotel stampo le prenotazioni
        ArrayList<Reservation> reservationsForHotel = new ArrayList<>();

        //lascio le prenotazioni che mi servono (probabilmente con il db ci sarà da fare una query)
        for (Map.Entry<String, Reservation> entry:reservationMap.entrySet()) {
            Reservation reservation = entry.getValue();
            if(reservation.getHotel().getId().equals(hotel.getId()))
                reservationsForHotel.add(reservation);
        }
        System.out.println("These are " + hotel.getName() + " reservations:" );
        for(Reservation reservation:reservationsForHotel) {
            reservation.getInfoReservation();
        }
    }

}
