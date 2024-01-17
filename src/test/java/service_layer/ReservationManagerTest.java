package service_layer;

import domain_model.*;
import org.junit.jupiter.api.*;
import utilities.HotelRating;
import utilities.IdGenerator;
import utilities.Research;
import utilities.UserType;

import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.assertTrue;

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
    String guestName, guestSurname, email, password, guestID;
    CreditCard creditCard;
    Guest guest;

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

        // creo un guest
        guestName = "Regino";
        guestSurname = "Kamberaj";
        email = "regino.kamberaj@edu.unifi.it";
        password = "guestPassword";
        guestID = IdGenerator.generateUserID(UserType.GUEST, guestName, guestSurname);
        creditCard = new CreditCard(guestName, "1234234534564567", "04-25", 831);
        guest = new Guest(guestID, guestName, guestSurname, email, null, password, creditCard, UserType.GUEST);
        accountManager.doRegistration(guest);
    }

    @AfterEach
    public void tearDown(){
        accountManager.deleteUser(hotelDirector);
        accountManager.deleteUser(guest);
        hotelManager.removeHotel(hotelFirenze);
        calendarManager.removeCalendar(hotelCalendarFirenze,hotelFirenze.getRooms(), reservationManager);
    }

    @Test
    public void createReservationTest(){
        Research research = setResearch("Firenze", 2, 2024, 3, 12, 2024, 3,15);
        reservationManager.createReservation(guest,research, hotelFirenze, hotelFirenze.getRooms().get(2).getId(), "Due letti singoli", false);
    }

    @Test
    public void getReservationTest(){
        Research research = setResearch("Firenze", 2, 2024, 4, 12, 2024, 4,14);
        Reservation reservation = reservationManager.createReservation(guest,research, hotelFirenze, hotelFirenze.getRooms().get(2).getId(), "Due letti singoli", false);
        List<Reservation> reservations = reservationManager.getReservations(guest);

        // verifico che la prenotazione esista nel database e venga messa in una lista dal metodo getReservations
        assert(reservations.size() == 1 );

        // verifico che la prenotazione recuperata corrisponda a quella che ho inserito
        Assertions.assertEquals(reservations.get(0).getId(), reservation.getId());
        Assertions.assertTrue( reservations.get(0).getCheckIn().isEqual(reservation.getCheckIn()) );
        Assertions.assertTrue( reservations.get(0).getCheckOut().isEqual(reservation.getCheckOut()) );
        Assertions.assertEquals(reservations.get(0).getNotes(), reservation.getNotes());

        // verifico l'inserimento di una seconda prenotazione
        Reservation secondReservation = reservationManager.createReservation(guest,research, hotelFirenze, hotelFirenze.getRooms().get(1).getId(), "Un letto singolo", false);
        reservations = reservationManager.getReservations(guest);

        // verifico che la prenotazione esista nel database e venga messa in una lista dal metodo getReservations
        assert(reservations.size() == 2 );
    }

    @Test
    public void deleteReservationTest(){
        Research research = setResearch("Firenze", 2, 2024, 4, 12, 2024, 4,14);
        Reservation reservation = reservationManager.createReservation(guest,research, hotelFirenze, hotelFirenze.getRooms().get(2).getId(), "Due letti singoli", false);
        Reservation secondReservation = reservationManager.createReservation(guest,research, hotelFirenze, hotelFirenze.getRooms().get(1).getId(), "Un letto singolo", false);
        List<Reservation> reservations = reservationManager.getReservations(guest);

        Assertions.assertEquals(2, reservations.size());

        // cancello una prenotazione e verifico che la cancellazione venga registrata nel database
        reservationManager.deleteReservation(reservation);
        reservations = reservationManager.getReservations(guest);
        Assertions.assertEquals(1, reservations.size());
    }

    @Test
    public void updateReservationTest(){
        Research research = setResearch("Firenze", 2, 2024, 4, 12, 2024, 4,14);
        Reservation reservation = reservationManager.createReservation(guest,research, hotelFirenze, hotelFirenze.getRooms().get(2).getId(), "Due letti singoli", false);
        List<Reservation> reservations = reservationManager.getReservations(guest);

        //modifico la prenotazione e controllo che gli aggiornamenti siano stati salvati nella tabella
        reservationManager.updateReservation(reservation, "Un letto matrimoniale", LocalDate.of(2024,5,12), LocalDate.of(2024,5,15));
        reservations = reservationManager.getReservations(guest);

        Assertions.assertEquals("Un letto matrimoniale", reservations.get(0).getNotes());
    }

    //helper method
    private Research setResearch(String city, int numOfGuests, int checkInYear, int checkInMounth, int checkInDay, int checkOutYear, int checkOutMounth, int checkOutDay){
        LocalDate checkInDate = LocalDate.of(checkInYear, checkInMounth, checkInDay);
        LocalDate checkOutDate = LocalDate.of(checkOutYear, checkOutMounth, checkOutDay);
        return new Research(city, checkInDate, checkOutDate, numOfGuests);
    }

}