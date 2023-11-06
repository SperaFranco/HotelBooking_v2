package service_layer;

import domain_model.User;
import domain_model.Guest;
import domain_model.HotelDirector;

import java.util.Scanner;

public class AccountManager {

    private void addUser(User user) {
        //In questo metodo si effettua la registrazione dell'utente

        if(user instanceof Guest) {
            //salvalo come Guest (statement setString per la tipologia)
        }
        else if(user instanceof HotelDirector) {
            //salvalo come hotelDirector
        }
    }

    public void addGuest(Guest guest) {
        //Dopo aver richiesto tutte le credenziali di cui si ha bisogno
        //si inseriscono nella stringa sql
        //e si esegue la query

        //va aggiunta la carta di credito
        addUser(guest);
    }

    public void addHotelDirector(HotelDirector director) {
        //l'hotel director ha in più un l'arraylist di hotel
        addUser(director);
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

}
