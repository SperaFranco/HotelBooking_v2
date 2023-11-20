package service_layer;
import domain_model.*;
import utilities.IdGenerator;
import utilities.Subject;

import java.time.LocalDate;
import java.util.*;

public class ReservationManager extends Subject {
    private final Map<String, Reservation> reservationMap;
    private final AccountManager accountManager; //occhio ai controllers
    private final Scanner scanner;
    public ReservationManager(Scanner scanner, AccountManager accountManager) {
        reservationMap = new HashMap<>();
        this.scanner = scanner;
        this.accountManager = accountManager;
    }

    public void addReservation(User client, LocalDate checkIn, LocalDate checkOut, int numOfGuests, String hotelReserved, String roomReserverd) {
        //TODO Da implementare aggiungendo l'oggetto nel database
        String response;

        //Nel caso l'user sia nullo chiedo di fare il login oppure di fare un nuovo account
        if(client == null){
            System.out.println("Please do the login to proceed or registrate if you don't have an account!");
            System.out.print("Do you have already an account?\nPlease answer \"yes\" or \"no\":");
            response = scanner.nextLine();
            if (response.equalsIgnoreCase("yes")) {
                client = accountManager.login();
            }else if (response.equalsIgnoreCase("no")){
                client = accountManager.doRegistration();
            }else {
                throw new RuntimeException("Option not in the list!");
            }
        }

        //Altrimenti chiedi per la description e aggiungi la prenotazione
        System.out.print("Is anything else you need in your reservation??:");
        String description = scanner.nextLine();
        Reservation newReservation = new Reservation(IdGenerator.generateReservationID(), checkIn, checkOut,
                numOfGuests, description, hotelReserved, roomReserverd, client.getId());
        //TODO ci sarebbe da aggiungere il controllo per il pagamento da qualche parte

        reservationMap.put(newReservation.getId(), newReservation);
        System.out.println("Reservation added! Thank you! :)");
        setChanged();
        notifyObservers(newReservation,"Add reservation");
    }

    public void updateReservation() {
        //TODO Da implementare cambiando l'oggetto nel database
        //TODO capire come modificare l'oggetto nel db --> tolgo e inserisco oppure cambio sul posto?
        int choice;
        boolean modified = false;
        String id;

        System.out.print("Choose the reservation to modify(enter the ID):");
        id = scanner.nextLine();
        //Recupero inzialmente la prenotazione richiesta
        Reservation reservation = findReservationById(id);
        if (reservation != null) {
            System.out.print("What do you want to modify?:" +
                    "\n1) Check in Date" +
                    "\n2) Check out Date " +
                    "\n3) Description " +
                    "\nChoice:");
            choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1:
                    LocalDate newCheckInDate;
                    System.out.print("Then please insert the new check in date(for example 2007-12-01): ");
                    newCheckInDate = LocalDate.parse(scanner.nextLine());
                    reservation.setCheckIn(newCheckInDate);
                    modified = true;
                    break;
                case 2:
                    LocalDate newCheckOutDate;
                    System.out.print("Then please insert the new check out date(for example 2007-10-1): ");
                    newCheckOutDate = LocalDate.parse(scanner.nextLine());
                    reservation.setCheckIn(newCheckOutDate);
                    modified = true;
                    break;
                case 3:
                    String description;
                    System.out.print("Then please insert the new description:");
                    description = scanner.nextLine();
                    reservation.setNotes(description);
                    modified = true;
                    break;
                default:
                    System.out.println("Other options are not editable... ");
            }
        } else
            System.out.println("Reservation not found!");

        if (modified)
            System.out.println("Reservation modified correctly!");
    }
        // e a seconda di cosa voglio modificare nella prenotazione la modifico nel db

    public void deleteReservation() {
        //TODO da implementare facendo rimozione dal db
            //Allo stesso modo recupero la prenotazione e la elimino
            String id;

            System.out.print("Choose the reservation to modify:");
            id = scanner.nextLine();
            Reservation reservationRemoved = findReservationById(id);
            reservationMap.remove(id);
            setChanged();
            notifyObservers(reservationRemoved, "Delete reservation");
    }
    public void getReservations(Guest guest) {
        //TODO a seconda del guest ricavo tutte le sue prenotazioni e le stampo (per guestMenu)
        System.out.println("These are " + guest.getName() + " reservations:");
        for (Reservation reservation: findReservationByGuest(guest.getId())) {
            System.out.print(reservation.getInfoReservation());
        }
    }
    public void getAllReservations(Hotel hotel) {
        // A seconda dell'hotel tutte le prenotazioni stampo le prenotazioni
        if (hotel != null){
            ArrayList<Reservation> reservationsForHotel = new ArrayList<>();

        //lascio le prenotazioni che mi servono (probabilmente con il db ci sarà da fare una query)
        for (Map.Entry<String, Reservation> entry : reservationMap.entrySet()) {
            Reservation reservation = entry.getValue();
            if (reservation.getHotel().equals(hotel.getId()))
                reservationsForHotel.add(reservation);
        }

        if (!reservationsForHotel.isEmpty()) {
            System.out.println("These are " + hotel.getName() + " reservations:");
            for (Reservation reservation : reservationsForHotel) {
                System.out.println(reservation.getInfoReservation());
            }
        } else
            System.out.println("No reservations for the hotel " + hotel.getName());
        }else
            System.out.println("Please choose an hotel first!");
    }
    private Reservation findReservationById(String id) {
        //TODO Da Implementare facendo il recupero della prenotazione dal database
        return reservationMap.get(id);
    }
    private ArrayList<Reservation> findReservationByGuest(String guestID) {
        ArrayList<Reservation> reservations = new ArrayList<>(reservationMap.values());
        ArrayList<Reservation> myReservations = new ArrayList<>();

        for (Reservation reservation : reservations) {
            if (reservation.getClient().equals(guestID)) {
                myReservations.add(reservation);
            }
        }
        return myReservations;
    }

    public String chooseRoomToReserve(Hotel hotel, LocalDate checkIn, LocalDate checkOut) {
        String roomID = null;
        System.out.println("Which room is to reserve?");
        ArrayList<Room> rooms = hotel.getRoomsAvailable(checkIn, checkOut);

        if(!rooms.isEmpty()) {
            System.out.println("These are the rooms available:");
            for (Room room: rooms) {
                int index = 1;
                room.getRoomInfo(index++);
            }

            System.out.println("Please enter the id of the room to book");
            roomID = scanner.nextLine();
        }else
            System.out.println("No rooms are available for these days...");
        return roomID;
    }
}
