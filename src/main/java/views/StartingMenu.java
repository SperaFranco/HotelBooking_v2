package views;

import domain_model.Guest;
import domain_model.HotelDirector;
import domain_model.User;
import service_layer.AccountManager;
import service_layer.CalendarManager;
import service_layer.HotelManager;
import service_layer.ReservationManager;

import java.util.Scanner;

public class StartingMenu {
    //Region Controllers
    private AccountManager accountManager;
    private HotelManager hotelManager;
    private ReservationManager reservationManager;
    private CalendarManager calendarManager;
    //endregion
    private HotelDirectorMenu hotelDirectorMenu;
    private GuestMenu guestMenu;
    private Scanner scanner;

    public StartingMenu() {
        this.scanner = new Scanner(System.in);
        accountManager = new AccountManager(scanner);
        reservationManager = new ReservationManager(scanner, accountManager); //le prenotazioni sono condivise fra guest e hoteldirector
        calendarManager = new CalendarManager(scanner);
        hotelManager = new HotelManager(reservationManager, calendarManager, scanner);
    }


    public void startMenu() {
        //Alla richiesta del crea nuovo account
        // chiedi inizialmente se è un cliente oppure se è un gestore
        // nel caso di gestore faremo in modo che si passi subito all'inserimento di una struttura(?)
        int choice = 0;
        User user = null;

        while(choice != 4) {
            System.out.print("""
                    Welcome to the new Hotel Booking System!
                    Please enter an option below:
                    1) To search for a hotel\s
                    2) To create a new account\s
                    3) To login
                    4) To exit
                    Choice:""");
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    hotelManager.doHotelResearch(user);
                    break;
                case 2:
                    //Dalla registrazione si torna indietro oppure si va direttamente ai menu?
                    user = accountManager.doRegistration();

                    if(user instanceof Guest) {
                        //Fai partire il guestMenu
                    }
                    else if(user instanceof HotelDirector) {
                        //Fai partire l'hotelDirectorMenu --> il director fa le sue azioni tramite il suo menu
                        hotelDirectorMenu = new HotelDirectorMenu(accountManager, hotelManager, reservationManager, calendarManager,(HotelDirector) user);
                        hotelDirectorMenu.startMenuDirector();
                    }
                    break;
                case 3:
                    user = accountManager.login();

                    if(user instanceof Guest) {
                        //Fai partire il guestMenu
                    }
                    else if(user instanceof HotelDirector) {
                        hotelDirectorMenu = new HotelDirectorMenu(accountManager, hotelManager, reservationManager, calendarManager,(HotelDirector) user);
                        hotelDirectorMenu.startMenuDirector();
                    }
                    break;
                case 4:
                    //Qui esco semplicemente
                    break;

                default:
                    System.out.println("Please select one of the options in the list!");
            }
        }
    }

    public void closeScanner() {
        if (scanner != null)
            scanner.close();
    }


}
