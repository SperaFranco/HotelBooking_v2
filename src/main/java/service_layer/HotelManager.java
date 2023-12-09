package service_layer;

import data_access.HotelDAO;
import domain_model.*;
import utilities.*;

import java.sql.SQLException;
import java.util.*;

public class HotelManager extends Subject {
    //Classe per la gestione e la modifica delle strutture degli hotel

    private final ReservationManager reservationManager;
    private final CalendarManager calendarManager;
    private final HotelDAO hotelDAO;

    public HotelManager(ReservationManager reservationManager, CalendarManager calendarManager){
        this.reservationManager = reservationManager;
        this.calendarManager = calendarManager;
        this.hotelDAO = new HotelDAO(calendarManager);
    }

    public Hotel createHotel(HotelDirector hotelDirector,String name, String city, String address, String telephone, String description, HotelRating rating, int numSingleRooms, int numDoubleRooms, int numTripleRooms){
        Hotel newHotel = new Hotel(IdGenerator.generateHotelID(city),
                name, city, address, telephone, rating, description, hotelDirector.getId(), calendarManager);
        ArrayList<Room> rooms = createRooms(newHotel.getId(), numSingleRooms, numDoubleRooms, numTripleRooms);
        newHotel.setRooms(rooms);
        HotelCalendar calendar = calendarManager.createCalendar(rooms, newHotel.getId(), this, reservationManager);
        newHotel.setCalendar(calendar);
        return newHotel;
    }
    public void addHotel(Hotel hotel){
        try {
            hotelDAO.addHotel(hotel);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        setChanged();
        notifyObservers(hotel, "New hotel added");
    }
    public void removeHotel(Hotel hotel) {
        //cancellare un hotel richiede di eliminare tutte le prenotazioni attive per quella struttura
        if(hotel == null) throw new RuntimeException("null reference to hotel");
        //TODO modificare questa parte
        List<Reservation> reservationList = reservationManager.getAllReservations(hotel);
        for(Reservation reservation:reservationList)
            reservationManager.deleteReservation(reservation.getId());
        try {
            hotelDAO.removeHotel(hotel.getId());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        setChanged();
        notifyObservers(hotel, "Hotel removed");
    }
    public ArrayList<Hotel> doHotelResearch(Research researchInfo) {
        ArrayList<Hotel> filteredHotels = new ArrayList<>();
        ArrayList<Hotel> allHotels = null;
        try {
            allHotels = hotelDAO.getAllHotels();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        for(Hotel hotel:allHotels) {
            if (hotel.getCity().equalsIgnoreCase(researchInfo.getCity())) {
                if (hotel.isHotelAvailable(researchInfo))
                    filteredHotels.add(hotel);
            }
        }
        return filteredHotels;
    }

    //Region Helpers
    private ArrayList<Room> createRooms(String id, int singleRooms, int doubleRooms, int tripleRooms){
        ArrayList<Room> rooms = new ArrayList<>();
        int[] numRooms = RoomType.setNumberOfRoomsPerType(singleRooms, doubleRooms, tripleRooms); //creo il numero di camere singole - doppie - triple
        RoomType[] types = RoomType.values();
        IdGenerator.resetRoomCounter(); //rimette a 1 il counter delle stanze per l'hotel

        for (int i = 0; i < numRooms.length; i++) {
            for (int j = 0; j < numRooms[i]; j++) {
                String description = "";
                String roomID;
                roomID = IdGenerator.generateRoomID(id, types[i]);
                Room room = new Room(roomID, types[i], description);
                rooms.add(room);
            }
        }
        return rooms;
    }
    private ArrayList<Hotel> findHotelsByDirector(HotelDirector director) {
        ArrayList<Hotel> hotelsByDirector = new ArrayList<>();
        ArrayList<Hotel> allHotels = null;
        try {
            allHotels = hotelDAO.getAllHotels();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        for (Hotel hotel: allHotels) {
            if (hotel.getDirectorID().equals(director.getId())){
                hotelsByDirector.add(hotel);
            }
        }

        return hotelsByDirector;
    }
    //End Region

    protected Map<String, Hotel> getHotelMap() {
        return null;
    }
}
