package domain_model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
    private HotelManager manager; //serve per l'observer --> occhio non è il director
    //end Region

    public HotelCalendar(HotelManager manager) {
        this.manager = manager;
        roomStatusMap = new HashMap<>();
        manager.addObserver(this);
        reservation.addObserver(this);
    }

    public Map<LocalDate,Map<String, RoomInfo>> getRoomStatusMap(){
        return roomStatusMap;
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
        String roomReservedID = reservation.getRoomReserved();
        LocalDate checkInDate = reservation.getCheckIn();
        LocalDate checkOutDate = reservation.getCheckOut();

        //aggiorna lo stato della camera per le date di check-in e check-out
        for (LocalDate date = checkInDate; !date.isAfter(checkOutDate); date = date.plusDays(1)){
            roomStatusMap.get(date).get(roomReservedID).setAvailability(false);
        }
    }

    public boolean isRoomAvailable(LocalDate checkIn, LocalDate checkOut, String roomID) {
        //Controllo se la camera risulta disponibile per tutti i giorni indicati dal checkin al checkout
        //TODO Ci sarebbe anche da fare il controllo per il numero minimo di pernottamenti

        for (LocalDate date = checkIn; !date.isAfter(checkOut); date = date.plusDays(1)) {
            Map<String, RoomInfo> roomInfoMap = roomStatusMap.get(date);
            RoomInfo info = roomInfoMap.get(roomID);
            if(!info.isAvailable())
                //Basta che un giorno sia falso e la camera non è più disponibile
                return false;
        }
        return true;
    }

    public double getTotalPrice(LocalDate checkIn, LocalDate checkOut, String id) {
        double sum = 0;

        for(LocalDate date = checkIn; date.isAfter(checkOut); date = date.plusDays(1)) {
            RoomInfo roomInfo = roomStatusMap.get(checkIn).get(id);
            sum += roomInfo.getPrice();
        }

        return sum;
    }

    public void displayCalendar(int numDaysToShow) {
        StringBuilder calendarDisplay = new StringBuilder();
        LocalDate date = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd");

        for (int i = 0; i < numDaysToShow; i++) {
            LocalDate displayDate = date.plusDays(i);
            int length = 0;
            if (roomStatusMap.containsKey(displayDate)) {

                calendarDisplay.append("Date: ").append(displayDate.format(formatter)).append("\n");
                for (Map.Entry<String, RoomInfo> entry : roomStatusMap.get(displayDate).entrySet()) {
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

    //end Helpers Methods
}
