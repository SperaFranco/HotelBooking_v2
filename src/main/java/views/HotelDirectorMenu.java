package views;

import domain_model.Hotel;
import domain_model.HotelDirector;
import service_layer.AccountManager;
import service_layer.CalendarManager;
import service_layer.HotelManager;
import service_layer.ReservationManager;
import utilities.Research;

import java.util.Scanner;

public class HotelDirectorMenu {
    private final AccountManager accountManager;
    private HotelManager hotelManager;
    private ReservationManager reservationManager;
    private CalendarManager calendarManager;
    private HotelDirector director;
    private Hotel hotel;

    public HotelDirectorMenu(AccountManager accountManager, HotelManager hotelManager, ReservationManager reservationManager, CalendarManager calendarManager, HotelDirector director) {
        this.accountManager = accountManager;
        this.hotelManager = hotelManager;
        this.reservationManager = reservationManager;
        this.calendarManager = calendarManager;
        this.director = director;
    }

    public void startMenuDirector() {
        Scanner scanner = new Scanner(System.in);
        int choice = -1;

        while (choice != 0) {
            System.out.print("""
                    Please enter an option between 1-9:
                    1)  To add a new hotel property
                    2)  To remove the hotel
                    3)  To display the hotel calendar
                    4)  To modify a price of a room
                    5)  To make a room unavailable
                    6)  To insert the number of minimum days to stay for a room
                    7)  To display all the hotel reservations
                    8)  To add a reservation
                    9)  To modify a reservation
                    10) To remove a reservation
                    0)  To logout
                    Choice:""");
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    hotelManager.addHotel(director);
                    break;
                case 2:
                    hotelManager.removeHotel(director);
                    break;
                case 3:
                    hotel = hotelManager.chooseHotel(director);
                    break;
                case 4:
                    calendarManager.displayCalendar(hotel);
                    break;
                case 5:
                    calendarManager.modifyPrice(hotel);
                    break;
                case 6:
                    calendarManager.closeRoom(hotel);
                    break;
                case 7:
                    calendarManager.insertMinimumStay(hotel);
                    break;
                case 8:
                    reservationManager.getAllReservations(hotel);
                    break;
                case 9:
                    //Chiedere prima per l'id della prenotazione
                    Research info = hotelManager.askResearchInfo();
                    //TODO va chiesto per quale hotel e quale camera si aggiunge la prenotazione
                    //TODO si chiede per quale cliente si vuol aggiungere la prenotazione
                    // oppure si intende semplicemente chi ha inserito la prenotazione?
                    reservationManager.addReservation(null, info.getCheckIn(), info.getCheckOut(), info.getNumOfGuest(), null, null);
                    break;
                case 10:
                    reservationManager.updateReservation("19");
                    break;
                case 11:
                    reservationManager.deleteReservation("10");
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

}
