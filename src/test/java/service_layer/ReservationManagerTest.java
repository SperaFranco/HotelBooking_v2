package service_layer;

import domain_model.*;
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
import java.util.List;

class ReservationManagerTest {

    @org.junit.jupiter.api.Test
    public void createReservation(){
       /*
        AccountManager accountManager = CreateManager.getInstance().getAccountManager();
        HotelDirector hotelDirector = new HotelDirector(IdGenerator.generateUserID(UserType.HOTEL_DIRECTOR,"Franco","Spera"), "Franco", "Spera", "info@relaistiffany.it", "+393337001756", "passwordHD", accountManager.getHotelManager(), UserType.HOTEL_DIRECTOR);
        accountManager.doRegistration(hotelDirector);
        CalendarManager calendarManager = new CalendarManager(accountManager);
        ReservationManager reservationManager = new ReservationManager(calendarManager);
        HotelManager hotelManager = new HotelManager(reservationManager, calendarManager);
        Hotel hotel = hotelManager.createHotel(hotelDirector,"Relais Tiffany", "Firenze", "via Guido Monaco 5", null, null, HotelRating.THREE_STAR_HOTEL, 1, 2, 1);
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
        assert(reservation != null);
        assert(reservation.getDescription().equals("two twin beds"));

        // verifica che nel calendario le date della camera prentata risultino bloccate
        for(LocalDate date = checkInDate; !date.isEqual(checkOutDate); date = date.plusDays(1))
            assert(!calendarManager.getCalendarByHotelID(hotel.getId()).getRoomStatusMap().get(date).get(rooms.get(0).getId()).getAvailability());
        // verifica che nel calendario la data di check-out della camera prentata risulti libera
        assert(calendarManager.getCalendarByHotelID(hotel.getId()).getRoomStatusMap().get(checkOutDate).get(rooms.get(0).getId()).getAvailability());
        // verifica che un'altra prenotazione non possa essere effettuata per quella stessa camera in date in cui risulta occupata
        LocalDate otherCheckInDate = LocalDate.of(2023, 12, 26);
        LocalDate otherCheckOutDate = LocalDate.of(2023, 12, 29);
        Research otherResearch = new Research("Firenze", otherCheckInDate, otherCheckOutDate, 2);
        Reservation reservation2 = reservationManager.createReservation(guest1,otherResearch,hotel, room.getId(),"king size bed");
        assert(reservation2 == null);
        // verifica che se cancello la prima prenotazione, adesso posso eseguire correttamente la seconda prenotazione
        reservationManager.deleteReservation(reservation.getId());
        reservation2 = reservationManager.createReservation(guest1,otherResearch,hotel, room.getId(),"king size bed");
        assert(reservation2 != null);

        */
    }
    @org.junit.jupiter.api.Test
    public void updateReservationDates(){
        /*
        //creo l'ambiente per eseguire il test
        AccountManager accountManager = new AccountManager();
        HotelDirector hotelDirector = new HotelDirector(IdGenerator.generateUserID(UserType.HOTEL_DIRECTOR,"Franco","Spera"), "Franco", "Spera", "info@relaistiffany.it", "+393337001756", "passwordHD", accountManager.getHotelManager(), UserType.HOTEL_DIRECTOR);
        accountManager.doRegistration(hotelDirector);
        CalendarManager calendarManager = new CalendarManager(accountManager);
        ReservationManager reservationManager = new ReservationManager(calendarManager);
        HotelManager hotelManager = new HotelManager(reservationManager, calendarManager);
        Hotel hotel = hotelManager.createHotel(hotelDirector,"Relais Tiffany", "Firenze", "via Guido Monaco 5", null, null, HotelRating.THREE_STAR_HOTEL, 1, 2, 1);
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

        //cambio le date della prenotazione
        LocalDate newCheckInDate = LocalDate.of(2023,12,24);
        LocalDate newCheckOutDate = LocalDate.of(2023,12,26);
        reservationManager.updateReservation(reservation.getId(), null, newCheckInDate, newCheckOutDate);

        //controllo che le date della precedente prenotazione siano disponibili e che non lo siano le date aggiornate
        LocalDate otherCheckInDate = LocalDate.of(2023, 12, 26);
        LocalDate otherCheckOutDate = LocalDate.of(2023, 12, 29);
        Research otherResearch = new Research("Firenze", otherCheckInDate, otherCheckOutDate, 2);
        Reservation reservation2 = reservationManager.createReservation(guest1,otherResearch,hotel, room.getId(),"king size bed");
        List<Reservation> guestReservations = reservationManager.getReservations(guest1);
        List<Reservation> hotelReservations = reservationManager.getAllReservations(hotel);
         */
    }
}