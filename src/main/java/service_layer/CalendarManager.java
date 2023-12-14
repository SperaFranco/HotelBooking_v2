package service_layer;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

import data_access.HotelCalendarDAO;
import domain_model.*;
import utilities.Subject;

public class CalendarManager extends Subject {
    private final HotelCalendarDAO hotelCalendarDAO;

    public CalendarManager(){
        this.hotelCalendarDAO = new HotelCalendarDAO();
    }

    public HotelCalendar createCalendar(ArrayList<Room> rooms, String hotelID, HotelManager hotelManager, ReservationManager reservationManager){
        HotelCalendar calendar = new HotelCalendar(hotelID, hotelManager, reservationManager);

        LocalDate startDate = LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonth(), LocalDate.now().getDayOfMonth());
        LocalDate endDate = LocalDate.of(LocalDate.now().plusYears(1).getYear(), LocalDate.now().plusYears(1).getMonth(),
                LocalDate.now().plusYears(1).getDayOfMonth());

        for(LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)){
            for (Room room : rooms) {
                String roomID = room.getId();
                RoomInfo roomInfo = new RoomInfo(hotelID, roomID, date);
                calendar.addRoomToCalendar(date, roomID, roomInfo);
            }
        }
        //FIXME aggiungere DAO pattern
        return calendar;
    }
    public void modifyPrice(Hotel hotel, LocalDate date, String roomID, double price){
        if (hotel == null)
            throw new RuntimeException("hotel is null");
        try {
            hotelCalendarDAO.setPrice(hotel.getId(), date.toString(), roomID, price);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        setChanged();
        notifyObservers("Room price changed");

    }
    public void closeRoom(Hotel hotel, LocalDate date, String roomID){

        if(hotel == null)
            throw new RuntimeException("hotel il null");
        try {
            hotelCalendarDAO.setAvailability(hotel.getId(), date.toString(), roomID, "not available");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        setChanged();
        notifyObservers("Room availability changed");

    }
    public void setMinimumStay(Hotel hotel, LocalDate date, String roomID, int minStay){

        if(hotel == null)
            throw new RuntimeException("hotel il null");
        try {
            hotelCalendarDAO.setMinimumStay(hotel.getId(), date.toString(), roomID, minStay );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        setChanged();

    }
    public int getMinimumStay(Hotel hotel, LocalDate date, String roomID){

        if(hotel == null) throw new RuntimeException("hotel il null");
        return hotelCalendarDAO.getMinimumStay();

    }

    //Region helpers

    public HotelCalendarDAO getCalendarDAO() {
        return hotelCalendarDAO;
    }

    public double getPrice(String hotelID, String date, String roomID) {
        try {
            return hotelCalendarDAO.getPrice(hotelID, date, roomID);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    //EndRegion

}
