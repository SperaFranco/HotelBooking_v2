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

    String guestName, guestSurname, hotelDirectorName, hotelDirectorSurname;
    UserType guestType, hotelDirectorType;
    CreditCard creditCard;
    String guestID, hotelDirectorID;
    HotelDirector hotelDirector;
    Guest guest;

    @BeforeAll
    static void beforeAll(){
        accountManager = new AccountManager();
    }

    @BeforeEach
    public void setUp(){

        // creo un Guest
        guestName = "Regino";
        guestSurname = "Kamberaj";
        guestType = UserType.GUEST;
        creditCard = new CreditCard(guestName + " " + guestSurname, "1234567890123456", "10-28", 725);
        guestID = IdGenerator.generateUserID(guestType, guestName, guestSurname);
        guest = new Guest(guestID, guestName, guestSurname, "reginokamberaj@gmail.com", "+39123456789", "password", creditCard, guestType);

        // creo un HotelDirector
        hotelDirectorName = "Franco";
        hotelDirectorSurname = "Spera";
        hotelDirectorType = UserType.HOTEL_DIRECTOR;
        hotelDirectorID = IdGenerator.generateUserID(hotelDirectorType, hotelDirectorName, hotelDirectorSurname);
        hotelDirector = new HotelDirector(hotelDirectorID, hotelDirectorName, hotelDirectorSurname, "franco.spera@edu.unifi.it", "012932-122832", "HDpassword", hotelDirectorType);
    }

    @Disabled
    @Test
     void doGuestRegistrationTest(){
        accountManager.doRegistration(guest);
    }

    @Disabled
    @Test
    void doHotelDirectorRegistrationTest(){
        accountManager.doRegistration(hotelDirector);
    }

    @Test
    void findUserByIDTest(){
        accountManager.doRegistration(guest);
        Guest guestInDB = (Guest)accountManager.findUserByID(guestID);
        assertThat(guestInDB.getId(), equalTo(guestID));
        accountManager.deleteUser(guest);
    }

    @Test
    void deleteUserTest(){
        accountManager.doRegistration(guest);
        accountManager.deleteUser(guest);
        Guest guestInDB = (Guest)accountManager.findUserByID(guestID);
        assertNull(guestInDB);
    }

    @Test
    void loginTest() {
        accountManager.doRegistration(hotelDirector);
        User user = accountManager.login("franco.spera@edu.unifi.it","HDpassword");
        assertThat(user.getPassword(), equalTo("HDpassword"));
        assertThat(user.getEmail(), equalTo("franco.spera@edu.unifi.it"));
        accountManager.deleteUser(hotelDirector);
    }

}