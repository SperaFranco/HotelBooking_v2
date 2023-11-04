package domain_model;

import java.util.ArrayList;

public class HotelDirector extends User {

    //Region Fields
    private ArrayList<Hotel> hotels;
    //endRegion

    public HotelDirector(String id, String name, String surname, String email, String telephone, String password) {
        super(id, name, surname, email, telephone, password);
        hotels = new ArrayList<>();
    }
}
