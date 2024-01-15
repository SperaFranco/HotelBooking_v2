package domain_model;

import utilities.*;
import java.util.ArrayList;

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
    private String directorID;
    private ArrayList<Room> rooms;
    //end Region


    public Hotel(String id, String name, String city, String address, String telephone,
                  HotelRating rating, String description, String managerID) {
        this.id = id;
        this.name = name;
        this.city = city;
        this.address = address;
        this.telephone = telephone;
        this.rating = rating;
        this.description = description;
        this.directorID = managerID;
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
    public void setRooms(ArrayList<Room> rooms) {
        this.rooms = rooms;
    }
    public ArrayList<Room> getRooms() {
        return this.rooms;
    }
    //end Region

}
