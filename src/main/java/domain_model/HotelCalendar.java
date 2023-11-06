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
    private Map<LocalDate, Map<String, RoomInfo>> roomStatusMap;
    private ReservationManager reservation;
    private HotelManager manager;
    //end Region

    public HotelCalendar() {
        roomStatusMap = new HashMap<>();
        manager.addObserver(this);
        reservation.addObserver(this);
    }

    public void addRoomToCalendar(LocalDate date, String roomNumber, RoomInfo roomInfo) {
        Map<String, RoomInfo> roomStatus = new HashMap<>();
        roomStatus.put(roomNumber, roomInfo);
        roomStatusMap.put(date, roomStatus);
    }


    @Override
    public void update(Subject subject, Object argument, String message) {
        if (argument instanceof Reservation) {
            Reservation reservation = (Reservation)argument; //faccio direttamente il cast!
            this.updateAvailability(reservation);
        }
    }

    //Region Helper Methods
    private void updateAvailability(Reservation reservation) {
        //Aggiorna il calendario quando viene effettuata una prenotazione
        String roomReservedID = reservation.getRoomReserved().getId();
        LocalDate checkInDate = reservation.getCheckIn();
        LocalDate checkOutDate = reservation.getCheckOut();

        //aggiorna lo stato della camera per le date di check-in e check-out
        for (LocalDate date = checkInDate; !date.isAfter(checkOutDate); date = date.plusDays(1)){
            roomStatusMap.get(date).get(roomReservedID).setAvailability(false);
        }
    }
    private void updateHotels(Subject subject, Object argument, String message) {
        //Qui i casi che ci interessano sono quando è stato modificato qualcosa relativo alle camere
    }
    //end Helpers Methos
}
