package domain_model;

import utilities.RoomType;

public class Room {
    //Region fields
    private String id;
    private RoomType type;
    private double price;
    private String description; //nella descrizione della camera possiamo indicare le dimensioni della camera,
    // il tipo e numero di letti, le amenities se presenti (bagno, wifi, tv ecc...) e altro se serve
    private boolean availability;
    private int minimumStay; //è il numero soggiorni minimo

    //TODO forse andrebbe aggiunto un campo hotel per indicare la camera di quale hotel fà parte
    //end Region

    public Room(String id, RoomType type, double price, int minimumStay, String description) {
        this.id = id;
        this.type = type;
        this.price = price;
        this.description = description;
        this.availability = true;
        this.minimumStay = minimumStay;
    }

    //Region getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public RoomType getType() {
        return type;
    }

    public void setType(RoomType type) {
        this.type = type;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isAvailability() {
        return availability;
    }

    public void setAvailability(boolean availability) {
        this.availability = availability;
    }

    public int getMinimumStay() {
        return minimumStay;
    }

    public void setMinimumStay(int minimumStay) {
        this.minimumStay = minimumStay;
    }

    //end Region
}
