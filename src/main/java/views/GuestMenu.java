package views;

import domain_model.Guest;
import domain_model.Hotel;
import domain_model.Reservation;
import service_layer.AccountManager;
import service_layer.HotelManager;
import service_layer.ReservationManager;
import utilities.Research;

import java.util.ArrayList;
import java.util.Scanner;

public class GuestMenu {
    private Scanner scanner;
    private AccountManager accountManager;
    private HotelManager hotelManager;
    private ReservationManager reservationManager;
    private Guest guest;

    public GuestMenu(AccountManager accountManager,HotelManager hotelManager, ReservationManager reservationManager,
                     Guest guest, Scanner scanner){
        this.accountManager = accountManager;
        this.hotelManager = hotelManager;
        this.guest = guest;
        this.scanner = scanner;
    }
    public void startGuestMenu() {
        int choice = -1;

        while (choice != 0) {
            System.out.print("""
                    Please enter an option between 1-3:
                    1) To do a hotel research
                    2) To add a reservation
                    3) To get all reservations
                    4) To modify a reservation
                    0)  To logout
                    Choice:""");
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    hotelManager.doHotelResearch(guest); //TODO dividere fra fare una ricerca e aggiungere prenotazione
                case 2:
                    Research research = hotelManager.askResearchInfo(true);
                    System.out.println("Choose the hotel to book");
                    ArrayList<Hotel> hotels = hotelManager.filterHotels(research.getCity(), research.getCheckIn(), research.getCheckOut(), research.getNumOfGuest(), 0);
                    for (Hotel hotel : hotels) {
                        int i = 1;
                        hotel.printHotelInfo(i++);
                    }
                    int hotelChoice = scanner.nextInt();

                    String roomID = reservationManager.chooseRoomToReserve(hotels.get(hotelChoice), research.getCheckIn(), research.getCheckOut());
                    if (roomID != null)
                        reservationManager.addReservation(guest, research.getCheckIn(), research.getCheckOut(), research.getNumOfGuest(), hotels.get(hotelChoice).getId(), roomID);
                    break;
                case 3:
                    reservationManager.getReservations(guest); //le stampo direttamente
                    break;
                case 4:
                    reservationManager.getReservations(guest); //le stampo direttamente
                    reservationManager.updateReservation();
                case 5:
                    reservationManager.getReservations(guest);
                    reservationManager.deleteReservation();
                case 0:
                    //Chiamo la funzione di logout?
                    accountManager.logout(guest);
                    guest = null;
                    break;
                default:
                    System.out.print("Not a option on the list... please try again!");
            }
        }
    }
}
