package domain_model;

import java.time.LocalDate;

public class RoomInfo {
    //Classe che mi tiene conto delle principali informazioni che variano della camera

    //Region fields
    private String hotelID;
    private String roomID; //uso l'id della camera per riferimermi a questa
    private double price;
    private int minimumStay; //è il numero soggiorni minimo
    private boolean availability;
    private LocalDate date;
    //end Region

    public RoomInfo(String hotelID, String roomID, LocalDate date) {
        this.hotelID = hotelID;
        this.roomID = roomID;
        this.price = 100.0;
        this.minimumStay = 1;
        this.availability = true;
        this.date = date;
    }

    public RoomInfo(String hotelID, String roomID, LocalDate date, double price, int minimumStay, boolean availability) {
        this.hotelID = hotelID;
        this.roomID = roomID;
        this.price = price;
        this.minimumStay = minimumStay;
        this.availability = availability;
        this.date = date;
    }

    //Region Getters and Setters

    public String getRoomID() {
        return roomID;
    }

    public void setRoomID(String roomID) {
        this.roomID = roomID;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getMinimumStay() {
        return minimumStay;
    }

    public void setMinimumStay(int minimumStay) {
        this.minimumStay = minimumStay;
    }

    public boolean getAvailability() {
        return availability;
    }

    public String getAvailabilityToString() {
        if (availability)
            return "available";
        else
            return "not available";
    }

    public void setAvailability(boolean availability) {
        this.availability = availability;
    }

    public String getHotelID() {
        return hotelID;
    }
    //end Region

    //FIXME risolvere il problema di questa data, che non ha senso che sia qua ma è legata all'implementazione dell'Observer
    public LocalDate getDate(){
        return date;
    }
}
