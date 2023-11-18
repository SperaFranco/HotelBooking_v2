package service_layer;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;
import java.util.Scanner;

import domain_model.HotelCalendar;
import domain_model.Room;
import domain_model.RoomInfo;
import utilities.Subject;

public class CalendarManager extends Subject {
    private HotelCalendar calendar;
    private final Scanner scanner;

    public CalendarManager(Scanner scanner){
        this.scanner = scanner;
    }
    public void createCalendar(ArrayList<Room> rooms){
        calendar = new HotelCalendar();

        LocalDate startDate = LocalDate.of(LocalDate.now().getYear(), 1,1);
        LocalDate endDate = LocalDate.of(LocalDate.now().getYear(), 12,31);

        for(LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)){
            for (Room room : rooms) {
                String roomID = room.getId();
                RoomInfo roomInfo = new RoomInfo(roomID);
                calendar.addRoomToCalendar(date, roomID, roomInfo);
            }
        }
        setChanged();
        notifyObservers("Calendar created");
    }

    public void displayCalendar() {
        int numDaysToShow; //se centra aumentiamo il numero dei giorni
        System.out.print("Please insert the number of days you want to see:");
        numDaysToShow = scanner.nextInt();
        scanner.nextLine();

        Map<LocalDate, Map<String, RoomInfo>> mapCalendar = calendar.getRoomStatusMap();
        if(mapCalendar != null) {
            StringBuilder calendarDisplay = new StringBuilder();
            LocalDate date = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd");

            for (int i = 0; i < numDaysToShow; i++) {
                LocalDate displayDate = date.plusDays(i);
                int length = 0;
                if (mapCalendar.containsKey(displayDate)) {
                    calendarDisplay.append("Date: ").append(displayDate.format(formatter)).append("\n");

                    for (Map.Entry<String, RoomInfo> entry : mapCalendar.get(displayDate).entrySet()) {
                        RoomInfo roomInfo = entry.getValue();
                        String line = String.format(" - Room %-5s " +
                                        "| Availability: %-5s | Price: %-7.2f | Minimum Stay: %-2d",
                                roomInfo.getRoomID(), roomInfo.isAvailable(), roomInfo.getPrice(), roomInfo.getMinimumStay());
                        calendarDisplay.append(line).append("\n");
                        length = line.length();
                    }
                    calendarDisplay.append("\n");
                }
                if (i != numDaysToShow - 1)
                    calendarDisplay.append("-".repeat(length));
            }
            System.out.println(calendarDisplay);
        }
        else
            System.out.println("Calendar not present...");
    }

    public void modifyPrice(){
        double price;
        RoomInfo roomInfo = modifyRoom();

        if(roomInfo != null) {
            System.out.print("Please insert the new price:");
            price = scanner.nextDouble();
            roomInfo.setPrice(price);
            setChanged();
            notifyObservers("Price changed");
        }
        else
            System.out.println("Sorry room not found!");
    }

    public void closeRoom(){
        RoomInfo roomInfo = modifyRoom();

        if(roomInfo != null) {
            roomInfo.setAvailability(false);
            setChanged();
            notifyObservers("Availability changed");
        }
        else
            System.out.println("Sorry room not found!");
    }

    public void insertMinimumStay(){
        int minStay;
        RoomInfo roomInfo = modifyRoom();

        if (roomInfo != null) {
            System.out.print("Please insert the new minimum days to stay:");
            minStay = scanner.nextInt();
            roomInfo.setMinimumStay(minStay);
            setChanged();
            notifyObservers("Minimum days changed");
        }
        else
            System.out.println("Sorry room not found!");
    }

    private RoomInfo modifyRoom() {
        String date, roomID;

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
