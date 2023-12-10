package service_layer;

import domain_model.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import service_layer.AccountManager;
import utilities.IdGenerator;
import utilities.UserType;

class AccountManagerTest {
    private static AccountManager accountManager;
    private static HotelDirector hotelDirector;

    @BeforeAll
    public static void setUp(){
        //Mi dice che se fatto con il beforeAll è da fare static
        accountManager = new AccountManager();
        String name = "Franco";
        String surname = "Spera";
        UserType type = UserType.HOTEL_DIRECTOR;
        hotelDirector = new HotelDirector(IdGenerator.generateUserID(type, name, surname), name, surname, "info@relaistiffany.it", "+393337001756", "passwordHD", accountManager.getHotelManager(), type);
        accountManager.doRegistration(hotelDirector);
    }

    @org.junit.jupiter.api.Test
    public void registerHotelDirector(){
        //Forse non serve testare la funzione
        String name = "Regino";
        String surname = "Kamberaj";
        UserType type = UserType.GUEST;
        CreditCard card = new CreditCard("Regino Kamberaj", "1234567890123456", "10-28", 725);
        Guest guest = new Guest(IdGenerator.generateUserID(type, name, surname), name, surname, "reginokamberaj@gmail.com", "+39123456789", "password", card, accountManager.getReservationManager(), type);
        accountManager.doRegistration(guest);
    }

    @org.junit.jupiter.api.Test
    public void loginTest(){
        User user = accountManager.login("info@relaistiffany.it","passwordHD");
        assert(user.getPassword().equals("passwordHD"));
        assert(user.getEmail().equals("info@relaistiffany.it"));
    }
    @org.junit.jupiter.api.Test
    public void logout(){
        accountManager.logout(hotelDirector);
    }

    @AfterAll
    public static void tearDown() {
        accountManager.disconnectFromDatabase(); //vedere se va bene
    }
}