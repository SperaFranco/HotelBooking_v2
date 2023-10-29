package domain_model;

import java.util.ArrayList;

public class HotelManager extends User {

    //Region Fields
    private ArrayList<Hotel> hotels;
    //endRegion

    public HotelManager(String id, String name, String surname, String email, String password, String telephone) {
        super(id, name, surname, email, password, telephone);
        hotels = new ArrayList<>();
    }
}
