package service_layer;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import domain_model.*;
import utilities.Subject;

public class CalendarManager extends Subject {
    private final Map<String, HotelCalendar> calendars; //mappa fra id degli hotel e calendari
    private final Scanner scanner;

    public CalendarManager(Scanner scanner){
        this.scanner = scanner;
        this.calendars = new HashMap<>();
    }
    public HotelCalendar createCalendar(ArrayList<Room> rooms, String hotelID, HotelManager director){
        HotelCalendar calendar = new HotelCalendar(director);

        LocalDate startDate = LocalDate.of(LocalDate.now().getYear(), 1,1);
        LocalDate endDate = LocalDate.of(LocalDate.now().getYear(), 12,31);

        for(LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)){
            for (Room room : rooms) {
                String roomID = room.getId();
                RoomInfo roomInfo = new RoomInfo(roomID);
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
            System.out.print("Please insert the number of days you want to see:");
            numDaysToShow = scanner.nextInt();
            scanner.nextLine();
            calendar.displayCalendar(numDaysToShow);
        }else
            System.out.println("Please first choose a hotel of reference...");
    }

    public void modifyPrice(Hotel hotel){
        if (hotel != null) {
            double price;
            RoomInfo roomInfo = modifyRoom(hotel.getId());

            if (roomInfo != null) {
                System.out.print("Please insert the new price:");
                price = scanner.nextDouble();
                roomInfo.setPrice(price);
                setChanged();
                notifyObservers("Price changed");
            } else
                System.out.println("Sorry room not found!");
        }else
            System.out.println("Please first choose a hotel of reference...");

    } //TODO lui manda notifiche

    public void closeRoom(Hotel hotel){
        if(hotel != null) {
            RoomInfo roomInfo = modifyRoom(hotel.getId());

            if (roomInfo != null) {
                roomInfo.setAvailability(false);
                setChanged();
                notifyObservers("Availability changed");
            } else
                System.out.println("Sorry room not found!");
        }else
            System.out.println("Please first choose a hotel of reference...");

    } //TODO lui manda notifiche

    public void insertMinimumStay(Hotel hotel){
        if(hotel != null) {
            int minStay;
            RoomInfo roomInfo = modifyRoom(hotel.getId());

            if (roomInfo != null) {
                System.out.print("Please insert the new minimum days to stay:");
                minStay = scanner.nextInt();
                roomInfo.setMinimumStay(minStay);
                setChanged();
                notifyObservers("Minimum days changed");
            } else
                System.out.println("Sorry room not found!");
        }else
            System.out.println("Please first choose a hotel of reference...");

    } //TODO lui manda notifiche

    private RoomInfo modifyRoom(String hotelID) {
        String date, roomID;
        HotelCalendar calendar = calendars.get(hotelID); //Forse calendar va messo come campo della classe

        System.out.println("Please insert the date and the room ID: ");
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
                    System.out.println("Room with ID " + roomID + "not found for the specified date");
            }else
                System.out.println("No rooms found for the specified date");

        } catch (DateTimeException e) {
            System.out.println("Invalid date format. Please use yyyy/mm/dd.");
        } catch (NullPointerException e) {
            System.out.println("Error: " + e.getMessage());
        }

        return null;
    }

}
