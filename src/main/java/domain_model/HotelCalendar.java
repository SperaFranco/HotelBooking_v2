package domain_model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

import service_layer.CalendarManager;
import service_layer.HotelManager;
import service_layer.ReservationManager;
import utilities.Observer;
import utilities.Research;
import utilities.Subject;

public class HotelCalendar implements Observer {
    //(Da capire meglio perchè gli update in realtà si potrebbero fare anche per altre condizioni) --> hotelCalender fà esso stesso da Observable per Room?

    //Region fields
    private final String hotelID;
    private final CalendarManager calendarManager;
    private final LocalDate startDate;
    private final LocalDate endDate;

    //end Region

    public HotelCalendar(String hotelID, LocalDate startDate, LocalDate endDate, ReservationManager reservationManager, CalendarManager calendarManager) {
        this.hotelID = hotelID;
        reservationManager.addObserver(this);
        this.calendarManager = calendarManager;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getHotelID() {
        return hotelID;
    }
    public LocalDate getStartDate() {
        return startDate;
    }
    public LocalDate getEndDate() {
        return endDate;
    }

    @Override
    public void update(Subject subject, Object argument, String message) {
        Reservation reservation = (Reservation)argument;
        String roomReservedID = reservation.getRoomReserved();
        LocalDate newCheckInDate = reservation.getCheckIn();
        LocalDate newCheckOutDate = reservation.getCheckOut();

        //Aggiorna il calendario quando viene effettuata una prenotazione
        if (message.contains("Add reservation")) {
            //nei giorni prenotati la camera va settata con disponibilità a falso
            //aggiorna lo stato della camera per le date di check-in e check-out
            setRoomAvailability(roomReservedID, newCheckInDate, newCheckOutDate, false);
        }else if(message.contains("Delete reservation")) {
            //nei giorni in cui la camera era prenotata la camera va settata con disponibilità a true
            //aggiorna lo stato della camera per le date di check-in e check-out
            setRoomAvailability(roomReservedID, newCheckInDate, newCheckOutDate, true);
        }
    }

    private void setRoomAvailability(String roomID, LocalDate checkIn, LocalDate checkOut, boolean availability) {
        //TODO il giorno di check-out è da considerare compreso o no?
        for (LocalDate date = checkIn; !date.isAfter(checkOut); date = date.plusDays(1)) {
            calendarManager.setAvailability(hotelID, date.toString(), roomID, availability);
        }
    }

}
