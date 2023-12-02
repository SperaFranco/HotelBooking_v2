package views;

import domain_model.Hotel;
import domain_model.HotelDirector;
import domain_model.Room;
import domain_model.Guest;
import service_layer.AccountManager;
import service_layer.CalendarManager;
import service_layer.HotelManager;
import service_layer.ReservationManager;
import utilities.Research;

import java.util.ArrayList;
import java.util.Scanner;

public class HotelDirectorMenu {
    private final AccountManager accountManager;
    private final HotelManager hotelManager;
    private final ReservationManager reservationManager;
    private final CalendarManager calendarManager;
    private HotelDirector director;
    private Hotel hotel;
    private Scanner scanner;

    public HotelDirectorMenu(AccountManager accountManager, HotelManager hotelManager,
                             ReservationManager reservationManager, CalendarManager calendarManager, HotelDirector director, Scanner scanner) {
        this.accountManager = accountManager;
        this.hotelManager = hotelManager;
        this.reservationManager = reservationManager;
        this.calendarManager = calendarManager;
        this.director = director;
        this.scanner = scanner;
    }

    public void startDirectorMenu() {
        int choice = -1;

        while (choice != 0) {
            System.out.print("""
                    Please enter an option between 1-3:
                    1)  To add a new hotel property
                    2)  To remove the hotel
                    3)  To choose an hotel
                    0)  To logout
                    Choice:""");
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    hotel = null;
                    hotelManager.addHotel(hotel);
                    secondMenuDirector(hotel);
                    break;
                case 2:
                    hotelManager.removeHotel(null);
                    System.out.println("Hotel Removed!");
                    break;
                case 3:
                    //hotel = hotelManager.chooseHotelByDirector(director);
                    secondMenuDirector(hotel);
                    break;
                case 0:
                    //Chiamo la funzione di logout?
                    accountManager.logout(director);
                    director = null; //Forse non necessario
                    break;
                default:
                    System.out.print("Not a option on the list... please try again!");
            }
        }
    }

    private void secondMenuDirector(Hotel hotel) {
        int choice = -1;

        while (choice != 0) {
            System.out.print("""
                    \nPlease enter an option between 1-8:
                    1)  To display the hotel calendar
                    2)  To modify a price of a room
                    3)  To make a room unavailable
                    4)  To insert the number of minimum days to stay for a room
                    5)  To display all the hotel reservations
                    6)  To add a reservation
                    7)  To modify a reservation
                    8)  To remove a reservation
                    0)  To return at the start menu
                    Choice:""");
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    //calendarManager.displayCalendar(hotel);
                    break;
                case 2:
                    calendarManager.modifyPrice(hotel, null, null, 100.00);
                    break;
                case 3:
                    calendarManager.closeRoom(hotel, null, null);
                    break;
                case 4:
                    calendarManager.insertMinimumStay(hotel, null, null, 1);
                    break;
                case 5:
                    reservationManager.getAllReservations(hotel);
                    break;
                case 6:
                    //Chiedo info
                    Research info = null;

                    //Inserisco i dati dell'utente
                    System.out.println("Please enter all the client information's:");
                    Guest guest = accountManager.addGuestWithoutAccount();

                    //Scelgo la camera da prenotare
                    ArrayList<Room> roomsAvailable = hotel.getRoomsAvailable(info);
                    if (!roomsAvailable.isEmpty()) {
                        //hotelManager.printRooms(roomsAvailable, hotel, info.getCheckIn(), info.getCheckOut());
                        String roomID = "";//hotelManager.chooseRoom(roomsAvailable);
                        if (roomID != null){
                            String description= "due letti singoli";
                            reservationManager.createReservation(guest, info, hotel, roomID, description);
                        }
                        else
                            System.out.println("Room not on the list!");
                    }else
                        System.out.println("No room available for this hotel!");
                    break;
                case 7:
                    reservationManager.getAllReservations(hotel); //le ristampa
                    reservationManager.updateReservation(null, null, null,null);
                    break;
                case 8:
                    reservationManager.getAllReservations(hotel);
                    reservationManager.deleteReservation(null);
                    break;
                case 0:
                    System.out.println("Returning to the starting menu...");
                    break;
                default:
                    System.out.print("Not a option on the list... please try again!");
            }

        }

    }

}
