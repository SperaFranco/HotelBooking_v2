package domain_model;

import utilities.HotelRating;
import utilities.IdGenerator;
import utilities.RoomType;

import java.time.LocalDate;
import java.util.*;

public class Hotel extends Observable {
    //Region fields
    private String id;
    private String name;
    private String city;
    private String address;
    private String telephone;
    private String email;
    private HotelRating rating;
    private String description;
    private ArrayList<Room> rooms;
    private HotelCalendar calendar; //TODO occhio c'è dà installare la dipendenza --> quando farlo?
    private HotelDirector manager;

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

        //TODO capire meglio come gestire la creazione delle camere e del calendario
        int[] numRooms = RoomType.getRoomPreference(); //qui avrò il numero di camere singole - doppie - triple
        RoomType[] types = RoomType.values();

        this.rooms = new ArrayList<>();
        for (int i = 0; i < numRooms.length; i++) {

            for (int j = 0; j < numRooms[i]; j++) {
                String roomID = IdGenerator.generateRoomID(id, types[i]);
                Room room = new Room(roomID, types[i], 1000, 0, ""); //TODO price, minimumStay e description devono essere variabili da passare come argomenti
                rooms.add(room);
            }

        }

        this.calendar = new HotelCalendar();

        LocalDate startDate = LocalDate.of(LocalDate.now().getYear(), 1,1);
        LocalDate endDate = LocalDate.of(LocalDate.now().getYear(), 12,31);

        for(LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)){
            for (Room room : rooms) {
                calendar.addRoom(date, room.getId(), room);
            }
        }

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
    //end Region


}
