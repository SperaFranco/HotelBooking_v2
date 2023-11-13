package views;

import domain_model.Guest;
import domain_model.HotelDirector;
import domain_model.Reservation;
import domain_model.User;
import service_layer.AccountManager;
import service_layer.HotelManager;
import service_layer.ReservationManager;
import utilities.UserType;

import java.util.Scanner;

public class StartingMenu {
    //qui ci saranno i vari controllers
    //Region Controllers
    private AccountManager accountManager;
    private HotelManager hotelManager;
    private ReservationManager reservationManager;
    //endregion
    private HotelDirectorMenu hotelDirectorMenu;
    private GuestMenu guestMenu;

    public StartingMenu() {
        accountManager = new AccountManager();
        reservationManager = new ReservationManager();
        hotelManager = new HotelManager(accountManager, reservationManager);
    }


    public void startMenu() {
        //Alla richiesta del crea nuovo account
        // chiedi inizialmente se è un cliente oppure se è un gestore
        // nel caso di gestore faremo in modo che si passi subito all'inserimento di una struttura(?)
        Scanner scanner = new Scanner(System.in);
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
                        //Fai partire l'hotelDirectorMenu
                        hotelDirectorMenu = new HotelDirectorMenu(hotelManager, reservationManager,(HotelDirector) user);
                        hotelDirectorMenu.startMenu();
                    }
                    break;
                case 3:
                    user = accountManager.login();

                    if(user instanceof Guest) {
                        //Fai partire il guestMenu
                    }
                    else if(user instanceof HotelDirector) {
                        //Fai partire l'hotelDirectorMenu
                    }
                    break;
                case 4:
                    //Qui esco semplicemente
                    break;

                default:
                    System.out.println("Please one of the options in the list!");
            }
        }

    }


}
