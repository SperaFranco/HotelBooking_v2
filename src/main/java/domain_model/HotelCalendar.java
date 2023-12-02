package domain_model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import service_layer.HotelManager;
import service_layer.ReservationManager;
import utilities.Observer;
import utilities.Research;
import utilities.Subject;

public class HotelCalendar implements Observer {
    //TODO manca da realizzare la logica di observer insieme alle prenotazioni
    //(Da capire meglio perchè gli update in realtà si potrebbero fare anche per altre condizioni) --> hotelCalender fà esso stesso da Observable per Room?

    //Region fields
    private final Map<LocalDate, Map<String, RoomInfo>> roomStatusMap; //todo lui deve stare attento quando prenoto/modifico una prenotazione
    private final String hotelID;
    //end Region

    public HotelCalendar(String hotelID, HotelManager manager, ReservationManager reservationManager) {
        this.hotelID = hotelID;
        roomStatusMap = new HashMap<>();
        for(LocalDate date = LocalDate.of(2023,1,1); !date.equals(LocalDate.of(2024, 1, 1)); date=date.plusDays(1) ){
            Map<String, RoomInfo> roomStatus = new HashMap<>();
            roomStatusMap.put(date, roomStatus);
        }
        manager.addObserver(this);
        reservationManager.addObserver(this);
    }

    public Map<LocalDate,Map<String, RoomInfo>> getRoomStatusMap(){
        return roomStatusMap;
    }


    public void addRoomToCalendar(LocalDate date, String roomNumber, RoomInfo roomInfo) {
        //Map<String, RoomInfo> roomStatus = new HashMap<>();
        //FIXME qui c'è un errore, dentro roomStatusMap, per ogni data, compare una sola camera
        //roomStatus.put(roomNumber, roomInfo);
        //roomStatusMap.put(date, roomStatus);
        roomStatusMap.get(date).put(roomNumber, roomInfo);
    }

    public String getHotelID() {
        return hotelID;
    }

    public double getTotalPrice(LocalDate checkIn, LocalDate checkOut, String id) {
        double sum = 0;

        for(LocalDate date = checkIn; !date.isAfter(checkOut); date = date.plusDays(1)) {
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
                            roomInfo.getRoomID(), roomInfo.getAvailability(), roomInfo.getPrice(), roomInfo.getMinimumStay());
                    calendarDisplay.append(line).append("\n");
                    length = line.length();
                }
                calendarDisplay.append("\n");
            }

            if (i != numDaysToShow - 1)
                calendarDisplay.append("-".repeat(length)).append("\n");

        }

        System.out.println(calendarDisplay);
    }
    @Override
    public void update(Subject subject, Object argument, String message) {
        if (argument instanceof Reservation reservation) {
            if(reservation.getHotel().equals(this.hotelID))
                updateAvailability(reservation, message);
        }
        else if(argument instanceof Hotel hotel)
            if (hotel.getId().equals(this.hotelID))
                updateCalendar(message);
    }


    //Region Helper Methods
    private void updateAvailability(Reservation reservation, String message) {
        String roomReservedID = reservation.getRoomReserved();
        LocalDate newCheckInDate = reservation.getCheckIn();
        LocalDate newCheckOutDate = reservation.getCheckOut();

        //Aggiorna il calendario quando viene effettuata una prenotazione
        if (message.contains("Add reservation")) {
            //nei giorni prenotati la camera va settata con disponibilità a falso

            //aggiorna lo stato della camera per le date di check-in e check-out
            setRoomAvailability(roomReservedID, newCheckInDate, newCheckOutDate, false);
        }else if(message.contains("Delete reservation")) {
            //nei giorni in cui la camera era prenotata la camera va settata con disponibilità a true
            //aggiorna lo stato della camera per le date di check-in e check-out
            setRoomAvailability(roomReservedID, newCheckInDate, newCheckOutDate, true);
        }
    }

    public void setRoomAvailability(String roomID, LocalDate checkIn, LocalDate checkOut, boolean availability) {
        for (LocalDate date = checkIn; !date.isAfter(checkOut); date = date.plusDays(1)) {
            roomStatusMap.get(date).get(roomID).setAvailability(availability);
        }
    }

    private void updateCalendar(String message) {
        if (message.contains("Hotel removed")) //controllare
            getRoomStatusMap().clear(); //cancello anche il calendario
    }
    public boolean isRoomAvailable(Research researchInfo, String roomID) {
        //Controllo se la camera risulta disponibile per tutti i giorni indicati dal checkin al checkout
        //TODO Ci sarebbe anche da fare il controllo per il numero minimo di pernottamenti

        for (LocalDate date = researchInfo.getCheckIn(); !date.isAfter(researchInfo.getCheckOut()); date = date.plusDays(1)) {
            Map<String, RoomInfo> roomInfoMap = roomStatusMap.get(date);
            //FIXME il problema è qui
            RoomInfo info = roomInfoMap.get(roomID);
            if(!info.getAvailability())
                //Basta che un giorno sia falso e la camera non è più disponibile
                return false;
        }
        return true;
    }



    //end Helpers Methods
}
