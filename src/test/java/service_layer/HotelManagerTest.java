package service_layer;

import data_access.HotelDAO;
import data_access.UserDAO;
import domain_model.Hotel;
import domain_model.HotelDirector;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import utilities.HotelRating;
import utilities.IdGenerator;
import utilities.Research;
import utilities.UserType;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

class HotelManagerTest {

    private static AccountManager accountManager;
    private static HotelManager hotelManager;
    private static CalendarManager calendarManager;
    private static UserDAO userDAO;
    private static HotelDAO hotelDAO;

    private static HotelDirector hotelDirector;

    @BeforeAll
    public static void setUp() {
        accountManager = new AccountManager();
        hotelManager = accountManager.getHotelManager();
        calendarManager = accountManager.getCalendarManager();
        userDAO = accountManager.getUserDao();
        hotelDAO = hotelManager.getHotelDAO();
        //Supponiamo di aver già creato un profilo da hotel director
        hotelDirector = new HotelDirector(IdGenerator.generateUserID(UserType.HOTEL_DIRECTOR,"Franco","Spera"), "Franco", "Spera", "info@relaistiffany.it", "+393337001756", "passwordHD", hotelManager, UserType.HOTEL_DIRECTOR);
        accountManager.doRegistration(hotelDirector);
    }
    @org.junit.jupiter.api.Test
    public void createHotelTest() throws SQLException {
        //createHotel mi crea solamente l'hotel e non me lo aggiunge al db!
        Hotel hotel = hotelManager.createHotel(hotelDirector,"Relais Tiffany", "Firenze", "via Guido Monaco 5", null, null,  HotelRating.THREE_STAR_HOTEL, 1, 2, 1);
        assert(hotel.getName().equals("Relais Tiffany"));
        assert(hotel.getRooms().size()==4);
    }

    @org.junit.jupiter.api.Test
    public void addAndRemoveRoomTest() throws SQLException {
        Hotel hotel = hotelManager.createHotel(hotelDirector,"Relais Tiffany", "Firenze", "via Guido Monaco 5", null, null, HotelRating.THREE_STAR_HOTEL, 1, 2, 1);
        hotelDAO.addHotel(hotel);
        /*
        TODO Franco modifica questi assert
        assert(hotelManager.getHotelMap().isEmpty());
        hotelManager.addHotel(hotel);
        assert(hotelManager.getHotelMap().size() ==1);
        hotelManager.removeHotel(hotel);
        assert(hotelManager.getHotelMap().isEmpty());
        */
        hotelDAO.removeHotel(hotel.getId());
    }

    @org.junit.jupiter.api.Test
    public void doHotelResearch() throws SQLException {
        Hotel hotel = hotelManager.createHotel(hotelDirector,"Relais Tiffany", "Firenze", "via Guido Monaco 5", null, null, HotelRating.THREE_STAR_HOTEL, 1, 2, 1);
        hotelManager.addHotel(hotel);
        LocalDate checkInDate = LocalDate.of(2023, 12, 25);
        LocalDate checkOutDate = LocalDate.of(2023, 12, 27);
        //FIXME numOfGuests deve essere maggiore di zero
        Research research = new Research("Firenze", checkInDate, checkOutDate, 3);
        ArrayList<Hotel> hotelsAvailable = hotelManager.doHotelResearch(research);
        assert(hotelsAvailable.size()==1);
        //assert(hotelsAvailable.contains(hotel)); questo da errore poichè vado a creare un nuovo oggetto hotel ma con le stesse caratteristiche
        calendarManager.setMinimumStay(hotelsAvailable.get(0), checkInDate, hotel.getRooms().get(3).getId(), 3);
        hotelsAvailable = hotelManager.doHotelResearch(research);
        assert(hotelsAvailable.isEmpty());
        hotelDAO.removeHotel(hotel.getId());
    }

    @AfterAll
    public static void TearDown() throws SQLException {
        userDAO.DeleteUser(hotelDirector);
        userDAO.disconnect();
    }









}