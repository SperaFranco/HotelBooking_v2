package service_layer;

import domain_model.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import utilities.HotelRating;
import utilities.IdGenerator;
import utilities.Research;
import utilities.UserType;

import java.time.LocalDate;
import java.util.ArrayList;

class ReservationManagerTest {

    @org.junit.jupiter.api.Test
    public void createReservation(){

        AccountManager accountManager = new AccountManager();
        HotelDirector hotelDirector = new HotelDirector(IdGenerator.generateUserID(UserType.HOTEL_DIRECTOR,"Franco","Spera"), "Franco", "Spera", "info@relaistiffany.it", "+393337001756", "passwordHD", accountManager.getHotelManager(), UserType.HOTEL_DIRECTOR);
        accountManager.doRegistration(hotelDirector);
        CalendarManager calendarManager = new CalendarManager();
        ReservationManager reservationManager = new ReservationManager(accountManager, calendarManager);
        HotelManager hotelManager = new HotelManager(reservationManager, calendarManager);
        Hotel hotel = hotelManager.createHotel(hotelDirector,"Relais Tiffany", "Firenze", "via Guido Monaco 5", null, null, null, HotelRating.THREE_STAR_HOTEL, 1, 2, 1);
        hotelManager.addHotel(hotel);
        Guest guest1 = new Guest(IdGenerator.generateUserID(UserType.GUEST,"Regino","Kamberaj"), "Regino", "Kamberaj", "regino.kamberaj@gmail.com", null, "passwordRegino", null, reservationManager, UserType.GUEST);
        LocalDate checkInDate = LocalDate.of(2023, 12, 25);
        LocalDate checkOutDate = LocalDate.of(2023, 12, 27);
        Research research = new Research("Firenze", checkInDate, checkOutDate, 2);
        ArrayList<Hotel> hotels = hotelManager.doHotelResearch(research);
        hotel = hotels.get(0);
        ArrayList<Room> rooms = hotel.getRoomsAvailable(research);
        Room room = rooms.get(0);
        Reservation reservation = reservationManager.createReservation(guest1,research,hotel, room.getId(),"two twin beds");
        assert(reservation.getDescription().equals("two twin beds"));

    }
}