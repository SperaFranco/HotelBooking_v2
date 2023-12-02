package domain_model;

import service_layer.CalendarManager;
import utilities.*;

import java.time.LocalDate;
import java.util.ArrayList;

public class Hotel implements Observer {
    //Region fields
    private String id;
    private String name;
    private String city;
    private String address;
    private String telephone;
    private String email;
    private HotelRating rating;
    private String description;
    private ArrayList<Room> rooms; //todo lui deve stare attento ai cambiamenti delle camere (per ora non modificabili)
    private HotelCalendar calendar;
    private String directorID;
    //end Region


    public Hotel(String id, String name, String city, String address, String telephone,
                 String email, HotelRating rating, String description, String managerID, CalendarManager calendarManager) {
        this.id = id;
        this.name = name;
        this.city = city;
        this.address = address;
        this.telephone = telephone;
        this.email = email;
        this.rating = rating;
        this.description = description;
        this.directorID = managerID;
        this.rooms = new ArrayList<>();
        calendarManager.addObserver(this);
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


    public String getDirectorID() {
        return directorID;
    }

    public void setManager(String manager) {
        this.directorID = manager;
    }

    public ArrayList<Room> getRooms() {
        return rooms;
    }

    public void setRooms(ArrayList<Room> rooms) {
        this.rooms = rooms;
    }

    public ArrayList<Room> getRoomsAvailable(Research researchInfo) {

        ArrayList<Room> roomsAvailable = new ArrayList<>();

        for (Room room : rooms) {
            if(RoomType.getRoomCapacity(room.getType()) >= researchInfo.getNumOfGuest())
                if(calendar.isRoomAvailable(researchInfo, room.getId()))
                    roomsAvailable.add(room);
        }
        return roomsAvailable;
    }

    public HotelCalendar getCalendar() {
        return calendar;
    }

    public void setCalendar(HotelCalendar calendar) {
        this.calendar = calendar;
    }
    //end Region


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

    public boolean isHotelAvailable(Research researchInfo) {
        //Controllo nel calendario se ho delle camere disponibili per le richieste indicate
        // e ritorno vero se il numero di camere è diverso da zero

        ArrayList<Room> availableRooms = new ArrayList<>();
        boolean roomAvailable = false;
        for (Room room : getRoomsAvailable(researchInfo)) {
            roomAvailable = true;
            break;
        }
        return roomAvailable;
    }


    @Override
    public void update(Subject subject, Object argument, String message) {
        if (argument instanceof RoomInfo roomInfo) {
            if (calendar.getHotelID().equals(roomInfo.getHotelID())) //la nuova roomInfo si riferisce al giusto calendario?
                updateCalendar(roomInfo, message);
        }
    }

    private void updateCalendar(RoomInfo roomInfo, String message) {
        RoomInfo myRoomInfo = calendar.getRoomStatusMap().get(roomInfo.getDate()).get(roomInfo.getRoomID());
        if(message.contains("price")){
            //Devo andare a settare la camera per il giorno indicato al nuovo prezzo messo
            myRoomInfo.setPrice(roomInfo.getPrice());
        }
        else if (message.contains("availability")) {
            myRoomInfo.setAvailability(roomInfo.getAvailability());
        }
        else if (message.contains("minimum stay")) {
            myRoomInfo.setMinimumStay(roomInfo.getMinimumStay());
        }
    }
}
