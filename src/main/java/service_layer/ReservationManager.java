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
    private final CalendarManager calendarManager;
    private final Scanner scanner;

    public ReservationManager(Scanner scanner, AccountManager accountManager, CalendarManager calendarManager) {
        reservationMap = new HashMap<>();
        this.scanner = scanner;
        this.accountManager = accountManager;
        this.calendarManager = calendarManager;
    }
    public void doReservation(Guest user, Research info, Hotel hotel,String roomID) {
        doReservationHelper(user, info, hotel, roomID);
    }
    public void addReservation(Reservation newReservation) {
        //TODO guardare come fare per mandare email di notifica prenotazione (qui o nel doReservation)
        reservationMap.put(newReservation.getId(), newReservation);
        System.out.println("Reservation added! Thank you! :)");
        setChanged();
        notifyObservers(newReservation,"Add reservation");
    }
    public void updateReservation() {
        int choice;
        boolean modified = false;
        String id;

        System.out.print("Choose the reservation to modify(enter the ID):");
        id = scanner.nextLine();
        //Recupero inizialmente la prenotazione richiesta
        Reservation reservation = findReservationById(id);
        if (reservation != null) {
            LocalDate checkIn = reservation.getCheckIn();
            LocalDate checkOut = reservation.getCheckOut();
            String roomReserved = reservation.getRoomReserved();

            System.out.print("What do you want to modify? (Enter an option between 1 or 2):" +
                    "\n1) Check-in and Check-out dates" +
                    "\n2) Description " +
                    "\nOther options are not editable... Please consider to delete your reservation if you need to." +
                    "\nChoice:");
           try {
               choice = Integer.parseInt(scanner.nextLine());
           }
           catch (NumberFormatException e) {
               System.out.println("Invalid input. Please Enter a number");
               return;
           }

            switch (choice) {
                case 1:
                    LocalDate newCheckInDate, newCheckOutDate;
                    System.out.print("Then please insert the new dates\n" + "Check-in date: ");
                    newCheckInDate = LocalDate.parse(scanner.nextLine());
                    System.out.print("Check-out date:");
                    newCheckOutDate = LocalDate.parse(scanner.nextLine());
                    HotelCalendar calendar = calendarManager.getCalendarByHotelID(reservation.getHotel());

                    //Controllo che date non si intersechino fra di loro
                    if (newCheckOutDate.isBefore(checkIn) || newCheckInDate.isAfter(checkOut)) {

                        //controllo che per le nuove date la camera sia disponibile
                        if(calendar.isRoomAvailable(newCheckInDate, newCheckOutDate, roomReserved)) {
                            //allora vanno settati i giorni vecchi a disponibili
                            calendar.setRoomAvailability(roomReserved, checkIn, checkOut, true);

                            //mentre quelli nuovi a non disponibili
                            calendar.setRoomAvailability(roomReserved, newCheckInDate, newCheckOutDate, false);
                            reservation.setCheckIn(newCheckInDate);
                            reservation.setCheckOut(newCheckOutDate);
                            modified = true;
                        }
                    }
                    else if (newCheckOutDate.isEqual(checkOut)) {
                        if(newCheckInDate.isBefore(checkIn)) {
                            if(calendar.isRoomAvailable(newCheckInDate, checkIn, roomReserved)) {
                                //mi basta arrivare al checkIn
                                calendar.setRoomAvailability(roomReserved, newCheckInDate, checkIn, false);
                                reservation.setCheckIn(newCheckInDate);
                                modified = true;
                            }
                        }
                        else if(newCheckInDate.isAfter(checkIn)) {
                            //In questo caso non devo controllare ma devo solo modificare la disponibilità
                            calendar.setRoomAvailability(roomReserved, checkIn, newCheckInDate.minusDays(1), true);
                            reservation.setCheckIn(newCheckInDate);
                            modified = true;
                        }
                    }
                    else if (newCheckInDate.isEqual(checkIn)) {
                        //qui il viceversa
                        if(newCheckOutDate.isBefore(checkOut)){
                            calendar.setRoomAvailability(roomReserved, newCheckOutDate.plusDays(1), checkOut, true);
                            reservation.setCheckOut(newCheckOutDate);
                            modified = true;
                        }
                        else if(newCheckOutDate.isAfter(checkOut)){
                            if(calendar.isRoomAvailable(checkOut, newCheckOutDate, roomReserved)){
                                calendar.setRoomAvailability(roomReserved, checkOut, newCheckOutDate, false);
                                reservation.setCheckOut(newCheckOutDate);
                                modified = true;
                            }
                        }
                    }
                    else if (newCheckInDate.isAfter(checkIn) && newCheckInDate.isBefore(checkOut) && newCheckOutDate.isAfter(checkOut)){
                        if (calendar.isRoomAvailable(checkOut.plusDays(1), newCheckOutDate, roomReserved)) {
                            calendar.setRoomAvailability(roomReserved, checkIn, newCheckInDate, true);
                            calendar.setRoomAvailability(roomReserved, newCheckInDate, newCheckOutDate, false);
                            reservation.setCheckIn(newCheckInDate);
                            reservation.setCheckOut(newCheckOutDate);
                            modified = true;
                        }
                    }
                    else if (newCheckOutDate.isBefore(checkOut) && newCheckOutDate.isAfter(checkIn) && newCheckInDate.isBefore(checkIn)) {
                        if (calendar.isRoomAvailable(newCheckInDate, checkIn.minusDays(1), roomReserved)) {
                            calendar.setRoomAvailability(roomReserved, newCheckOutDate, checkOut, true);
                            calendar.setRoomAvailability(roomReserved, newCheckInDate, newCheckOutDate, false);
                            reservation.setCheckIn(newCheckInDate);
                            reservation.setCheckOut(newCheckOutDate);
                            modified = true;
                        }
                    }
                    else if (newCheckInDate.isAfter(checkIn) && newCheckOutDate.isBefore(checkOut)) {
                        calendar.setRoomAvailability(roomReserved, checkIn, newCheckInDate.minusDays(1), true);
                        calendar.setRoomAvailability(roomReserved, newCheckOutDate.plusDays(1), checkOut, true);
                        reservation.setCheckIn(newCheckInDate);
                        reservation.setCheckOut(newCheckOutDate);
                        modified = true;
                    }
                    break;
                case 2:
                    String description;
                    System.out.print("Then please insert the new description:");
                    description = scanner.nextLine();
                    reservation.setNotes(description);
                    modified = true;
                    break;
                default:
                    System.out.println("Not an option in the list...");
            }
        }else {
            System.out.println("Reservation not found!");
        }
        if (modified)
            System.out.println("Reservation modified correctly!");
        else
            System.out.println("Reservation not modified correctly");
    }
    public void deleteReservation() {
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
        //A seconda del guest ricavo tutte le sue prenotazioni e le stampo (per guestMenu)
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
