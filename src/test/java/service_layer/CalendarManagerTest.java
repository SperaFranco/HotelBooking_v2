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
        numOfTripleRoomsFirenze = 1;
        hotelFirenze = hotelManager.createHotel(hotelDirector, hotelNameFirenze, hotelCityFirenze, hotelAddressFirenze, null, null,  HotelRating.FOUR_STAR_HOTEL, numOfSingleRoomsFirenze, numOfDoubleRoomsFirenze, numOfTripleRoomsFirenze);

        hotelManager.addHotel(hotelFirenze);
        hotelCalendarFirenze  = calendarManager.createCalendar(hotelFirenze.getRooms(), hotelFirenze.getId(), reservationManager);
    }

    @AfterEach
    void tearDown(){
        accountManager.deleteUser(hotelDirector);
        hotelManager.removeHotel(hotelFirenze);
        calendarManager.removeCalendar(hotelCalendarFirenze, hotelFirenze.getRooms(), reservationManager);

    }

    @Test
    void createCalendarTest(){
        hotelCalendarFirenze = calendarManager.createCalendar(hotelFirenze.getRooms(),hotelFirenze.getId(), reservationManager);
    }


    @Test
    void removeCalendarTest(){
        calendarManager.removeCalendar(hotelCalendarFirenze, hotelFirenze.getRooms(), reservationManager);
    }

    @Test
    public void modifyPrice(){
        calendarManager.modifyPrice(hotelFirenze, LocalDate.of(2024,1,25),hotelFirenze.getRooms().get(1).getId(),140);
        assert(calendarManager.getPrice(hotelFirenze.getId(),LocalDate.of(2024,1,25).toString(), hotelFirenze.getRooms().get(1).getId()) == 140);
    }

    @Test
    public void setAvailabilityTest() {
        Research research = setResearch("Firenze", 3, 2024, 2,12, 2024, 2, 14);
        ArrayList<Hotel> hotelsAvailable = hotelManager.doHotelResearch(research);
        assert(hotelsAvailable.size()==1);

        LocalDate dateClosingRoom = LocalDate.of(2024,2,12);
        calendarManager.setAvailability(hotelFirenze.getId(), dateClosingRoom.toString(), hotelFirenze.getRooms().get(4).getId(), false);
        hotelsAvailable = hotelManager.doHotelResearch(research);
        assert(hotelsAvailable.isEmpty());
    }

    @Test
    public void setMinimumStayTest(){
        Research research = setResearch("Firenze", 3, 2024, 2,12, 2024, 2, 14);
        ArrayList<Hotel> hotelsAvailable = hotelManager.doHotelResearch(research);
        assert(hotelsAvailable.size()==1);

        LocalDate date = LocalDate.of(2024,2,12);
        calendarManager.setMinimumStay(hotelFirenze, date,hotelFirenze.getRooms().get(4).getId(), 3);
        hotelsAvailable = hotelManager.doHotelResearch(research);
        assert(hotelsAvailable.isEmpty());
    }

    @AfterAll
    public static void afterAll() {

    }

    //helper method
    private Research setResearch(String city, int numOfGuests, int checkInYear, int checkInMounth, int checkInDay, int checkOutYear, int checkOutMounth, int checkOutDay){
        LocalDate checkInDate = LocalDate.of(checkInYear, checkInMounth, checkInDay);
        LocalDate checkOutDate = LocalDate.of(checkOutYear, checkOutMounth, checkOutDay);
        return new Research(city, checkInDate, checkOutDate, numOfGuests);
    }
}