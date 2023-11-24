package domain_model;

import utilities.RoomType;

import java.time.LocalDate;

public class Room {
    //Region fields
    private String id;
    private RoomType type;
    private String description; //nella descrizione della camera possiamo indicare le dimensioni della camera,
    // il tipo e numero di letti, le amenities se presenti (bagno, wifi, tv ecc...) e altro se serve
    //end Region

    public Room(String id, RoomType type, String description) {
        this.id = id;
        this.type = type;
        this.description = description;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    //end Region

    public boolean canRoomAccomodate(int numOfGuests) {
        return numOfGuests <= RoomType.getRoomCapacity(type);
    }

    public String getRoomInfo(int index) {
        return "Room number " + index + " informations:\n" +
                "RoomID: " + id +
                "Room type: " + type +
                "Description: " + description;
    }

}
