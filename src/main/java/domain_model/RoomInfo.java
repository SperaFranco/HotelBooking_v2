package domain_model;

import java.time.LocalDate;

public class RoomInfo {
    //Classe che mi tiene conto delle principali informazioni che variano della camera

    //Region fields
    private String hotelID;
    private String roomID; //uso l'id della camera per riferimermi a questa
    private LocalDate date;

    private double price;
    private int minimumStay; //è il numero soggiorni minimo
    private boolean availability;
    //end Region

    public RoomInfo(String hotelID, String roomID, LocalDate date) {
        this.roomID = roomID;
        this.price = 100.0;
        this.minimumStay = 1;
        this.availability = true;
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

    public void setAvailability(boolean availability) {
        this.availability = availability;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getHotelID() {
        return hotelID;
    }
    //end Region
}
