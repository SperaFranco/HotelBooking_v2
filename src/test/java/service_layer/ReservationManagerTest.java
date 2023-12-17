package service_layer;

import domain_model.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
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

    @BeforeAll
    public static void setUp(){
        accountManager = new AccountManager();
        calendarManager = accountManager.getCalendarManager();
        hotelManager = accountManager.getHotelManager();
        reservationManager = accountManager.getReservationManager();

        //Supponiamo di aver già inserito un hotel
        hotelDirector = new HotelDirector(IdGenerator.generateUserID(UserType.HOTEL_DIRECTOR,"Franco","Spera"), "Franco", "Spera", "info@relaistiffany.it", "+393337001756", "passwordHD", UserType.HOTEL_DIRECTOR);
        accountManager.doRegistration(hotelDirector);
        hotel1 = hotelManager.createHotel(hotelDirector,"Relais Tiffany", "Firenze", "via Guido Monaco 5", null, null, HotelRating.THREE_STAR_HOTEL, 1, 2, 1);
        hotelManager.addHotel(hotel1);
        calendar = calendarManager.createCalendar(hotel1.getRooms(), hotel1.getId(), reservationManager);
    }
    @org.junit.jupiter.api.Test
    public void createReservation(){
        Guest guest1 = new Guest(IdGenerator.generateUserID(UserType.GUEST,"Regino","Kamberaj"), "Regino", "Kamberaj", "regino.kamberaj@gmail.com", null, "passwordRegino", null, UserType.GUEST);

        LocalDate checkInDate = LocalDate.of(2023, 12, 25);
        LocalDate checkOutDate = LocalDate.of(2023, 12, 27);
        Research research = new Research("Firenze", checkInDate, checkOutDate, 2);
        ArrayList<Hotel> hotels = hotelManager.doHotelResearch(research);
        Hotel hotel = hotels.get(0);
        ArrayList<Room> rooms = hotelManager.getRoomsAvailable(hotel.getId(), research);
        String roomID = rooms.get(0).getId();

        Reservation reservation = reservationManager.createReservation(guest1,research,hotel, roomID,"two twin beds");
        assert(reservation != null);
        assert(reservation.getNotes().equals("two twin beds"));

        // verifica che nel calendario le date della camera prenotata risultino bloccate
        for(LocalDate date = checkInDate; !date.isEqual(checkOutDate); date = date.plusDays(1))
            assert(!calendarManager.getAvailability(hotel.getId(), date.toString(), roomID));

        // verifica che nel calendario la data di check-out della camera prentata risulti libera
        assert(calendarManager.getAvailability(hotel.getId(), checkOutDate.toString(), roomID));

        // verifica che un'altra prenotazione non possa essere effettuata per quella stessa camera in date in cui risulta occupata
        LocalDate otherCheckInDate = LocalDate.of(2023, 12, 26);
        LocalDate otherCheckOutDate = LocalDate.of(2023, 12, 29);
        Research otherResearch = new Research("Firenze", otherCheckInDate, otherCheckOutDate, 2);
        //Beh in realtà qui dovrei filtrare fra gli hotel...
        Reservation reservation2 = reservationManager.createReservation(guest1,otherResearch,hotel, roomID,"king size bed");
        assert(reservation2 == null);
        // verifica che se cancello la prima prenotazione, adesso posso eseguire correttamente la seconda prenotazione
        reservationManager.deleteReservation(reservation);
        reservation2 = reservationManager.createReservation(guest1,otherResearch,hotel, roomID,"king size bed");
        assert(reservation2 != null);
        reservationManager.deleteReservation(reservation2);

        accountManager.deleteUser(guest1);
    }
    @org.junit.jupiter.api.Test
    public void updateReservationDates(){
        Guest guest1 = new Guest(IdGenerator.generateUserID(UserType.GUEST,"Regino","Kamberaj"), "Regino", "Kamberaj", "regino.kamberaj@gmail.com", null, "passwordRegino", null, UserType.GUEST);

        LocalDate checkInDate = LocalDate.of(2023, 12, 25);
        LocalDate checkOutDate = LocalDate.of(2023, 12, 27);
        Research research = new Research("Firenze", checkInDate, checkOutDate, 2);
        ArrayList<Hotel> hotels = hotelManager.doHotelResearch(research);
        Hotel hotel = hotels.get(0);
        ArrayList<Room> rooms = hotelManager.getRoomsAvailable(hotel.getId(), research);
        String room = rooms.get(0).getId();
        Reservation reservation = reservationManager.createReservation(guest1,research,hotel, room,"two twin beds");

        //cambio le date della prenotazione
        LocalDate newCheckInDate = LocalDate.of(2023,12,24);
        LocalDate newCheckOutDate = LocalDate.of(2023,12,26);
        reservationManager.updateReservation(reservation, null, newCheckInDate, newCheckOutDate);
        List<Reservation> guestReservations1 = reservationManager.getReservations(guest1);

        //controllo che le date della precedente prenotazione siano disponibili e che non lo siano le date aggiornate
        LocalDate otherCheckInDate = LocalDate.of(2023, 12, 27);
        LocalDate otherCheckOutDate = LocalDate.of(2023, 12, 29);
        Research otherResearch = new Research("Firenze", otherCheckInDate, otherCheckOutDate, 2);
        Reservation reservation2 = reservationManager.createReservation(guest1, otherResearch,hotel, room,"king size bed");
        List<Reservation> guestReservations2 = reservationManager.getReservations(guest1);
        List<Reservation> hotelReservations = reservationManager.getAllReservations(hotel);

        reservationManager.deleteReservation(reservation);
        reservationManager.deleteReservation(reservation2);
        accountManager.deleteUser(guest1);
        //TODO mancano gli assert?
    }

    @AfterAll
    public static void tearDown(){
        accountManager.deleteUser(hotelDirector);
        hotelManager.removeHotel(hotel1);
        calendarManager.removeCalendar(calendar, hotel1.getRooms());
        accountManager.getUserDao().disconnect();
    }
}