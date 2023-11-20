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
    Scanner scanner;
    public AccountManager(Scanner scanner){
        this.scanner = scanner;
    }
    public User doRegistration() {
        String userType = null;
        User newUser = null;

        System.out.println("Are you a \"guest\" or a \"hotel manager\"?");
        System.out.print("I am a:");
        userType = scanner.nextLine();

        if (userType.equalsIgnoreCase("guest"))
            newUser = addGuest();
        else if (userType.contains("hotel") || userType.contains("manager")) {
            newUser = addHotelDirector();
        }
        else {
            System.out.println("Please try again your registration...");
        }
        return newUser;
    }
    public User login() {
        //TODO ricordarsi di fare il close del resultset
        int maxAttemps = 5;
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
                System.out.println("Error occured: " + e.getMessage());
            }
        }

        System.out.println("Too many unsuccessful login attempts... exiting");
        return null; //se entro 5 tentativi non lo trovo restituisco null
    }
    public void logout(User user) {
        //TODO vedere se problematica
        //Potrei ad esempio risettare l'user a null
        users.remove(user);
        user = null;
    }

    //Region helper methods
    public Guest addGuest() {
        //Dopo aver richiesto tutte le credenziali di cui si ha bisogno
        //si inseriscono nella stringa sql
        //e si esegue la query
        Guest newGuest = (Guest)createUser(UserType.GUEST);
        if (newGuest != null)
            users.add(newGuest);
        return newGuest;
    }

    public Guest addGuestWithoutAccount() {
        String name, surname, telephone;
        System.out.print("Name:");
        name = scanner.nextLine();
        System.out.print("Surname:");
        surname = scanner.nextLine();
        System.out.print("Telephone:");
        telephone = scanner.nextLine();

        Guest newGuest = new Guest(IdGenerator.generateUserID(UserType.GUEST, name, surname), name, surname, telephone);
        users.add(newGuest);
        return newGuest;
    }
    private HotelDirector addHotelDirector() {
        //l'hotel director ha in più un l'arraylist di hotel
        HotelDirector newHotelDirector = (HotelDirector)createUser(UserType.HOTEL_DIRECTOR);
        if (newHotelDirector != null)
            users.add(newHotelDirector);
        return newHotelDirector;
    }
    private User createUser(UserType type) {
        //TODO al momento non chiedo di inserire il numero di telefono
        String name, surname, email, telephone, password;

        System.out.print("Please enter your name:");
        name = scanner.nextLine();
        System.out.print("Please enter your surname:");
        surname = scanner.nextLine();
        System.out.print("Please enter your email:");
        email = scanner.nextLine();
        System.out.print("Please enter your password:");
        password = scanner.nextLine();


        if (type == UserType.GUEST) {
            //TODO gli va chiesta la carta
            return new Guest(IdGenerator.generateUserID(type, name, surname),
                    name, surname, email,"", password, null, "");
        }
        else if (type == UserType.HOTEL_DIRECTOR) {
            return new HotelDirector(IdGenerator.generateUserID(type, name, surname),
                    name, surname, email,"",password);
        }
        return null;
    }
    private User findUserByEmail(String email) {
        for (User user:users) {
            if (user.getEmail().equalsIgnoreCase(email))
                return user;
        }
        return null;
    }
    //end Region

}
