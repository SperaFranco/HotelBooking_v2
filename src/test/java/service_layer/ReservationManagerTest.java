package service_layer;

import domain_model.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import utilities.HotelRating;
import utilities.IdGenerator;
import utilities.Research;
import utilities.UserType;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

class ReservationManagerTest {
    private static AccountManager accountManager;
    private static HotelManager hotelManager;
    private static ReservationManager reservationManager;
    private static CalendarManager calendarManager;
    private static HotelDirector hotelDirector;
    private static Hotel hotel1;
    private static HotelCalendar calendar;
    private static final boolean sendEmail = false; //Se vuoi far partire le email metti questo a true

    @BeforeAll
    public static void beforeAll(){
        accountManager = AccountManager.createAccountManager();
        calendarManager = CalendarManager.createCalendarManager();
        reservationManager = ReservationManager.createReservationManager(accountManager,calendarManager);
        hotelManager = HotelManager.createHotelManager(calendarManager, reservationManager);
    }

    @BeforeEach
    public void setUp(){
        hotelDirector = new HotelDirector(IdGenerator.generateUserID(UserType.HOTEL_DIRECTOR,"Franco","Spera"), "Franco", "Spera", "reginokamberaj@gmail.com", "+393337001756", "Kr29332d", UserType.HOTEL_DIRECTOR);
        accountManager.doRegistration(hotelDirector);
        hotel1 = hotelManager.createHotel(hotelDirector,"Relais Tiffany", "Firenze", "via Guido Monaco 5", null, null, HotelRating.THREE_STAR_HOTEL, 1, 2, 1);
        hotelManager.addHotel(hotel1);
        calendar = calendarManager.createCalendar(hotel1.getRooms(), hotel1.getId(), reservationManager);
    }

    @AfterEach
    public void tearDown(){
        accountManager.deleteUser(hotelDirector);
        hotelManager.removeHotel(hotel1);
    }

    @org.junit.jupiter.api.Test
    public void createReservation(){
        Guest guest1 = new Guest(IdGenerator.generateUserID(UserType.GUEST,"Regino","Kamberaj"), "Regino", "Kamberaj", "regino.kamberaj@edu.unifi.it", null, "passwordRegino",
                new CreditCard("Regino Kamberaj", "1234567812345678", "10-28", 735), UserType.GUEST);
        accountManager.doRegistration(guest1);

        LocalDate checkInDate = LocalDate.of(2024, 1, 25);
        LocalDate checkOutDate = LocalDate.of(2024, 1, 27);
        Research research = new Research("Firenze", checkInDate, checkOutDate, 2);
        ArrayList<Hotel> hotels = hotelManager.doHotelResearch(research);
        Hotel hotel = hotels.get(0);
        ArrayList<Room> rooms = hotelManager.getRoomsAvailable(hotel.getId(), research);
        String roomID = rooms.get(0).getId();

        Reservation reservation = reservationManager.createReservation(guest1,research,hotel, roomID,"two twin beds", sendEmail);
        assert(reservation != null);
        assert(reservation.getNotes().equals("two twin beds"));

        // verifica che nel calendario le date della camera prenotata risultino bloccate
        for(LocalDate date = checkInDate; !date.isEqual(checkOutDate); date = date.plusDays(1))
            assert(!calendarManager.getAvailability(hotel.getId(), date.toString(), roomID));

        // verifica che nel calendario la data di check-out della camera prentata risulti libera TODO da decidere se comprendere o meno
        //assert(calendarManager.getAvailability(hotel.getId(), checkOutDate.toString(), roomID));

        // verifica che un'altra prenotazione non possa essere effettuata per quella stessa camera in date in cui risulta occupata
        LocalDate otherCheckInDate = LocalDate.of(2024, 1, 26);
        LocalDate otherCheckOutDate = LocalDate.of(2024, 1, 29);
        Research otherResearch = new Research("Firenze", otherCheckInDate, otherCheckOutDate, 2);
        //Beh in realtà qui dovrei filtrare fra gli hotel...
        Reservation reservation2 = reservationManager.createReservation(guest1,otherResearch,hotel, roomID,"king size bed", sendEmail);
        assert(reservation2 == null);
        // verifica che se cancello la prima prenotazione, adesso posso eseguire correttamente la seconda prenotazione
        reservationManager.deleteReservation(reservation);
        accountManager.addBalance(guest1, 1000.0);
        reservation2 = reservationManager.createReservation(guest1,otherResearch,hotel, roomID,"king size bed", sendEmail);
        assert(reservation2 != null);
        reservationManager.deleteReservation(reservation2);

        accountManager.deleteUser(guest1);
    }
    @org.junit.jupiter.api.Test
    public void updateReservationDates(){
        Guest guest1 = new Guest(IdGenerator.generateUserID(UserType.GUEST,"Regino","Kamberaj"), "Regino", "Kamberaj", "regino.kamberaj@gmail.com", null, "passwordRegino",
                new CreditCard("Regino Kamberaj", "1234567812345678", "10-28", 735), UserType.GUEST);
        accountManager.doRegistration(guest1);

        LocalDate checkInDate = LocalDate.of(2024, 1, 24);
        LocalDate checkOutDate = LocalDate.of(2024, 1, 27);
        Research research = new Research("Firenze", checkInDate, checkOutDate, 2);
        ArrayList<Hotel> hotels = hotelManager.doHotelResearch(research);
        Hotel hotel = hotels.get(0);
        ArrayList<Room> rooms = hotelManager.getRoomsAvailable(hotel.getId(), research);
        String room = rooms.get(0).getId();
        Reservation reservation = reservationManager.createReservation(guest1,research,hotel, room,"two twin beds", sendEmail);

        //cambio le date della prenotazione
        LocalDate newCheckInDate = LocalDate.of(2024,1,25);
        LocalDate newCheckOutDate = LocalDate.of(2024,1,26);
        Research research2 = new Research("Firenze", newCheckInDate, newCheckOutDate, 2);
        reservationManager.updateReservation(reservation, null, newCheckInDate, newCheckOutDate);

        List<Reservation> guestReservations1 = reservationManager.getReservations(guest1);

        //controllo che le date della precedente prenotazione siano disponibili e che non lo siano le date aggiornate
        assert(calendarManager.getAvailability(hotel.getId(), checkOutDate.toString(), room));
        assert(calendarManager.getAvailability(hotel.getId(), checkOutDate.toString(), room));
        assert(!calendarManager.isRoomAvailable(hotel.getId(), research2, room));
        LocalDate otherCheckInDate = LocalDate.of(2024, 1, 27);
        LocalDate otherCheckOutDate = LocalDate.of(2024, 1, 29);
        Research otherResearch = new Research("Firenze", otherCheckInDate, otherCheckOutDate, 2);
        Reservation reservation2 = reservationManager.createReservation(guest1, otherResearch,hotel, room,"king size bed", sendEmail);
        List<Reservation> guestReservations2 = reservationManager.getReservations(guest1);
        List<Reservation> hotelReservations = reservationManager.getAllReservations(hotel);

        reservationManager.deleteReservation(reservation);
        reservationManager.deleteReservation(reservation2);
        accountManager.deleteUser(guest1);
    }

    @AfterAll
    public static void afterAll(){
        accountManager.deleteUser(hotelDirector);
        hotelManager.removeHotel(hotel1);
        calendarManager.removeCalendar(calendar, hotel1.getRooms(), reservationManager);
    }
}