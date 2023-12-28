package domain_model;

import utilities.Research;

import java.time.LocalDate;

public class Reservation
{
    //Region fields
    private String id;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private int numOfGuests;
    private String notes;
    private String hotelID;
    private String roomReservedID;
    private String clientID;
    //end Region


    public Reservation(String id, Research researchInfo, String notes, String hotel, String roomReserved, String client) {
        this.id = id;
        this.checkIn = researchInfo.getCheckIn();
        this.checkOut = researchInfo.getCheckOut();
        this.numOfGuests = researchInfo.getNumOfGuest();
        this.notes = notes;
        this.hotelID = hotel;
        this.roomReservedID = roomReserved; //nel uml settiamolo a 1 --> potremmo rendere possibile che uno stesso utente compia
        // più prenotazioni, ma può prenotare solo una camera alla volta
        this.clientID = client;

    }

    public Reservation(String id, LocalDate checkIn, LocalDate checkOut, int numOfGuests, String description, String hotelID, String roomReservedID, String clientID) {
        this.id = id;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.numOfGuests = numOfGuests;
        this.notes = description;
        this.hotelID = hotelID;
        this.roomReservedID = roomReservedID;
        this.clientID = clientID;
    }

    //Region getters and setters
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public LocalDate getCheckIn() {
        return checkIn;
    }
    public void setCheckIn(LocalDate checkIn) {
        this.checkIn = checkIn;
    }
    public LocalDate getCheckOut() {
        return checkOut;
    }
    public void setCheckOut(LocalDate checkOut) {
        this.checkOut = checkOut;
    }
    public String getClient() {
        return clientID;
    }
    public void setClient(String client) {
        this.clientID = client;
    }
    public int getNumOfGuests() {
        return numOfGuests;
    }
    public void setNumOfGuests(int numOfGuests) {
        this.numOfGuests = numOfGuests;
    }
    public String getRoomReserved() {
        return roomReservedID;
    }
    public void setRoomReserved(String roomReserved) {
        this.roomReservedID = roomReserved;
    }
    public String getNotes() {
        return notes;
    }
    public void setNotes(String notes) {
        this.notes = notes;
    }
    public String getHotel() {
        return hotelID;
    }
    public void setHotel(String hotel) {
        this.hotelID = hotel;
    }
    //end Region
}
