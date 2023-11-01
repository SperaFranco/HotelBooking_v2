package domain_model;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

public class HotelCalendar implements Observer {
    //TODO manca da realizzare la logica di observer insieme alle prenotazioni
    //(Da capire meglio perchè gli update in realtà si potrebbero fare anche per altre condizioni) --> hotelCalender fà esso stesso da Observable per Room?

    //Region fields
    private Map<LocalDate, Map<String, Room>> roomStatusMap;
    private Hotel hotel;
    //end Region

    public HotelCalendar() {
        roomStatusMap = new HashMap<>();
        hotel.addObserver(this);
    }

    public void addRoom(LocalDate date, String roomNumber, Room room) {
        Map<String, Room> roomStatus = new HashMap<>();
        roomStatus.put(roomNumber, room);
        roomStatusMap.put(date, roomStatus);
    }

    @Override
    public void update(Observable o, Object arg) {
        //Aggiorna il calendario quando viene effettuata una prenotazione
        Reservation reservation = (Reservation) arg;
        Room roomReserved = reservation.getRoomReserved();
        LocalDate checkInDate = reservation.getCheckIn();
        LocalDate checkOutDate = reservation.getCheckOut();

        //aggiorna lo stato della camera per le date di check-in e check-out
        for (LocalDate date = checkInDate; !date.isAfter(checkOutDate); date = date.plusDays(1)){
            roomReserved.setAvailability(false);
        }
    }
}