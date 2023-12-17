package domain_model;

import service_layer.CalendarManager;
import service_layer.HotelManager;
import service_layer.ReservationManager;
import utilities.Observer;
import utilities.Subject;
import utilities.UserType;

import java.util.ArrayList;

public class HotelDirector extends User {
    public HotelDirector(String id, String name, String surname, String email, String telephone, String password, UserType type) {
        super(id, name, surname, email, telephone, password, type);
    }

}
