package service_layer;

import domain_model.*;
import org.junit.jupiter.api.*;
import utilities.HotelRating;
import utilities.IdGenerator;
import utilities.Research;
import utilities.UserType;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

class ReservationManagerTest {
    // managers
    private static AccountManager accountManager;
    private static HotelManager hotelManager;
    private static ReservationManager reservationManager;
    private static CalendarManager calendarManager;

    //Set TRUE to send confirmation e-mail at the end of a reservation process
    private static final boolean sendEmail = false;

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
    public static void beforeAll(){
        accountManager = AccountManager.createAccountManager();
        calendarManager = CalendarManager.createCalendarManager();
        reservationManager = ReservationManager.createReservationManager(accountManager,calendarManager);
        hotelManager = HotelManager.createHotelManager(calendarManager, reservationManager);
    }

    @BeforeEach
    public void setUp(){
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
    public void tearDown(){
        accountManager.deleteUser(hotelDirector);
        hotelManager.removeHotel(hotelFirenze);
        calendarManager.removeCalendar(hotelCalendarFirenze,hotelFirenze.getRooms(), reservationManager);
    }

    @Test
    public void createReservationTest(){

    }
    @Test
    public void getReservationTest(){

    }
    @Test
    public void deleteReservationTest(){

    }
    @Test
    public void updateReservationDatesTest(){

    }



}