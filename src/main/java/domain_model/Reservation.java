package domain_model;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;

public class Reservation {
    //Region fields
    private String id;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private Guest client; //chi ha effettuato la prenotazione
    private int numOfGuests; //TODO bisogna garantire che il numero di ospiti sia coerente con il tipo di camera prentotata
    private String description;
    private Room roomReserved;

    //end Region


    public Reservation(String id, LocalDate checkIn, LocalDate checkOut, Guest client, int numOfGuests, String description, Room roomReserved) {
        this.id = id;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.client = client;
        this.numOfGuests = numOfGuests;
        this.description = description;
        this.roomReserved = roomReserved; //nel uml settiamolo a 1 --> potremmo rendere possibile che uno stesso utente compia
        // più prenotazioni, ma può prenotare solo una camera alla volta

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

    public Guest getClient() {
        return client;
    }

    public void setClient(Guest client) {
        this.client = client;
    }

    public int getNumOfGuests() {
        return numOfGuests;
    }

    public void setNumOfGuests(int numOfGuests) {
        this.numOfGuests = numOfGuests;
    }

    public Room getRoomReserved() {
        return roomReserved;
    }

    public void setRoomReserved(Room roomReserved) {
        this.roomReserved = roomReserved;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    //end Region

}
