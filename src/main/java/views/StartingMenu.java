package views;

import domain_model.User;
import service_layer.AccountManager;
import service_layer.HotelManager;
import utilities.UserType;

import java.util.Scanner;

public class StartingMenu {
    //qui ci saranno i vari controllers
    private AccountManager accountManager;
    private HotelManager hotelManager;
    public StartingMenu() {
        accountManager = new AccountManager();
        hotelManager = new HotelManager();
    }


    public void startMenu() {
        //Alla richiesta del crea nuovo account
        // chiedi inizialmente se è un cliente oppure se è un gestore
        // nel caso di gestore faremo in modo che si passi subito all'inserimento di una struttura(?)
        Scanner scanner = new Scanner(System.in);
        int choice = 0;
        String userType = null;
        User user = null;

        while(choice != 4) {
            System.out.println("Welcome to the new Hotel Booking System!\n\n" +
                    "Please enter a option below:" +
                    "\n1) To search for a hotel " +
                    "\n2) To create a new account " +
                    "\n3) To login" +
                    "\n4) To exit" +
                    "\nChoice:");
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    hotelManager.doHotelReseach(user);
                    break;
                case 2:
                    //Dalla registrazione si torna indietro oppure si va direttamente ai menu?
                    System.out.println("Are you a guest or a hotel manager?");
                    System.out.print("I am a:");
                    userType = scanner.nextLine();
                    if (userType.contains("guest"))
                        accountManager.addGuest(UserType.GUEST);
                    else if (userType.contains("hotel") || userType.contains("manager")) {
                        accountManager.addHotelDirector(UserType.HOTEL_DIRECTOR);
                    }
                    else {
                        System.out.println("Please try again your registration...");
                    }
                    break;
                case 3:

            }
        }

    }


}
