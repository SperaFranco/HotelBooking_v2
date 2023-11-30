package service_layer;

import domain_model.Guest;
import domain_model.HotelDirector;
import domain_model.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import utilities.UserType;

import static org.junit.jupiter.api.Assertions.*;

class AccountManagerTest {

    @BeforeEach
    public void setUp(){
    }

    @org.junit.jupiter.api.Test
    public void registerHotelDirector(){
        AccountManager accountManager = new AccountManager();
        HotelDirector hotelDirector = new HotelDirector("HD_RelaisTiffany_001", "Franco", "Spera", "info@relaistiffany.it", "+393337001756", "passwordHD", accountManager.getHotelManager(), UserType.HOTEL_DIRECTOR);
        accountManager.doRegistration(hotelDirector);
    }
    @org.junit.jupiter.api.Test
    public void loginTest(){
        AccountManager accountManager = new AccountManager();
        HotelDirector hotelDirector = new HotelDirector("HD_RelaisTiffany_001", "Franco", "Spera", "info@relaistiffany.it", "+393337001756", "passwordHD", accountManager.getHotelManager(), UserType.HOTEL_DIRECTOR);
        accountManager.doRegistration(hotelDirector);
        User user = accountManager.login("info@relaistiffany.it","passwordHD");
        assert(user.getPassword().equals("passwordHD"));
        assert(user.getEmail().equals("info@relaistiffany.it"));
    }
    @org.junit.jupiter.api.Test
    public void logout(){
        AccountManager accountManager = new AccountManager();
        HotelDirector hotelDirector = new HotelDirector("HD_RelaisTiffany_001", "Franco", "Spera", "info@relaistiffany.it", "+393337001756", "passwordHD", accountManager.getHotelManager(), UserType.HOTEL_DIRECTOR);
        accountManager.doRegistration(hotelDirector);
        accountManager.logout(hotelDirector);
    }
}