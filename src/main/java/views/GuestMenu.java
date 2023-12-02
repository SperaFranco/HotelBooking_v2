package views;

import domain_model.Guest;
import domain_model.Hotel;
import domain_model.Room;
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
                    Choice: """);
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    hotelManager.doHotelResearch(null);
                case 2:
                    //Effettuo la ricerca
                    Research research = null;//hotelManager.askResearchInfo(true);

                    //Scelgo l'hotel
                    System.out.println("Choose the hotel to book");
                    Hotel hotelChoosed = null;
                    //hotelManager.filterHotelByResearchInfo(research);

                    //Scelgo la camera
                    if(hotelChoosed != null) {
                        ArrayList<Room> roomsAvailable = hotelChoosed.getRoomsAvailable(research);
                        if (roomsAvailable.isEmpty()) {
                            //hotelManager.printRooms(roomsAvailable, hotelChoosed, research.getCheckIn(), research.getCheckOut());
                            String roomToReserve = "";//hotelManager.chooseRoom(roomsAvailable);
                            if (roomToReserve != null) {
                                String description = "due letti singoli";
                                reservationManager.createReservation(guest, research, hotelChoosed, roomToReserve,description);
                                reservationManager.createReservation(guest, research, hotelChoosed, roomToReserve,description);
                            }
                            else
                                System.out.println("Room not on the list!");
                        } else
                            System.out.println("No rooms available for hotel " + hotelChoosed.getName());
                    }
                    else
                        System.out.println("Hotel not on the list!");

                    break;
                case 3:
                    reservationManager.getReservations(guest); //le stampo direttamente
                    break;
                case 4:
                    reservationManager.getReservations(guest); //le stampo direttamente
                    reservationManager.updateReservation(null, null, null, null);
                case 5:
                    reservationManager.getReservations(guest);
                    reservationManager.deleteReservation(null);
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
