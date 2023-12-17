package service_layer;

import data_access.HotelDAO;
import data_access.RoomDAO;
import domain_model.*;
import utilities.*;

import java.sql.SQLException;
import java.util.*;

public class HotelManager {
    //Classe per la gestione e la modifica delle strutture degli hotel
    private final HotelDAO hotelDAO;
    private final RoomDAO roomDAO;
    private final CalendarManager calendarManager;
    private final ReservationManager reservationManager;
    public HotelManager(CalendarManager calendarManager, ReservationManager reservationManager){
        this.calendarManager = calendarManager;
        this.reservationManager = reservationManager;
        this.hotelDAO = new HotelDAO();
        this.roomDAO = hotelDAO.getRoomDAO();
    }

    public Hotel createHotel(HotelDirector hotelDirector,String name, String city, String address, String telephone, String description, HotelRating rating, int numSingleRooms, int numDoubleRooms, int numTripleRooms){
        Hotel newHotel = new Hotel(IdGenerator.generateHotelID(city),
                name, city, address, telephone, rating, description, hotelDirector.getId());
        ArrayList<Room> rooms = createRooms(newHotel.getId(), numSingleRooms, numDoubleRooms, numTripleRooms);
        newHotel.setRooms(rooms);
        return newHotel;
    }
    public void addHotel(Hotel hotel){
        try {
            hotelDAO.addHotel(hotel);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void removeHotel(Hotel hotel) {
        //cancellare un hotel richiede di eliminare tutte le prenotazioni attive per quella struttura
        if(hotel == null) throw new RuntimeException("null reference to hotel");
        //TODO modificare questa parte --> tolto un hotel che facciamo delle sue prenotazioni?
        //--> le eliminiamo con i trigger (??)
        try {
            hotelDAO.removeHotel(hotel);
            //questo è il fresco di zona
            for (Reservation reservation : reservationManager.getAllReservations(hotel))
                reservationManager.deleteReservation(reservation);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public ArrayList<Hotel> doHotelResearch(Research researchInfo) {
        ArrayList<Hotel> filteredHotels = new ArrayList<>();
        ArrayList<String> allHotels = null;
        try {
            allHotels = hotelDAO.filterHotels(researchInfo.getCity());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        for(String hotel:allHotels) {
            if (!getRoomsAvailable(hotel, researchInfo).isEmpty()) {
                try {
                    filteredHotels.add(hotelDAO.getHotelByID(hotel));
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return filteredHotels;
    }
    public ArrayList<Room> getRoomsAvailable(String hotelID, Research research) {
        ArrayList<Room> roomsAvailable = new ArrayList<>();

        //Prendo le camere di un hotel
        ArrayList<Room> rooms = null;
        try {
            rooms = roomDAO.getRoomsByHotelID(hotelID);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        //Le filtro se non possono ospitare il numero di ospiti
        for (Room room : rooms) {
            try {
                if(roomDAO.canRoomAccomodate(room.getId(), research.getNumOfGuest()))
                    roomsAvailable.add(room);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        //le filtro se non sono disponibili
        roomsAvailable.removeIf(room -> !calendarManager.isRoomAvailable(hotelID, research, room.getId()));

        return roomsAvailable;
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
        try {
            return hotelDAO.getHotelsByDirector(director.getId());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //End Region
}
