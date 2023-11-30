package views;

import domain_model.Guest;
import domain_model.HotelDirector;
import domain_model.User;
import service_layer.AccountManager;
import service_layer.CalendarManager;
import service_layer.HotelManager;
import service_layer.ReservationManager;

import java.util.Scanner;

public class StartMenu {
    //Region Controllers
    private final AccountManager accountManager;

    private Scanner scanner;

    public StartMenu() {
        this.scanner = new Scanner(System.in);
        accountManager = new AccountManager();
    }


    public void startMenu() {
        //Alla richiesta del crea nuovo account
        // chiedi inizialmente se è un cliente oppure se è un gestore
        // nel caso di gestore faremo in modo che si passi subito all'inserimento di una struttura(?)
        int choice = -1;
        User user = null;

        while(choice != 0) {
            System.out.print("""
                    Welcome to the new Hotel Booking System!
                    Please enter an option below:
                    1) To search for a hotel\s
                    2) To create a new account\s
                    3) To login
                    0) To exit
                    Choice:""");
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    accountManager.getHotelManager().doHotelResearch(user);
                    break;
                case 2:
                    //Dalla registrazione si torna indietro oppure si va direttamente ai menu?
                    //user = accountManager.doRegistration();
                    IdentifyUser(user);
                    break;
                case 3:
                    user = accountManager.login("email@email.it", "password");
                    IdentifyUser(user);
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

    private void IdentifyUser(User user) {

        if(user instanceof Guest) {
            //Fai partire il guestMenu
            GuestMenu guestMenu = new GuestMenu(accountManager, accountManager.getHotelManager(),
                    accountManager.getReservationManager(), (Guest) user, scanner);
            guestMenu.startGuestMenu();
        }
        else if(user instanceof HotelDirector) {
            //endregion
            HotelDirectorMenu hotelDirectorMenu = new HotelDirectorMenu(accountManager, accountManager.getHotelManager(),
                    accountManager.getReservationManager(), accountManager.getCalendarManager(), (HotelDirector) user, scanner);
            hotelDirectorMenu.startDirectorMenu();
        }
    }


}
