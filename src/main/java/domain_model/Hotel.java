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
    private ArrayList<Room> rooms; //TODO c'è da farlo diventare anche lui un observer
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

        System.out.println("Hotel created! Now add your rooms...");

        //TODO capire meglio come gestire la creazione delle camere e del calendario
        createRooms(id);
        createCalendar(rooms);


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

    //end Region

    private void createRooms(String id) {
        //TODO price, minimumStay e description devono essere variabili da passare come argomenti
        int[] numRooms = RoomType.getRoomPreference(); //qui chiederò il numero di camere singole - doppie - triple
        RoomType[] types = RoomType.values();

        Scanner scanner = new Scanner(System.in);
        this.rooms = new ArrayList<>();
        IdGenerator.resetRoomCounter(); //rimette a 1 il counter delle stanze per l'hotel

        for (int i = 0; i < numRooms.length; i++) {
            for (int j = 0; j < numRooms[i]; j++) {
                double price;
                int minimumStay;
                String description, roomID;

                roomID = IdGenerator.generateRoomID(id, types[i]);
                System.out.println("Please insert more info for room " + roomID + ":");
                System.out.print("The price (for example 100.0):");
                price = scanner.nextDouble();
                System.out.print("The number of minimum days to stay:");
                minimumStay = scanner.nextInt();
                System.out.print("And a brief description" +
                        "(like the dimensions of the room and/or if it has some amenities like tv or wifi):");
                description = scanner.nextLine();
                Room room = new Room(roomID, types[i], description);
                rooms.add(room);
            }
        }
    }

    private void createCalendar(ArrayList<Room> rooms) {
        this.calendar = new HotelCalendar();

        LocalDate startDate = LocalDate.of(LocalDate.now().getYear(), 1,1);
        LocalDate endDate = LocalDate.of(LocalDate.now().getYear(), 12,31);

        for(LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)){
            for (Room room : rooms) {
                String roomID = room.getId();
                RoomInfo roomInfo = new RoomInfo(roomID);
                calendar.addRoomToCalendar(date, roomID, roomInfo);
            }
        }
    }

    public Room findRoomByID(String id) {
        Room myRoom = null;
        for (Room room : rooms) {
            if(room.equals(id)){
                myRoom = room;
                break;
            }
        }
        return myRoom;
    }

    public String printHotelInfo() {
        StringBuilder info = new StringBuilder();
        info.append("Hotel Information:\n");
        info.append("ID: " + id + "\n");
        info.append("Name: " + name + "\n");
        info.append("City: " + city + "\n");
        info.append("Address: " + address + "\n");
        info.append("Number of Roooms: "+ rooms.size() + "\n");

        return info.toString();
    }
}
