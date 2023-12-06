package domain_model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
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


    public void addRoomToCalendar(LocalDate date, String roomID, RoomInfo roomInfo) {
        roomStatusMap.get(date).put(roomID, roomInfo);
    }

    public String getHotelID() {
        return hotelID;
    }

    public double getTotalPrice(LocalDate checkIn, LocalDate checkOut, String id) {
        double sum = 0;

        for(LocalDate date = checkIn; !date.isEqual(checkOut); date = date.plusDays(1)) {
            RoomInfo roomInfo = roomStatusMap.get(checkIn).get(id);
            sum += roomInfo.getPrice();
        }

        return sum;
    }

    @Override
    public void update(Subject subject, Object argument, String message) {
        if (argument instanceof Reservation) {
            if( ((Reservation)argument).getHotel().equals(this.hotelID) )
                updateAvailability( ((Reservation)argument), message );
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
        for (LocalDate date = checkIn; !date.isEqual(checkOut); date = date.plusDays(1)) {
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

        for (LocalDate date = researchInfo.getCheckIn(); !date.isEqual(researchInfo.getCheckOut()); date = date.plusDays(1)) {
            Map<String, RoomInfo> roomInfoMap = roomStatusMap.get(date);
            RoomInfo info = roomInfoMap.get(roomID);
            if(!info.getAvailability() || researchInfo.getCheckIn().until(researchInfo.getCheckOut(), ChronoUnit.DAYS) < info.getMinimumStay())
                return false;
        }
        return true;
    }



    //end Helpers Methods
}
