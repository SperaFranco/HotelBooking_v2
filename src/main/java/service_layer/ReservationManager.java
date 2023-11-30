package service_layer;
import domain_model.*;
import utilities.IdGenerator;
import utilities.Research;
import utilities.Subject;

import java.time.LocalDate;
import java.util.*;

public class ReservationManager extends Subject {
    private final Map<String, Reservation> reservationMap;
    private final AccountManager accountManager; //occhio ai controllers
    private final CalendarManager calendarManager;

    public ReservationManager(AccountManager accountManager, CalendarManager calendarManager) {
        reservationMap = new HashMap<>();
        this.accountManager = accountManager;
        this.calendarManager = calendarManager;
    }
    public void doReservation(Guest user, Research info, Hotel hotel, String roomID, String description) {
        if(user == null) throw new RuntimeException("user is null");
        Reservation newReservation = new Reservation(IdGenerator.generateReservationID(hotel.getId(), user.getName(), user.getSurname(), info.getCheckIn()),
                info.getCheckIn(), info.getCheckOut(), info.getNumOfGuest(), description, hotel.getId(), roomID, user.getId());
        addReservation(newReservation);
    }
    public void addReservation(Reservation newReservation) {
        //TODO guardare come fare per mandare email di notifica prenotazione (qui o nel doReservation)
        reservationMap.put(newReservation.getId(), newReservation);
        setChanged();
        notifyObservers(newReservation,"Add reservation");
    }
    public void updateReservation(String id, String newDescription, LocalDate newCheckInDate, LocalDate newCheckOutDate) {

        Reservation reservation = findReservationById(id);
        if (reservation == null) throw new RuntimeException("reservation not found");
        if(newDescription!=null)
            reservation.setNotes(newDescription);
        if(newCheckInDate!=null && newCheckOutDate!=null){
            HotelCalendar calendar = calendarManager.getCalendarByHotelID(reservation.getHotel());
            calendar.setRoomAvailability(reservation.getRoomReserved(), reservation.getCheckIn(), reservation.getCheckOut(), true);
            if(!calendar.isRoomAvailable(newCheckInDate, newCheckOutDate, reservation.getRoomReserved())) {
                calendar.setRoomAvailability(reservation.getRoomReserved(), reservation.getCheckIn(), reservation.getCheckOut(), false);
                throw new RuntimeException("the dates requested are not available");
            }
            calendar.setRoomAvailability(reservation.getRoomReserved(), newCheckInDate, newCheckOutDate, false);
            reservation.setCheckIn(newCheckInDate);
            reservation.setCheckOut(newCheckOutDate);
        }
    }
    public void deleteReservation(String id) {
        Reservation reservationRemoved = findReservationById(id);
        if(reservationRemoved==null) throw new RuntimeException("reservation not found");
        reservationMap.remove(id);
        setChanged();
        notifyObservers(reservationRemoved, "Delete reservation");
    }
    public List<Reservation> getReservations(Guest guest) {
        List<Reservation> reservations = new ArrayList<>();
        for (Reservation reservation: findReservationByGuest(guest.getId()))
            reservations.add(reservation);
        return reservations;
    }
    public List<Reservation> getAllReservations(Hotel hotel) {
        if (hotel == null) throw new RuntimeException("hotel is a null reference");
        ArrayList<Reservation> reservationsForHotel = new ArrayList<>();
        //TODO ricontrollare questo ciclo
        for (Map.Entry<String, Reservation> entry : reservationMap.entrySet()) {
            Reservation reservation = entry.getValue();
            if (reservation.getHotel().equals(hotel.getId()))
                reservationsForHotel.add(reservation);
        }
        return reservationsForHotel;
    }

    //Region helpers
    private Reservation findReservationById(String id) {
        return reservationMap.get(id);
    }
    private ArrayList<Reservation> findReservationByGuest(String guestID) {
        ArrayList<Reservation> reservations = new ArrayList<>(reservationMap.values());
        ArrayList<Reservation> myReservations = new ArrayList<>();

        for (Reservation reservation : reservations) {
            if (reservation.getClient().equals(guestID)) {
                myReservations.add(reservation);
            }
        }
        return myReservations;
    }

    //End Region helpers
}

