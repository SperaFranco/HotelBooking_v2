package domain_model;

public class RoomInfo {
    //Classe che mi tiene conto delle principali informazioni che variano della camera

    //Region fields
    private String roomID; //uso l'id della camera per riferimermi a questa
    private double price;
    private int minimumStay; //è il numero soggiorni minimo
    private boolean availability;
    //end Region

    public RoomInfo(String roomID) {
        this.roomID = roomID;
        this.price = 1000.0;
        this.minimumStay = 1;
        this.availability = true;
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

    public boolean isAvailable() {
        return availability;
    }

    public void setAvailability(boolean availability) {
        this.availability = availability;
    }
    //end Region
}
