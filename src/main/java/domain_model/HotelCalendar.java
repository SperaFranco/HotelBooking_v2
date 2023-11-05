package domain_model;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import service_layer.HotelManager;
import service_layer.ReservationManager;
import utilities.Observer;
import utilities.Subject;

public class HotelCalendar implements Observer {
    //TODO manca da realizzare la logica di observer insieme alle prenotazioni
    //(Da capire meglio perchè gli update in realtà si potrebbero fare anche per altre condizioni) --> hotelCalender fà esso stesso da Observable per Room?

    //Region fields
    private Map<LocalDate, Map<String, Room>> roomStatusMap;
    private ReservationManager reservation;
    private HotelManager manager;
    //end Region

    public HotelCalendar() {
        roomStatusMap = new HashMap<>();
        manager.addObserver(this);
        reservation.addObserver(this);
    }

    public void addRoomToCalendar(LocalDate date, String roomNumber, Room room) {
        Map<String, Room> roomStatus = new HashMap<>();
        roomStatus.put(roomNumber, room);
        roomStatusMap.put(date, roomStatus);
    }

    @Override
    public void updateAvailability(Subject o, Object arg) {
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

    public void updateReservations(Subject o, Object arg, String message) {
        //Fake implementation ??
    }

    @Override
    public void updateHotels(Subject subject, Object argument, String message) {
        //Qui i casi che ci interessano sono quando è stato modificato qualcosa relativo alle camere
    }
}
