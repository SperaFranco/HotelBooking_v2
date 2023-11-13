package service_layer;

import domain_model.User;
import domain_model.Guest;
import domain_model.HotelDirector;
import utilities.IdGenerator;
import utilities.UserType;

import java.util.ArrayList;
import java.util.Scanner;

public class AccountManager {
    ArrayList<User> users = new ArrayList<>();

    public User doRegistration() {
        Scanner scanner = new Scanner(System.in);
        String userType = null;
        User newUser = null;

        System.out.println("Are you a \"guest\" or a \"hotel manager\"?");
        System.out.print("I am a:");
        userType = scanner.nextLine();

        if (userType.equalsIgnoreCase("guest"))
            newUser = addGuest(UserType.GUEST);
        else if (userType.contains("hotel") || userType.contains("manager")) {
            newUser = addHotelDirector(UserType.HOTEL_DIRECTOR);
        }
        else {
            System.out.println("Please try again your registration...");
        }
        scanner.close();
        return newUser;
    }
    public User login() {
        //TODO ricordarsi di fare il close del resultset
        int maxAttemps = 5;
        Scanner scanner = new Scanner(System.in);
        User loginUser = null;
        String email = null;
        for(int attempt = 1; attempt <= maxAttemps; attempt++) {
            try {

                if(email == null) {
                    System.out.print("Please enter your email: ");
                    email = scanner.nextLine();
                }
                //Se è il primo tentativo (o l'utente non è stato ancora trovato) allora cercalo
                if(attempt == 1 || loginUser == null) {
                    loginUser = findUserByEmail(email);

                    if (loginUser == null) {
                        System.out.println("User not found! Please try again.");
                        email = null;
                    }
                }

                //se Trova l'utente, stampane il nome e salva da qualche parte la password
                if (loginUser != null) {
                    System.out.print("Hello " + loginUser.getName() + "!\nPlease enter your password:");
                    String password = scanner.nextLine();
                    if (password.equals(loginUser.getPassword())) {
                        //se password coincidono ritorna l'utente
                        return loginUser;
                    } else {
                        System.out.println("Incorrect password... please try again!");
                    }
                }

            }catch (Exception e) {
                e.printStackTrace();
            }
        }

        System.out.println("Too many unsuccessful login attempts... exiting");
        return null; //se entro 5 tentativi non lo trovo restituisco null
    }
    public void logout() {
        //TODO vedere se utile
    }
    public Guest addGuest(UserType guest) {
        //Dopo aver richiesto tutte le credenziali di cui si ha bisogno
        //si inseriscono nella stringa sql
        //e si esegue la query
        String[] info = askUserInfo();
        //va aggiunta la carta di credito
        Guest newGuest = new Guest(IdGenerator.generateUserID(guest, info[0],info[1]),
                info[0], info[1], info[2],"", info[3], null, "");
        users.add(newGuest);
        return newGuest;
    }
    public HotelDirector addHotelDirector(UserType hotelDirector) {
        //l'hotel director ha in più un l'arraylist di hotel
        String[] info = askUserInfo();
        HotelDirector newHotelDirector = new HotelDirector(IdGenerator.generateUserID(hotelDirector, info[0], info[1]),
                info[0], info[1], info[2],"",info[3]);
        users.add(newHotelDirector);
        return newHotelDirector;
    }
    private String[] askUserInfo() {
        //TODO al momento non chiedo di inserire il numero di telefono
        String name, surname, email, telephone, password;
        Scanner scanner = new Scanner(System.in);

        System.out.print("Please enter your name:");
        name = scanner.nextLine();
        System.out.print("Please enter your surname:");
        surname = scanner.nextLine();
        System.out.print("Please enter your email:");
        email = scanner.nextLine();
        System.out.print("Please enter your password:");
        password = scanner.nextLine();

        scanner.close();
        return new String[]{name, surname, email, password};
    }
    private User findUserByEmail(String email) {
        for (User user:users) {
            if (user.getEmail().equalsIgnoreCase(email))
                return user;
        }
        return null;
    }

}
