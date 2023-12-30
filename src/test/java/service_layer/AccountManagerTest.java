package service_layer;

import domain_model.*;
import org.junit.jupiter.api.*;
import utilities.IdGenerator;
import utilities.UserType;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.jupiter.api.Assertions.assertNull;

class AccountManagerTest {
    private static AccountManager accountManager;
    @BeforeAll
    static void setUp(){
        accountManager = new AccountManager();
    }

    @Test
    void findUserByIDTest(){
        String name = "Regino";
        String surname = "Kamberaj";
        UserType type = UserType.GUEST;
        CreditCard card = new CreditCard(name + " " + surname, "1234567890123456", "10-28", 725);
        String id = IdGenerator.generateUserID(type, name, surname);
        Guest guest = new Guest(id, name, surname, "reginokamberaj@gmail.com", "+39123456789", "password", card, type);
        accountManager.doRegistration(guest);
        Guest guestInDB = (Guest)accountManager.findUserByID(id);
        assertThat(guestInDB.getId(), equalTo(id));
        accountManager.deleteUser(guest);
    }

    @Test
    void deleteUserTest(){
        String name = "Marco";
        String surname = "Marchi";
        UserType type = UserType.GUEST;
        CreditCard card = new CreditCard(name + " " + surname, "1234567890123456", "10-28", 725);
        String id = IdGenerator.generateUserID(type, name, surname);
        Guest guest = new Guest(id, name, surname, "marcomarchi@gmail.com", "+39123456789", "password", card, type);
        accountManager.doRegistration(guest);
        accountManager.deleteUser(guest);
        Guest guestInDB = (Guest)accountManager.findUserByID(id);
        assertNull(guestInDB);
    }

    @Test
    void loginTest() {
        String name = "Franco";
        String surname = "Spera";
        UserType type = UserType.HOTEL_DIRECTOR;
        HotelDirector hotelDirector = new HotelDirector(IdGenerator.generateUserID(type, name, surname), name, surname, "info@relaistiffany.it", "+393337001756", "passwordHD", type);
        accountManager.doRegistration(hotelDirector);

        User user = accountManager.login("info@relaistiffany.it","passwordHD");
        assert(user.getPassword().equals("passwordHD"));
        assert(user.getEmail().equals("info@relaistiffany.it"));

        accountManager.deleteUser(hotelDirector);
    }
    @Test
    void logout() {
        String name = "Franco";
        String surname = "Spera";
        UserType type = UserType.HOTEL_DIRECTOR;
        HotelDirector hotelDirector = new HotelDirector(IdGenerator.generateUserID(type, name, surname), name, surname, "info@relaistiffany.it", "+393337001756", "passwordHD", type);
        accountManager.doRegistration(hotelDirector);
        accountManager.logout(hotelDirector);

        accountManager.deleteUser(hotelDirector);
    }

    @AfterAll
    static void tearDown() {
        accountManager.getUserDao().disconnect();
        accountManager = null;
    }
}