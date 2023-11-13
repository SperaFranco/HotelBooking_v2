package views;

import domain_model.HotelDirector;
import domain_model.User;
import service_layer.CalendarManager;
import service_layer.HotelManager;
import service_layer.ReservationManager;

import java.util.Scanner;

public class HotelDirectorMenu {
    private HotelManager hotelManager;
    private ReservationManager reservationManager;
    private CalendarManager calendarManager;

    private HotelDirector director;
    public HotelDirectorMenu(HotelManager hotelManager, ReservationManager reservationManager, HotelDirector director) {
        this.hotelManager = hotelManager;
        this.reservationManager = reservationManager;
        this.director = director;
        calendarManager = new CalendarManager();
    }

    public void startMenu() {
        Scanner scanner = new Scanner(System.in);
        int choice = -1;

        while (choice != 0) {
            System.out.print("""
                    Please enter an option between 1-4:
                    1) To add a new hotel property\s
                    2) To remove the hotel
                    3) To display the hotel calendar\s
                    4) To display all the hotel reservations
                    5) To remove a reservation
                    6) To modify a reservation
                    0) To logout
                    Choice:""");
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    hotelManager.addHotel(director);
                    break;
                case 2:
                    //TODO qui ci va il metodo del calendar manager
                    break;
                case 3:
                    break;
                case 4:
                    reservationManager.getAllReservations(director);
                    break;
                case 5:
                    //Chiedere prima per l'id della prenotazione
                    reservationManager.deleteReservation("10");
                    break;
                case 6:
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
