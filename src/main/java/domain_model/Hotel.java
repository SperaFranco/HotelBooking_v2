package domain_model;

import utilities.HotelRating;
import utilities.IdGenerator;
import utilities.RoomType;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

public class Hotel {
    //Region fields
    private String id;
    private String name;
    private String city;
    private String address;
    private String telephone;
    private String email;
    private HotelRating rating;
    private String description;
    private ArrayList<Room> rooms; //TODO c'è da farlo diventare anche lui un observer (?)
    private HotelCalendar calendar; //TODO occhio c'è dà installare la dipendenza --> quando farlo?
    private HotelDirector manager;
    private ArrayList<Room> roomsAvailable; //mi tiene conto delle camere disponibili di un hotel --> occhio potrebbe dare problemi
    //end Region


    public Hotel(String id, String name, String city, String address, String telephone,
                 String email, HotelRating rating, String description, HotelDirector manager) {
        this.id = id;
        this.name = name;
        this.city = city;
        this.address = address;
        this.telephone = telephone;
        this.email = email;
        this.rating = rating;
        this.description = description;
        this.manager = manager;
        this.rooms = new ArrayList<>();
    }

    //Region getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public HotelRating getRating() {
        return rating;
    }

    public void setRating(HotelRating rating) {
        this.rating = rating;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public HotelDirector getManager() {
        return manager;
    }

    public void setManager(HotelDirector manager) {
        this.manager = manager;
    }

    public ArrayList<Room> getRooms() {
        return rooms;
    }

    public void setRooms(ArrayList<Room> rooms) {
        this.rooms = rooms;
    }

    public ArrayList<Room> getRoomsAvailable() {
        return roomsAvailable;
    }
    //end Region

    private void createCalendar(ArrayList<Room> rooms) {

    }
    public Room findRoomByID(String id) {
        Room myRoom = null;
        for (Room room : rooms) {
            if(room.getId().equals(id)){
                myRoom = room;
                break;
            }
        }
        return myRoom;
    }
    public String printHotelInfo(int i) {
        return "Hotel number " + i + " informations:\n" +
                "Name: " + name + "\n" +
                "City: " + city + "\n" +
                "Address: " + address + "\n" +
                "Number of Rooms: " + rooms.size() + "\n";
    }
    public String getAllRoomInfo(int index, LocalDate checkIn, String id) {
        StringBuilder info = new StringBuilder();
        info.append("Room number " + index + " informations:\n");
        Room room = findRoomByID(id);
        info.append("Room type: " + room.getType());
        info.append(calendar.getPrice(checkIn, id));
        info.append("Description: " + room.getDescription());

        return info.toString();
    }

    public int getHotelTotalCapacity() {
        //In questo metodo scorro fra tutte le camere dell'hotel e
        // ne ritorno il numero totale di persone che l'hotel è in grado di ospitare
        //per una versione migliore dovrei ciclare solo sulle camere che risultano disponibili su un certo periodo
        int capacity = 0;

        for (Room room:rooms) {
            capacity += RoomType.getRoomCapacity(room.getType());
        }

        return capacity;
    }
    public boolean isHotelAvailable(LocalDate checkIn, LocalDate checkOut, int numOfGuests, int numOfRooms) {
        //Controllo nel calendario se ho delle camere disponibili per le richieste indicate
        // e ritorno vero se il numero di camere è diverso da zero
        //Se serve potrei ritornare direttamente le camere che mi risultano disponibili
        //al momento non uso numOfRooms ma lo dovrei usare per dividere le persone fra le camere
        ArrayList<Room> availableRooms = new ArrayList<>();

        for (Room room : rooms) {
            String roomID = room.getId();
            if(calendar.isRoomAvailable(checkIn, checkOut, roomID) && room.canRoomAccomodate(numOfGuests))
                availableRooms.add(room);
        }

        return !availableRooms.isEmpty();
    }

}
