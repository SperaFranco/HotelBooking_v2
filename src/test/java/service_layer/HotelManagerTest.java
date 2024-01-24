package service_layer;

import domain_model.*;
import org.junit.jupiter.api.*;
import utilities.HotelRating;
import utilities.IdGenerator;
import utilities.Research;
import utilities.UserType;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.jupiter.api.Assertions.*;

class HotelManagerTest {

    // managers
    private static AccountManager accountManager;
    private static HotelManager hotelManager;
    private static CalendarManager calendarManager;
    private static ReservationManager reservationManager;

    // data for tests
    String hotelDirectorName, hotelDirectorSurname;
    UserType hotelDirectorType;
    String hotelDirectorID;
    HotelDirector hotelDirector;
    String hotelNameFirenze, hotelCityFirenze, hotelAddressFirenze;
    String hotelNameFirenze2, hotelCityFirenze2, hotelAddressFirenze2;
    String hotelNameLivorno, hotelCityLivorno, hotelAddressLivorno;
    int numOfSingleRoomsFirenze, numOfDoubleRoomsFirenze, numOfTripleRoomsFirenze;
    int numOfSingleRoomsFirenze2, numOfDoubleRoomsFirenze2, numOfTripleRoomsFirenze2;
    int numOfSingleRoomsLivorno, numOfDoubleRoomsLivorno, numOfTripleRoomsLivorno;
    Hotel hotelFirenze, hotelFirenze2, hotelLivorno;
    HotelCalendar calendarHotelFirenze, calendarHotelFirenze2, calendarHotelLivorno;

    @BeforeAll
    public static void beforeAll() {
        accountManager = AccountManager.createAccountManager();
        calendarManager = CalendarManager.createCalendarManager();
        reservationManager = ReservationManager.createReservationManager(accountManager, calendarManager);
        hotelManager = HotelManager.createHotelManager(calendarManager,reservationManager);
    }

    @BeforeEach
    void setUp(){
        // creo il direttore d'hotel
        hotelDirectorName = "Franco";
        hotelDirectorSurname = "Spera";
        hotelDirectorType = UserType.HOTEL_DIRECTOR;
        hotelDirectorID = IdGenerator.generateUserID(hotelDirectorType, hotelDirectorName, hotelDirectorSurname);
        hotelDirector = new HotelDirector(hotelDirectorID, "Franco", "Spera", "franco.spera@edu.unifi.it", "+393337232176423", "passwordHD", UserType.HOTEL_DIRECTOR);
        accountManager.doRegistration(hotelDirector);

        // creo un hotel a Firenze
        hotelNameFirenze = "LEONARDO DA VINCI";
        hotelCityFirenze = "Firenze";
        hotelAddressFirenze = "via Guido Monaco 2";
        numOfSingleRoomsFirenze = 2;
        numOfDoubleRoomsFirenze = 2;
        numOfTripleRoomsFirenze = 2;
        hotelFirenze = hotelManager.createHotel(hotelDirector, hotelNameFirenze, hotelCityFirenze, hotelAddressFirenze, null, null,  HotelRating.FOUR_STAR_HOTEL, numOfSingleRoomsFirenze, numOfDoubleRoomsFirenze, numOfTripleRoomsFirenze);

        hotelNameFirenze2 = "ROOM MATE";
        hotelCityFirenze2 = "Firenze";
        hotelAddressFirenze2 = "via XXVII Aprile 34";
        numOfSingleRoomsFirenze2 = 1;
        numOfDoubleRoomsFirenze2 = 1;
        numOfTripleRoomsFirenze2 = 1;
        hotelFirenze2 = hotelManager.createHotel(hotelDirector, hotelNameFirenze2, hotelCityFirenze2, hotelAddressFirenze2, null, null,  HotelRating.FOUR_STAR_HOTEL, numOfSingleRoomsFirenze2, numOfDoubleRoomsFirenze2, numOfTripleRoomsFirenze2);


        // creo un hotel a Livorno
        hotelNameLivorno = "GRAN DUCA";
        hotelCityLivorno = "Livorno";
        hotelAddressLivorno = "piazza Micheli 16";
        numOfSingleRoomsLivorno = 3;
        numOfDoubleRoomsLivorno = 1;
        numOfTripleRoomsLivorno = 2;
        hotelLivorno = hotelManager.createHotel(hotelDirector, hotelNameLivorno, hotelCityLivorno, hotelAddressLivorno, null, null,  HotelRating.FOUR_STAR_HOTEL, numOfSingleRoomsLivorno, numOfDoubleRoomsLivorno, numOfTripleRoomsLivorno);
    }

    @AfterEach
    void tearDown(){
        accountManager.deleteUser(hotelDirector);
        hotelManager.removeHotel(hotelFirenze);
        hotelManager.removeHotel(hotelFirenze2);
        hotelManager.removeHotel(hotelLivorno);
    }


    @Test
    void createHotelTest() {
        hotelManager.addHotel(hotelFirenze);
    }

