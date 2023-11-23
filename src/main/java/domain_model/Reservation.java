package domain_model;

import java.time.LocalDate;

public class Reservation
{
    //Region fields
    private String id;
    private LocalDate checkIn;
    private LocalDate checkOut;

    //TODO bisogna garantire che il numero di ospiti sia coerente con il tipo di camera prenotata
    private int numOfGuests;
    private String notes;
    private String hotelID;
    private String roomReservedID;
    private String userID;
    //end Region


    public Reservation(String id, LocalDate checkIn, LocalDate checkOut, int numOfGuests, String description,
                       String hotel, String roomReserved, String client) {
        this.id = id;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.numOfGuests = numOfGuests;
        this.notes = description;
        this.hotelID = hotel;
        this.roomReservedID = roomReserved; //nel uml settiamolo a 1 --> potremmo rendere possibile che uno stesso utente compia
        // più prenotazioni, ma può prenotare solo una camera alla volta
        this.userID = client;

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
        return userID;
    }

    public void setClient(String client) {
        this.userID = client;
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

    public String getInfoReservation() {
        return "Reservation ID: " + id + "\n" +
                "Check-in date: " + checkIn + "\n" +
                "Check-out date: " + checkOut + "\n" +
                "Number of guests: " + numOfGuests + "\n" +
                "Description: " + notes + "\n" +
                //Mi stampo gli id e non il nome... non proprio il massimo
                "Hotel reserved: " + hotelID + "\n" +
                "Room Reserved: " + roomReservedID + "\n" +
                "Client: " + userID + "\n";
    }
    //end Region
}
