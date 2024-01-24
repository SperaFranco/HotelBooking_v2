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
    private static CalendarManager calendarManager;
    private static ReservationManager reservationManager;
    private static HotelManager hotelManager;

    private HotelManager(CalendarManager calendarManager, ReservationManager reservationManager){
        HotelManager.calendarManager = calendarManager;
        HotelManager.reservationManager = reservationManager;
        this.hotelDAO = new HotelDAO();
    }
    public static HotelManager createHotelManager(CalendarManager calendarManager, ReservationManager reservationManager){
        if(hotelManager == null)
            hotelManager = new HotelManager(calendarManager, reservationManager);
        return hotelManager;
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
            if(findHotelByID(hotel.getId()) != null)
                throw new RuntimeException("hotel già esistente");
            hotelDAO.addHotel(hotel);
        } catch (SQLException | RuntimeException e) {
            throw new RuntimeException(e);
        }
    }
    public void removeHotel(Hotel hotel) {
        //cancellare un hotel richiede di eliminare tutte le camere e le prenotazioni attive per quella struttura
        if(hotel == null) throw new RuntimeException("null reference to hotel");
        try {
            hotelDAO.removeHotel(hotel);
            for (Reservation reservation : reservationManager.getAllReservations(hotel))
                reservationManager.deleteReservation(reservation);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public ArrayList<Hotel> doHotelResearch(Research researchInfo) {
        ArrayList<Hotel> filteredHotels = new ArrayList<>();
        ArrayList<String> hotelIDsFilteredByCity = null;
        try {
            hotelIDsFilteredByCity = hotelDAO.filterHotels(researchInfo.getCity());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        //Lascio poi solo gli hotel che hanno almeno una camera disponibile secondo i parametri di ricerca
        for(String hotel:hotelIDsFilteredByCity) {
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
        ArrayList<Room> roomsWithRequiredCapacity = new ArrayList<>();
        RoomDAO roomDAO = hotelDAO.getRoomDAO();

        //Prendo tutte le camere di un hotel
        ArrayList<Room> rooms = null;
        try {
            rooms = roomDAO.getRoomsByHotelID(hotelID);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        //Le filtro se non possono ospitare il numero di ospiti
        for (Room room : rooms) {
            if(RoomType.getRoomCapacity(room.getType())>= research.getNumOfGuest())
                roomsWithRequiredCapacity.add(room);
        }

        //le filtro se non sono disponibili
        roomsWithRequiredCapacity.removeIf(room -> !calendarManager.isRoomAvailable(hotelID, research, room.getId()));

        return roomsWithRequiredCapacity;
    }
    public Hotel findHotelByID(String id) {
        try {
            return hotelDAO.getHotelByID(id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //Helpers
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
}
