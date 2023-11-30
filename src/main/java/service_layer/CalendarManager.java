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
    public void displayCalendar(Hotel hotel) {
        if (hotel != null) {
            HotelCalendar calendar = calendars.get(hotel.getId());

            int numDaysToShow; //se centra aumentiamo il numero dei giorni
            System.out.print("Please insert the number of days you want to see: ");
       //     numDaysToShow = Integer.parseInt(scanner.nextLine());

       //     calendar.displayCalendar(numDaysToShow);
        }else
            System.out.println("Please first choose a hotel of reference...");
    }
    public void modifyPrice(Hotel hotel){
        if (hotel != null) {
            double price;
            RoomInfo roomInfo = getRoomInfo(hotel.getId());

            if (roomInfo != null) {
                System.out.print("Please insert the new price:");
         //       price = scanner.nextDouble();
         //       roomInfo.setPrice(price);
                setChanged();
                notifyObservers("Room price changed");
            } else
                System.out.println("Sorry room not found!");
        }else
            System.out.println("Please first choose a hotel of reference...");

    }
    public void closeRoom(Hotel hotel){
        if(hotel != null) {
            RoomInfo roomInfo = getRoomInfo(hotel.getId());

            if (roomInfo != null) {
                roomInfo.setAvailability(false);
                setChanged();
                notifyObservers("Room availability changed");
            } else
                System.out.println("Sorry room not found!");
        }else
            System.out.println("Please first choose a hotel of reference...");

    }

    public void insertMinimumStay(Hotel hotel){
        if(hotel != null) {
            int minStay;
            RoomInfo roomInfo = getRoomInfo(hotel.getId());

            if (roomInfo != null) {
                System.out.print("Please insert the new minimum days to stay:");
    //            minStay = scanner.nextInt();
    //            roomInfo.setMinimumStay(minStay);
                setChanged();

            } else
                System.out.println("Sorry room not found!");
        }else
            System.out.println("Please first choose a hotel of reference...");
    }

    //Region helpers
    private RoomInfo getRoomInfo(String hotelID) {

        String date, roomID;
        HotelCalendar calendar = calendars.get(hotelID);

    /*    System.out.println("Please insert the date and the room ID: ");
        System.out.print("Date (yyyy/mm/dd):");
        date = scanner.nextLine();

        System.out.print("RoomID:");
        roomID = scanner.nextLine();

        try {
            LocalDate parsedDate = LocalDate.parse(date);
            Map<String, RoomInfo> roomInfoMap = calendar.getRoomStatusMap().get(parsedDate);

            if (roomInfoMap != null) {
                RoomInfo roomInfo = roomInfoMap.get(roomID);

                if (roomInfo != null)
                    return roomInfo;
                else
                    System.out.println("Room with ID: " + roomID + " not found for the specified date");
            }else
                System.out.println("No rooms found for the specified date");

        } catch (DateTimeException e) {
            System.out.println("Invalid date format. Please use yyyy/mm/dd.");
        } catch (NullPointerException e) {
            System.out.println("Error: " + e.getMessage());
        }
    */
        return null;
    }

    public HotelCalendar getCalendarByHotelID(String id) {
        return calendars.get(id);
    }
    //EndRegion

}
