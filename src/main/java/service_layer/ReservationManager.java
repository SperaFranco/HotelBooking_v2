package service_layer;
import domain_model.*;
import utilities.IdGenerator;
import utilities.Research;
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
    public void doReservation(Guest user, Research info, Hotel hotel,String roomID) {
        doReservationHelper(user, info, hotel, roomID);
    }
    public void addReservation(Reservation newReservation) {
        //TODO Da implementare aggiungendo l'oggetto nel database
        //TODO guardare come fare per mandare email di notifica prenotazione (qui o nel doReservation)
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

    //Region helpers
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
    private void doReservationHelper(Guest user, Research info, Hotel hotel, String roomID) {
        //Prima di proseguire con la prenotazione vedo se l'utente si è loggato
        String response = null;

        //Nel caso l'user sia nullo chiedo di fare il login oppure di fare un nuovo account
        if(user == null){
            System.out.println("Please do the login to proceed or registrate if you don't have an account!");
            System.out.print("Do you have already an account?\nPlease answer \"yes\" or \"no\":");
            response = scanner.nextLine();
            if (response.equalsIgnoreCase("yes")) {
                user = (Guest)accountManager.login();
            }else if (response.equalsIgnoreCase("no")){
                user = (Guest)accountManager.doRegistration();
            }else {
                throw new RuntimeException("Option not in the list!");
            }
        }

        System.out.println("Checking If there is enough money on the card...");
        CreditCard card = user.getCard();
        double roomPrice = hotel.getCalendar().getTotalPrice(info.getCheckIn(), info.getCheckOut(), roomID);
        boolean paymentsuccessful = false;
        while (!paymentsuccessful) {
            try {
                card.doPayment(roomPrice);
                paymentsuccessful = true;
            } catch (RuntimeException e) {
                System.out.println("Payment failed: " + e.getMessage());
                System.out.println("Do you want to add more funds to your card? (yes/no): ");
                String otherResponse = scanner.nextLine();

                if(otherResponse.equalsIgnoreCase("yes")) {
                    System.out.println("Enter the amount to add to your card: ");
                    double amount = Double.parseDouble(scanner.nextLine());
                    card.setBalance(card.getBalance() + amount);
                }else
                    break; //l'utente ci ha ripensato e non vuole continuare con la prenotazione
            }
        }

        if (paymentsuccessful) {
            //Chiedo all'utente di aggiungere un ulteriore descrizione se serve
            System.out.print("Is anything else you need in your reservation??:");
            String description = scanner.nextLine();

            Reservation newReservation = new Reservation(IdGenerator.generateReservationID(hotel.getId(), user.getName(), user.getSurname(), info.getCheckIn()),
                    info.getCheckIn(), info.getCheckOut(), info.getNumOfGuest(), description, hotel.getId(), roomID, user.getId());
            addReservation(newReservation);
        }else
            System.out.println("Returning back to start menu");
    }
    //End Region helpers
}
