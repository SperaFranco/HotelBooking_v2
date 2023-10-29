package domain_model;

import utilities.HotelRating;

import java.util.ArrayList;

public class Hotel {

    //Region fields
    private String name;
    private String city;
    private String address;
    private String telephone;
    private String email;
    private HotelRating rating;
    private String description;

    private ArrayList<Room> rooms;
    //end Region
}
