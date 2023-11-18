package views;

import domain_model.HotelDirector;
import service_layer.CalendarManager;
import service_layer.HotelManager;
import service_layer.ReservationManager;

import java.util.Scanner;

public class HotelDirectorMenu {
    private HotelManager hotelManager;
    private ReservationManager reservationManager;

    private CalendarManager calendarManager;
    private HotelDirector director;
    public HotelDirectorMenu(HotelManager hotelManager, ReservationManager reservationManager, CalendarManager calendarManager, HotelDirector director) {
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
                    1) To add a new hotel property
                    2) To remove the hotel
                    3) To display the hotel calendar
                    4) To modify a price of a room
                    5) To make a room unavailable
                    6) To insert the number of minimum days to stay for a room
                    7) To display all the hotel reservations
                    8) To remove a reservation
                    9) To modify a reservation
                    0) To logout
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
                    calendarManager.displayCalendar();
                    break;
                case 4:
                    calendarManager.modifyPrice();
                    break;
                case 5:
                    calendarManager.closeRoom();
                    break;
                case 6:
                    calendarManager.insertMinimumStay();
                    break;
                case 7:
                    reservationManager.getAllReservations(director);
                    break;
                case 8:
                    //Chiedere prima per l'id della prenotazione
                    reservationManager.deleteReservation("10");
                    break;
                case 9:
                    //uguale qui
                    reservationManager.updateReservation("19");
                case 0:
                    //Chiamo la funzione di logout?
                default:
                    System.out.print("Not a option on the list... please try again!");
            }
        }
    }

}