    @Test
    void createExistingHotel(){
        hotelManager.addHotel(hotelFirenze);
        Exception exception = assertThrows(RuntimeException.class, ()->hotelManager.addHotel(hotelFirenze));
        assertEquals("java.lang.RuntimeException: hotel già esistente", exception.getMessage());
    }

    @Test
    void findHotelByIDTest(){
        hotelManager.addHotel(hotelFirenze);
        Hotel hotelInDB = hotelManager.findHotelByID(hotelFirenze.getId());
        assertThat(hotelInDB.getId(), equalTo(hotelFirenze.getId()));
        hotelManager.removeHotel(hotelFirenze);
    }
    @Test
    public void removeHotel() {
        hotelManager.addHotel(hotelFirenze);
        hotelManager.removeHotel(hotelFirenze);
        Hotel hotelInDB = hotelManager.findHotelByID(hotelFirenze.getId());
        assertNull(hotelInDB);
    }
    @Test
    void setMinimumStayTest(){
        hotelManager.addHotel(hotelFirenze);
        if(calendarHotelFirenze == null)
            calendarHotelFirenze = calendarManager.createCalendar(hotelFirenze.getRooms(), hotelFirenze.getId(), reservationManager);
        LocalDate date = LocalDate.of(2024,4,16);

        //verifico che il valore del soggiorno minimo sia 1
        calendarManager.setMinimumStay(hotelFirenze, date, hotelFirenze.getRooms().get(0).getId(), 3);
        assert(calendarManager.getMinimumStay(hotelFirenze.getId(),date, hotelFirenze.getRooms().get(0).getId())==3);

        // cambio il valore del soggiorno minimo e controllo che la modifica venga salvata correttamente
        calendarManager.setMinimumStay(hotelFirenze, date, hotelFirenze.getRooms().get(0).getId(), 1);
        assert(calendarManager.getMinimumStay(hotelFirenze.getId(),date, hotelFirenze.getRooms().get(0).getId())==1);
    }
    @Test
    public void doHotelResearchTest() {
        hotelManager.addHotel(hotelFirenze);
        hotelManager.addHotel(hotelLivorno);
        if(calendarHotelLivorno == null)
            calendarHotelLivorno = calendarManager.createCalendar(hotelLivorno.getRooms(), hotelLivorno.getId(), reservationManager);
        if(calendarHotelFirenze == null)
            calendarHotelFirenze = calendarManager.createCalendar(hotelFirenze.getRooms(), hotelFirenze.getId(), reservationManager);
        if(calendarHotelFirenze2 == null)
            calendarHotelFirenze2 = calendarManager.createCalendar(hotelFirenze2.getRooms(), hotelFirenze2.getId(), reservationManager);

        // verifico che cercando un hotel su Firenze mi restituisca come risultato uno
        LocalDate checkInDate = LocalDate.of(2024, 1, 25);
        LocalDate checkOutDate = LocalDate.of(2024, 1, 27);
        Research research = new Research("Firenze", checkInDate, checkOutDate, 3);
        ArrayList<Hotel> hotelsAvailable = hotelManager.doHotelResearch(research);
        assert(hotelsAvailable.size()==1);

        // aggiungo un secondo hotel a Firenze e verifico che il metodo mi restituisca due hotel
        hotelManager.addHotel(hotelFirenze2);
        hotelsAvailable = hotelManager.doHotelResearch(research);
        assert(hotelsAvailable.size()==2);
        hotelManager.removeHotel(hotelFirenze2);

        // imposto il soggiorno minimo per le camere triple a 3, in modo da far fallire una ricerca di una camera tripla per un soggiorno di due notti
        calendarManager.setMinimumStay(hotelsAvailable.get(0), checkInDate, hotelFirenze.getRooms().get(4).getId(), 3);
        calendarManager.setMinimumStay(hotelsAvailable.get(0), checkInDate, hotelFirenze.getRooms().get(5).getId(), 3);
        hotelsAvailable = hotelManager.doHotelResearch(research);
        assert(hotelsAvailable.isEmpty());



        // cambio le date della ricerca in un periodo in cui il soggiorno minimo è 1 per verificare che la ricerca ha esito positivo
        checkInDate = LocalDate.of(2024,1,28);
        checkOutDate = LocalDate.of(2024,1,30);
        research = new Research("Firenze", checkInDate, checkOutDate, 3);
        hotelsAvailable = hotelManager.doHotelResearch(research);
        assert(hotelsAvailable.size()==1);

        hotelManager.removeHotel(hotelFirenze);
        hotelManager.removeHotel(hotelLivorno);
        calendarManager.removeCalendar(calendarHotelFirenze, hotelFirenze.getRooms(), reservationManager);
        calendarManager.removeCalendar(calendarHotelLivorno, hotelLivorno.getRooms(), reservationManager);
    }

    @AfterAll
    public static void afterAll() {

    }









}