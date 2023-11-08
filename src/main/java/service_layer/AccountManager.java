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



    public void addGuest(UserType guest) {
        //Dopo aver richiesto tutte le credenziali di cui si ha bisogno
        //si inseriscono nella stringa sql
        //e si esegue la query
        String[] info = askUserInfo();
        //va aggiunta la carta di credito
        Guest newGuest = new Guest(IdGenerator.generateUserID(guest, info[0],info[1]),
                info[0], info[1], info[2],"", info[3], null, "");
        users.add(newGuest);
    }



    public void addHotelDirector(UserType hotelDirector) {
        //l'hotel director ha in più un l'arraylist di hotel
        String[] info = askUserInfo();
        HotelDirector newHotelDirector = new HotelDirector(IdGenerator.generateUserID(hotelDirector, info[0], info[1]),
                info[0], info[1], info[2],"",info[3]);
        users.add(newHotelDirector);
    }

    public User login() {
        //TODO ricordarsi di fare il close del resultset
        //Richiedi email
        String email, password;
        User loginUser = null;
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter you email: ");
        email = scanner.nextLine();

        //se Trova l'utente, stampane il nome e salva da qualche parte la password
        System.out.println("Hello " + /*nome utente*/ "!\nPlease enter your password:");
        password = scanner.nextLine();

        //se password coincidono continua
        //Creo il nuovo oggetto a seconda del tipo
        return loginUser; //nel caso il loginUser rimane a null non ho trovato nessun utente
    }

    public void logout() {
        //TODO vedere se utile
    }

    private void addUser(User user) {
        //In questo metodo si effettua la registrazione dell'utente

        if(user instanceof Guest) {
            //salvalo come Guest (statement setString per la tipologia)
        }
        else if(user instanceof HotelDirector) {
            //salvalo come hotelDirector
        }
    }

    private String[] askUserInfo() {
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

        return new String[]{name, surname, email, password};
    }
}
