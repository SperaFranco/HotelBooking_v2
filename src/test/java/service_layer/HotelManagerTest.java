package service_layer;

import domain_model.Hotel;
import domain_model.HotelDirector;
import service_layer.AccountManager;
import service_layer.CalendarManager;
import service_layer.HotelManager;
import service_layer.ReservationManager;
import utilities.HotelRating;
import utilities.IdGenerator;
import utilities.Research;
import utilities.UserType;
import java.time.LocalDate;
import java.util.ArrayList;

class HotelManagerTest {

    @org.junit.jupiter.api.Test
    public void createHotel(){
        AccountManager accountManager = new AccountManager();
        HotelDirector hotelDirector = new HotelDirector(IdGenerator.generateUserID(UserType.HOTEL_DIRECTOR,"Franco","Spera"), "Franco", "Spera", "info@relaistiffany.it", "+393337001756", "passwordHD", accountManager.getHotelManager(), UserType.HOTEL_DIRECTOR);
        accountManager.doRegistration(hotelDirector);
        CalendarManager calendarManager = new CalendarManager();
        ReservationManager reservationManager = new ReservationManager(calendarManager);
        HotelManager hotelManager = new HotelManager(reservationManager, calendarManager);
        Hotel hotel = hotelManager.createHotel(hotelDirector,"Relais Tiffany", "Firenze", "via Guido Monaco 5", null, null, null, HotelRating.THREE_STAR_HOTEL, 1, 2, 1);
        assert(hotel.getName().equals("Relais Tiffany"));
        assert(hotel.getRooms().size()==4);
    }

    @org.junit.jupiter.api.Test
    public void addAndRemoveRoom(){
        AccountManager accountManager = new AccountManager();
        HotelDirector hotelDirector = new HotelDirector(IdGenerator.generateUserID(UserType.HOTEL_DIRECTOR,"Franco","Spera"), "Franco", "Spera", "info@relaistiffany.it", "+393337001756", "passwordHD", accountManager.getHotelManager(), UserType.HOTEL_DIRECTOR);
        accountManager.doRegistration(hotelDirector);
        CalendarManager calendarManager = new CalendarManager();
        ReservationManager reservationManager = new ReservationManager(calendarManager);
        HotelManager hotelManager = new HotelManager(reservationManager, calendarManager);
        Hotel hotel = hotelManager.createHotel(hotelDirector,"Relais Tiffany", "Firenze", "via Guido Monaco 5", null, null, null, HotelRating.THREE_STAR_HOTEL, 1, 2, 1);

        assert(hotelManager.getHotelMap().isEmpty());
        hotelManager.addHotel(hotel);
        assert(hotelManager.getHotelMap().size() ==1);
        hotelManager.removeHotel(hotel);
        assert(hotelManager.getHotelMap().isEmpty());

    }

    @org.junit.jupiter.api.Test
    public void doHotelResearch(){
        AccountManager accountManager = new AccountManager();
        HotelDirector hotelDirector = new HotelDirector(IdGenerator.generateUserID(UserType.HOTEL_DIRECTOR,"Franco","Spera"), "Franco", "Spera", "info@relaistiffany.it", "+393337001756", "passwordHD", accountManager.getHotelManager(), UserType.HOTEL_DIRECTOR);
        accountManager.doRegistration(hotelDirector);
        CalendarManager calendarManager = new CalendarManager();
        ReservationManager reservationManager = new ReservationManager(calendarManager);
        HotelManager hotelManager = new HotelManager(reservationManager, calendarManager);
        Hotel hotel = hotelManager.createHotel(hotelDirector,"Relais Tiffany", "Firenze", "via Guido Monaco 5", null, null, null, HotelRating.THREE_STAR_HOTEL, 1, 2, 1);
        hotelManager.addHotel(hotel);
        LocalDate checkInDate = LocalDate.of(2023, 12, 25);
        LocalDate checkOutDate = LocalDate.of(2023, 12, 27);
        //FIXME numOfGuests deve essere maggiore di zero
        Research research = new Research("Firenze", checkInDate, checkOutDate, 3);
        ArrayList<Hotel> hotelsAvailable = hotelManager.doHotelResearch(research);
        assert(hotelsAvailable.size()==1);
        assert(hotelsAvailable.contains(hotel));
        calendarManager.setMinimumStay(hotel, checkInDate, hotel.getRooms().get(3).getId(), 3);
        hotelsAvailable = hotelManager.doHotelResearch(research);
        assert(hotelsAvailable.isEmpty());
    }









}