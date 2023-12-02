package service_layer;

import domain_model.*;
import utilities.*;

import java.time.LocalDate;
import java.util.*;

public class HotelManager extends Subject {
    //Classe per la gestione e la modifica delle strutture degli hotel
    private final Map<String, Hotel> hotelMap;
    private final ReservationManager reservationManager;
    private final CalendarManager calendarManager;

    public HotelManager(ReservationManager reservationManager, CalendarManager calendarManager){
        hotelMap = new HashMap<>();
        this.reservationManager = reservationManager;
        this.calendarManager = calendarManager;
    }

    public Hotel createHotel(HotelDirector hotelDirector,String name, String city, String address, String telephone, String email, String description, HotelRating rating, int numSingleRooms, int numDoubleRooms, int numTripleRooms){
        Hotel newHotel = new Hotel(IdGenerator.generateHotelID(city),
                name, city, address, telephone, email, rating, description, hotelDirector.getId(), calendarManager);
        ArrayList<Room> rooms = createRooms(newHotel.getId(), numSingleRooms, numDoubleRooms, numTripleRooms);
        newHotel.setRooms(rooms);
        return newHotel;
    }
    public void addHotel(Hotel hotel){
        HotelCalendar calendar = calendarManager.createCalendar(hotel.getRooms(), hotel.getId(), this, reservationManager);
        hotel.setCalendar(calendar);
        hotelMap.put(hotel.getId(), hotel);
        setChanged();
        notifyObservers(hotel, "New hotel added");
    }
    public void modifyHotel() {
        //TODO nell'hotel manager aggiungiamo pure i metodi per modificare le camere dell'hotel?
        //Cosa modificare? hotel stesso oppure camere? e cosa delle camere?
        // per ora facciamo che l'hotel non è modificabile.
    }
    public void removeHotel(Hotel hotel) {
        //cancellare un hotel richiede di eliminare tutte le prenotazioni attive per quella struttura

        if(hotel == null) throw new RuntimeException("null reference to hotel");
        List<Reservation> reservationList = reservationManager.getAllReservations(hotel);
        for(Reservation reservation:reservationList)
            reservationManager.deleteReservation(reservation.getId());
        hotelMap.remove(hotel.getId());
        setChanged();
        notifyObservers(hotel, "Hotel removed");
    }
    public ArrayList<Hotel> doHotelResearch(Research researchInfo) {

        ArrayList<Hotel> filteredHotels = filterHotelByResearchInfo(researchInfo);
        //if (filteredHotels.isEmpty()) throw new RuntimeException("There are no hotels available for those dates.");
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
    private ArrayList<Hotel> filterHotelByResearchInfo(Research researchInfo) {

        ArrayList<Hotel> filteredHotels = new ArrayList<>();
        ArrayList<Hotel> allHotels = new ArrayList<>(hotelMap.values());

        for(Hotel hotel:allHotels) {
            if (hotel.getCity().equalsIgnoreCase(researchInfo.getCity())) {
                if (hotel.isHotelAvailable(researchInfo))
                    filteredHotels.add(hotel);
            }
        }
        return filteredHotels;

    }
    private ArrayList<Hotel> findHotelsByDirector(HotelDirector director) {
        ArrayList<Hotel> allHotels = new ArrayList<>(hotelMap.values());
        ArrayList<Hotel> hotelsByDirector = new ArrayList<>();

        for (Hotel hotel: allHotels) {
            if (hotel.getDirectorID().equals(director.getId())){
                hotelsByDirector.add(hotel);
            }
        }

        return hotelsByDirector;
    }
    //End Region

    protected Map<String, Hotel> getHotelMap() {
        return hotelMap;
    }
}
