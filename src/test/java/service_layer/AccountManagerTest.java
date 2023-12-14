package service_layer;

import domain_model.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import utilities.IdGenerator;
import utilities.UserType;

class AccountManagerTest {
    private static AccountManager accountManager;
    private static HotelManager hotelManager;
    private static ReservationManager reservationManager;
    private static UserDAO userDAO;
    @BeforeAll
    public static void setUp(){
        //Mi dice che se fatto con il beforeAll è da fare static
        accountManager = new AccountManager();
        reservationManager = accountManager.getReservationManager();
        hotelManager = accountManager.getHotelManager();
        userDAO = accountManager.getUserDao();
    }

    @org.junit.jupiter.api.Test
    public void registerTest() throws SQLException{
        String name = "Regino";
        String surname = "Kamberaj";
        UserType type = UserType.GUEST;
        CreditCard card = new CreditCard(name + " " + surname, "1234567890123456", "10-28", 725);
        String id = IdGenerator.generateUserID(type, name, surname);
        Guest guest = new Guest(id, name, surname, "reginokamberaj@gmail.com", "+39123456789", "password", card, reservationManager, type);
        accountManager.doRegistration(guest);
    }

    @org.junit.jupiter.api.Test
    public void loginTest() throws SQLException{
        String name = "Franco";
        String surname = "Spera";
        UserType type = UserType.HOTEL_DIRECTOR;
        HotelDirector hotelDirector = new HotelDirector(IdGenerator.generateUserID(type, name, surname), name, surname, "info@relaistiffany.it", "+393337001756", "passwordHD", hotelManager, type);
        accountManager.doRegistration(hotelDirector);

        User user = accountManager.login("info@relaistiffany.it","passwordHD");
        assert(user.getPassword().equals("passwordHD"));
        assert(user.getEmail().equals("info@relaistiffany.it"));

        userDAO.DeleteUser(hotelDirector);
    }
    @org.junit.jupiter.api.Test
    public void logout() throws SQLException{
        String name = "Franco";
        String surname = "Spera";
        UserType type = UserType.HOTEL_DIRECTOR;
        HotelDirector hotelDirector = new HotelDirector(IdGenerator.generateUserID(type, name, surname), name, surname, "info@relaistiffany.it", "+393337001756", "passwordHD", hotelManager, type);
        accountManager.doRegistration(hotelDirector);
        accountManager.logout(hotelDirector);

        userDAO.DeleteUser(hotelDirector);
    }

    @AfterAll
    public static void tearDown() {
        userDAO.disconnect();
    }
}