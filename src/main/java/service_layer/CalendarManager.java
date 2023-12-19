package service_layer;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

import data_access.HotelCalendarDAO;
import domain_model.*;
import utilities.Research;
import utilities.Subject;

public class CalendarManager{
    private final HotelCalendarDAO hotelCalendarDAO;

    public CalendarManager(){
        this.hotelCalendarDAO = new HotelCalendarDAO();
    }

    public HotelCalendar createCalendar(ArrayList<Room> rooms, String hotelID, ReservationManager reservationManager){
        LocalDate startDate = LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonth(), LocalDate.now().getDayOfMonth());
        LocalDate endDate = LocalDate.of(LocalDate.now().plusYears(1).getYear(), LocalDate.now().plusYears(1).getMonth(),
                LocalDate.now().plusYears(1).getDayOfMonth());
        HotelCalendar hotelCalendar = new HotelCalendar(hotelID, startDate, endDate, reservationManager, this);

        try {
            hotelCalendarDAO.addCalendar(hotelCalendar, rooms);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return hotelCalendar;
    }
    public void removeCalendar(HotelCalendar calendar, ArrayList<Room> rooms) {
        //Devo prendere tutte le camere, l'hotelID e tutte le date fra inizio e fine
        try {
            hotelCalendarDAO.deleteCalendar(calendar, rooms);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void modifyPrice(Hotel hotel, LocalDate date, String roomID, double price){
        if (hotel == null)
            throw new RuntimeException("hotel is null");
        try {
            hotelCalendarDAO.setPrice(hotel.getId(), date.toString(), roomID, price);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public double getPrice(String hotelID, String date, String roomID) {
        try {
            return hotelCalendarDAO.getPrice(hotelID, date, roomID);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void closeRoom(Hotel hotel, LocalDate date, String roomID){
        if(hotel == null)
            throw new RuntimeException("hotel is null");
        setAvailability(hotel.getId(), date.toString(), roomID, false);
    }
    public void setAvailability(String hotelID, String date, String roomID, boolean availability) {
        try {
            hotelCalendarDAO.setAvailability(hotelID, date, roomID, availability ? "available" : "not available");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public boolean getAvailability(String hotelID, String date, String roomID ) {
        try {
            String availabilty = hotelCalendarDAO.getAvailability(hotelID, date, roomID);
            return availabilty.equals("available");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void setMinimumStay(Hotel hotel, LocalDate date, String roomID, int minStay){

        if(hotel == null)
            throw new RuntimeException("hotel is null");
        try {
            hotelCalendarDAO.setMinimumStay(hotel.getId(), date.toString(), roomID, minStay );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public int getMinimumStay(String hotel, LocalDate date, String roomID){
        if(hotel == null)
            throw new RuntimeException("hotel is null");
        return hotelCalendarDAO.getMinimumStay(hotel, date.toString(), roomID);

    }

    public boolean isRoomAvailable(String hotelID, Research research, String roomID) {
        for (LocalDate date = research.getCheckIn(); !date.isAfter(research.getCheckOut()); date = date.plusDays(1)) {
            if (!getAvailability(hotelID, date.toString(), roomID) || !(research.getCheckIn().until(research.getCheckOut(), ChronoUnit.DAYS) >= getMinimumStay(hotelID, date, roomID)))
                return false;
        }
        return true;
    }
}
