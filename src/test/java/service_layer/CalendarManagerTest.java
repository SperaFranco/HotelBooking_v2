package service_layer;

import domain_model.*;
import org.junit.jupiter.api.*;
import utilities.HotelRating;
import utilities.IdGenerator;
import utilities.Research;
import utilities.UserType;

import java.time.LocalDate;
import java.util.ArrayList;

class CalendarManagerTest {

    // managers
    private static AccountManager accountManager;
    private static HotelManager hotelManager;
    private static CalendarManager calendarManager;
    private static ReservationManager reservationManager;

    //data for testing
    String hotelDirectorName, hotelDirectorSurname;
    UserType hotelDirectorType;
    String hotelDirectorID;
    HotelDirector hotelDirector;
    String hotelNameFirenze, hotelCityFirenze, hotelAddressFirenze;
    int numOfSingleRoomsFirenze, numOfDoubleRoomsFirenze, numOfTripleRoomsFirenze;
    Hotel hotelFirenze;
    HotelCalendar hotelCalendarFirenze;

    @BeforeAll
    public static void beforeAll() {
        accountManager = AccountManager.createAccountManager();
        calendarManager = CalendarManager.createCalendarManager();
        reservationManager = ReservationManager.createReservationManager(accountManager, calendarManager);
        hotelManager = HotelManager.createHotelManager(calendarManager, reservationManager);
    }

    @BeforeEach
    void setUp(){

        //creo un direttore di hotel
        hotelDirectorName = "Franco";
        hotelDirectorSurname = "Spera";
        hotelDirectorType = UserType.HOTEL_DIRECTOR;
        hotelDirectorID = IdGenerator.generateUserID(hotelDirectorType, hotelDirectorName, hotelDirectorSurname);
        hotelDirector = new HotelDirector(hotelDirectorID, hotelDirectorName, hotelDirectorSurname, "franco.spera@edu.unifi.it", "012932-122832", "HDpassword", hotelDirectorType);
        accountManager.doRegistration(hotelDirector);

        //creo un hotel
        hotelNameFirenze = "LEONARDO DA VINCI";
        hotelCityFirenze = "Firenze";
        hotelAddressFirenze = "via Guido Monaco 2";
        numOfSingleRoomsFirenze = 2;
        numOfDoubleRoomsFirenze = 2;
        numOfTripleRoomsFirenze = 2;
        hotelFirenze = hotelManager.createHotel(hotelDirector, hotelNameFirenze, hotelCityFirenze, hotelAddressFirenze, null, null,  HotelRating.FOUR_STAR_HOTEL, numOfSingleRoomsFirenze, numOfDoubleRoomsFirenze, numOfTripleRoomsFirenze);
    }

    @AfterEach
    void tearDown(){
        accountManager.deleteUser(hotelDirector);
        hotelManager.removeHotel(hotelFirenze);
    }

    @Test
    void createCalendarTest(){
        calendarManager.createCalendar(hotelFirenze.getRooms(),hotelFirenze.getId(), reservationManager);
    }

    @Test
    public void closeRoom() {
        Hotel hotel = hotelManager.createHotel(hotelDirector,"Relais Tiffany", "Firenze", "via Guido Monaco 5", null, null, HotelRating.THREE_STAR_HOTEL, 1, 2, 1);
        hotelManager.addHotel(hotel);
        HotelCalendar calendar = calendarManager.createCalendar(hotel.getRooms(), hotel.getId(), reservationManager);

        //TODO ocho a quando si rilanciano i test che i giorni passano...
        LocalDate checkInDate = LocalDate.of(2024, 1, 25);
        LocalDate checkOutDate = LocalDate.of(2024, 1, 27);
        Research research = new Research("Firenze", checkInDate, checkOutDate, 2);
        ArrayList<Hotel> hotelsAvailable = hotelManager.doHotelResearch(research);
        assert(hotelsAvailable.size()==1);
        //assert(hotelsAvailable.contains(hotel));
        LocalDate dateClosingRoom = LocalDate.of(2024,1,25);
        calendarManager.closeRoom(hotel, dateClosingRoom, hotel.getRooms().get(1).getId());
        hotelsAvailable = hotelManager.doHotelResearch(research);
        assert(hotelsAvailable.size()==1);
        //assert(hotelsAvailable.contains(hotel));
        calendarManager.closeRoom(hotel, dateClosingRoom, hotel.getRooms().get(2).getId());
        calendarManager.closeRoom(hotel, dateClosingRoom, hotel.getRooms().get(3).getId());
        hotelsAvailable = hotelManager.doHotelResearch(research);
        assert(hotelsAvailable.isEmpty());
        hotelManager.removeHotel(hotel);
        calendarManager.removeCalendar(calendar, hotel.getRooms(), reservationManager);

    }
    @Test
    public void modifyPrice(){
        Hotel hotel = hotelManager.createHotel(hotelDirector,"Relais Tiffany", "Firenze", "via Guido Monaco 5", null, null, HotelRating.THREE_STAR_HOTEL, 1, 2, 1);
        hotelManager.addHotel(hotel);
        HotelCalendar calendar = calendarManager.createCalendar(hotel.getRooms(), hotel.getId(), reservationManager);
        calendarManager.modifyPrice(hotel, LocalDate.of(2024,1,25),hotel.getRooms().get(1).getId(),140);
        assert(calendarManager.getPrice(hotel.getId(),LocalDate.of(2024,1,25).toString(), hotel.getRooms().get(1).getId()) == 140);

        hotelManager.removeHotel(hotel);
        calendarManager.removeCalendar(calendar, hotel.getRooms(), reservationManager);
    }

    @AfterAll
    public static void afterAll() {

    }
}