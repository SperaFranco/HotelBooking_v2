package domain_model;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class HotelCalendar {
    //TODO manca da realizzare la logica di observer insieme alle prenotazioni
    private int year;
    private Map<LocalDate, Map<String, Room>> roomStatusMap;

    public HotelCalendar() {
        roomStatusMap = new HashMap<>();
    }

    public void addRoom(LocalDate date, String roomNumber, Room room) {
        Map<String, Room> roomStatus = new HashMap<>();
        roomStatus.put(roomNumber, room);
        roomStatusMap.put(date, roomStatus);
    }

}
