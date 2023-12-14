package domain_model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

import service_layer.CalendarManager;
import service_layer.HotelManager;
import service_layer.ReservationManager;
import utilities.Observer;
import utilities.Research;
import utilities.Subject;

public class HotelCalendar implements Observer {
    //TODO manca da realizzare la logica di observer insieme alle prenotazioni
    //(Da capire meglio perchè gli update in realtà si potrebbero fare anche per altre condizioni) --> hotelCalender fà esso stesso da Observable per Room?

    //Region fields
    private Map<LocalDate, Map<String, RoomInfo>> roomStatusMap; //todo lui deve stare attento quando prenoto/modifico una prenotazione
    private final String hotelID;
    private final CalendarManager calendarManager;
    //end Region

    public HotelCalendar(String hotelID, HotelManager manager, ReservationManager reservationManager, CalendarManager calendarManager) {
        this.hotelID = hotelID;
        roomStatusMap = new HashMap<>();
        manager.addObserver(this);
        reservationManager.addObserver(this);
        this.calendarManager = calendarManager;
    }

    public Map<LocalDate,Map<String, RoomInfo>> getRoomStatusMap(){
        return roomStatusMap;
    }
    public void setRoomStatusMap(Map<LocalDate, Map<String, RoomInfo>> newMap) {
        this.roomStatusMap = newMap;
    }

    public void addRoomToCalendar(LocalDate date, String roomID, RoomInfo roomInfo) {
        Map<String, RoomInfo> roomStatus = roomStatusMap.get(date);

        if (roomStatus == null) {
            roomStatus = new HashMap<>();
            roomStatusMap.put(date, roomStatus);
        }

        roomStatus.put(roomID, roomInfo);
    }

    public String getHotelID() {
        return hotelID;
    }

    public void setRoomAvailability(String roomID, LocalDate checkIn, LocalDate checkOut, boolean availability) {
        for (LocalDate date = checkIn; !date.isEqual(checkOut); date = date.plusDays(1)) {
            roomStatusMap.get(date).get(roomID).setAvailability(availability);
            //Lo setto anche nel db --> sarebbe da discutere su quale tenere se db o roomStatusMap
            calendarManager.setAvailability(hotelID, date.toString(), roomID, availability);
        }
    }
    public boolean isRoomAvailable(Research researchInfo, String roomID) {
        for (LocalDate date = researchInfo.getCheckIn(); !date.isEqual(researchInfo.getCheckOut()); date = date.plusDays(1)) {
            Map<String, RoomInfo> roomInfoMap = roomStatusMap.get(date);
            RoomInfo info = roomInfoMap.get(roomID);
            if(!info.getAvailability() || researchInfo.getCheckIn().until(researchInfo.getCheckOut(), ChronoUnit.DAYS) < info.getMinimumStay())
                return false;
        }
        return true;
    }

    /*
    public double getTotalPrice(LocalDate checkIn, LocalDate checkOut, String id) {
        double sum = 0;

        for(LocalDate date = checkIn; !date.isEqual(checkOut); date = date.plusDays(1)) {
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
    */
    @Override
    public void update(Subject subject, Object argument, String message) {
        if (argument instanceof Reservation) {
            if( ((Reservation)argument).getHotel().equals(this.hotelID) )
                updateAvailability( ((Reservation)argument), message );
        }
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

    //end Helpers Methods
}
