package service_layer;

import data_access.HotelCalendarDAO;
import data_access.HotelDAO;
import data_access.UserDAO;
import domain_model.Hotel;
import domain_model.HotelCalendar;
import domain_model.HotelDirector;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import service_layer.AccountManager;
import service_layer.CalendarManager;
import service_layer.HotelManager;
import service_layer.ReservationManager;
import utilities.HotelRating;
import utilities.IdGenerator;
import utilities.Research;
import utilities.UserType;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

class CalendarManagerTest {
    private static AccountManager accountManager;
    private static HotelManager hotelManager;
    private static CalendarManager calendarManager;
    private static HotelDirector hotelDirector;

    @BeforeAll
    public static void setUp() {
        accountManager = new AccountManager();
        calendarManager = accountManager.getCalendarManager();
        hotelManager = accountManager.getHotelManager();

        //Supponiamo di aver già aggiunto un hotelDirector
        AccountManager accountManager = new AccountManager();
        hotelDirector = new HotelDirector(IdGenerator.generateUserID(UserType.HOTEL_DIRECTOR,"Franco","Spera"), "Franco", "Spera", "info@relaistiffany.it", "+393337001756", "passwordHD", UserType.HOTEL_DIRECTOR);
        accountManager.doRegistration(hotelDirector);

    }

    @org.junit.jupiter.api.Test
    public void closeRoom() {
        Hotel hotel = hotelManager.createHotel(hotelDirector,"Relais Tiffany", "Firenze", "via Guido Monaco 5", null, null, HotelRating.THREE_STAR_HOTEL, 1, 2, 1);
        hotelManager.addHotel(hotel);
        HotelCalendar calendar = calendarManager.createCalendar(hotel.getRooms(), hotel.getId(), accountManager.getReservationManager());

        LocalDate checkInDate = LocalDate.of(2023, 12, 25);
        LocalDate checkOutDate = LocalDate.of(2023, 12, 27);
        Research research = new Research("Firenze", checkInDate, checkOutDate, 2);
        ArrayList<Hotel> hotelsAvailable = hotelManager.doHotelResearch(research);
        assert(hotelsAvailable.size()==1);
        //assert(hotelsAvailable.contains(hotel));
        LocalDate dateClosingRoom = LocalDate.of(2023,12,25);
        calendarManager.closeRoom(hotel, dateClosingRoom, hotel.getRooms().get(1).getId());
        hotelsAvailable = hotelManager.doHotelResearch(research);
        assert(hotelsAvailable.size()==1);
        //assert(hotelsAvailable.contains(hotel));
        calendarManager.closeRoom(hotel, dateClosingRoom, hotel.getRooms().get(2).getId());
        calendarManager.closeRoom(hotel, dateClosingRoom, hotel.getRooms().get(3).getId());
        hotelsAvailable = hotelManager.doHotelResearch(research);
        assert(hotelsAvailable.isEmpty());
        hotelManager.removeHotel(hotel);
        calendarManager.removeCalendar(calendar, hotel.getRooms());

    }
    @org.junit.jupiter.api.Test
    public void modifyPrice(){
        Hotel hotel = hotelManager.createHotel(hotelDirector,"Relais Tiffany", "Firenze", "via Guido Monaco 5", null, null, HotelRating.THREE_STAR_HOTEL, 1, 2, 1);
        hotelManager.addHotel(hotel);
        HotelCalendar calendar = calendarManager.createCalendar(hotel.getRooms(), hotel.getId(), accountManager.getReservationManager());
        calendarManager.modifyPrice(hotel, LocalDate.of(2023,12,25),hotel.getRooms().get(1).getId(),140);
        assert(calendarManager.getPrice(hotel.getId(),LocalDate.of(2023,12,25).toString(), hotel.getRooms().get(1).getId()) == 140);

        hotelManager.removeHotel(hotel);
        calendarManager.removeCalendar(calendar, hotel.getRooms());
    }

    @AfterAll
    public static void TearDown() {
        accountManager.deleteUser(hotelDirector);
        accountManager.getUserDao().disconnect();
    }
}