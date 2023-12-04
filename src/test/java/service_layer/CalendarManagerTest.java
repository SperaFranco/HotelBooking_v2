package service_layer;

import domain_model.Hotel;
import domain_model.HotelDirector;
import utilities.HotelRating;
import utilities.IdGenerator;
import utilities.Research;
import utilities.UserType;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class CalendarManagerTest {

    @org.junit.jupiter.api.Test
    public void closeRoom(){
        AccountManager accountManager = new AccountManager();
        HotelDirector hotelDirector = new HotelDirector(IdGenerator.generateUserID(UserType.HOTEL_DIRECTOR,"Franco","Spera"), "Franco", "Spera", "info@relaistiffany.it", "+393337001756", "passwordHD", accountManager.getHotelManager(), UserType.HOTEL_DIRECTOR);
        accountManager.doRegistration(hotelDirector);
        CalendarManager calendarManager = new CalendarManager();
        ReservationManager reservationManager = new ReservationManager(accountManager, calendarManager);
        HotelManager hotelManager = new HotelManager(reservationManager, calendarManager);
        Hotel hotel = hotelManager.createHotel(hotelDirector,"Relais Tiffany", "Firenze", "via Guido Monaco 5", null, null, null, HotelRating.THREE_STAR_HOTEL, 1, 2, 1);
        hotelManager.addHotel(hotel);
        LocalDate checkInDate = LocalDate.of(2023, 12, 25);
        LocalDate checkOutDate = LocalDate.of(2023, 12, 27);
        //FIXME numOfGuests deve essere maggiore di zero
        Research research = new Research("Firenze", checkInDate, checkOutDate, 2);
        ArrayList<Hotel> hotelsAvailable = hotelManager.doHotelResearch(research);
        assert(hotelsAvailable.size()==1);
        assert(hotelsAvailable.contains(hotel));
        LocalDate dateClosingRoom = LocalDate.of(2023,12,25);
        calendarManager.closeRoom(hotel, dateClosingRoom, hotel.getRooms().get(1).getId());
        hotelsAvailable = hotelManager.doHotelResearch(research);
        assert(hotelsAvailable.size()==1);
        assert(hotelsAvailable.contains(hotel));
        calendarManager.closeRoom(hotel, dateClosingRoom, hotel.getRooms().get(2).getId());
        calendarManager.closeRoom(hotel, dateClosingRoom, hotel.getRooms().get(3).getId());
        hotelsAvailable = hotelManager.doHotelResearch(research);

        assert(hotelsAvailable.isEmpty());

    }
    @org.junit.jupiter.api.Test
    public void modifyPrice(){
        AccountManager accountManager = new AccountManager();
        HotelDirector hotelDirector = new HotelDirector(IdGenerator.generateUserID(UserType.HOTEL_DIRECTOR,"Franco","Spera"), "Franco", "Spera", "info@relaistiffany.it", "+393337001756", "passwordHD", accountManager.getHotelManager(), UserType.HOTEL_DIRECTOR);
        accountManager.doRegistration(hotelDirector);
        CalendarManager calendarManager = new CalendarManager();
        ReservationManager reservationManager = new ReservationManager(accountManager, calendarManager);
        HotelManager hotelManager = new HotelManager(reservationManager, calendarManager);
        Hotel hotel = hotelManager.createHotel(hotelDirector,"Relais Tiffany", "Firenze", "via Guido Monaco 5", null, null, null, HotelRating.THREE_STAR_HOTEL, 1, 2, 1);
        hotelManager.addHotel(hotel);
        calendarManager.modifyPrice(hotel, LocalDate.of(2023,12,25),hotel.getRooms().get(1).getId(),140);
        assert(calendarManager.getCalendarByHotelID(hotel.getId()).getRoomStatusMap().get(LocalDate.of(2023,12,25)).get(hotel.getRooms().get(1).getId()).getPrice()==140);
    }
}