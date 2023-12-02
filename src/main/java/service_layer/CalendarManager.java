package service_layer;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.*;

import domain_model.*;
import utilities.Subject;

public class CalendarManager extends Subject {
    private final Map<String, HotelCalendar> calendars; //mappa fra id degli hotel e calendari

    public CalendarManager(){
        this.calendars = new HashMap<>();
    }
    public HotelCalendar createCalendar(ArrayList<Room> rooms, String hotelID, HotelManager hotelManager, ReservationManager reservationManager){
        HotelCalendar calendar = new HotelCalendar(hotelID, hotelManager, reservationManager);

        LocalDate startDate = LocalDate.of(LocalDate.now().getYear(), 1,1);
        LocalDate endDate = LocalDate.of(LocalDate.now().getYear(), 12,31);

        for(LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)){
            for (Room room : rooms) {
                String roomID = room.getId();
                RoomInfo roomInfo = new RoomInfo(hotelID, roomID, date);
                calendar.addRoomToCalendar(date, roomID, roomInfo);
            }
        }

        this.calendars.put(hotelID, calendar);
        return calendar;
    }
    public void modifyPrice(Hotel hotel, LocalDate date, String roomID, double price){

        if (hotel == null) throw new RuntimeException("hotel is null");
        RoomInfo roomInfo = getRoomInfo(hotel.getId(), date, roomID);
        if (roomInfo == null) throw new RuntimeException("roomInfo is null");
        roomInfo.setPrice(price);
        setChanged();
        notifyObservers("Room price changed");

    }
    public void closeRoom(Hotel hotel, LocalDate date, String roomID){

        if(hotel == null) throw new RuntimeException("hotel il null");
        RoomInfo roomInfo = getRoomInfo(hotel.getId(), date, roomID);
        if (roomInfo == null) throw new RuntimeException("roomInfo is null");
        roomInfo.setAvailability(false);
        setChanged();
        notifyObservers("Room availability changed");

    }
    //TODO aggiornare il codice affinché le camere con minimum stay maggiore a uno compaiano solo nelle ricerche che rispettano il vincolo
    public void insertMinimumStay(Hotel hotel, LocalDate date, String roomID, int minStay){

        if(hotel == null) throw new RuntimeException("hotel il null");
        RoomInfo roomInfo = getRoomInfo(hotel.getId(), date, roomID);
        if (roomInfo == null) throw new RuntimeException("roomInfo is null");
        roomInfo.setMinimumStay(minStay);
        setChanged();

    }
    //Region helpers
    private RoomInfo getRoomInfo(String hotelID, LocalDate date, String roomID) {

        HotelCalendar calendar = calendars.get(hotelID);
        Map<String, RoomInfo> roomInfoMap = calendar.getRoomStatusMap().get(date);
        if (roomInfoMap == null) throw new RuntimeException("roomInfoMap is null");
        RoomInfo roomInfo = roomInfoMap.get(roomID);
        if (roomInfo == null) throw new RuntimeException("roomInfo is null");
        return roomInfo;

    }
    public HotelCalendar getCalendarByHotelID(String id) {
        return calendars.get(id);
    }
    //EndRegion

}
