package domain_model;

import service_layer.ReservationManager;
import utilities.Observer;
import utilities.Subject;
import utilities.UserType;

import java.util.ArrayList;
import java.util.Objects;

public class Guest extends User{
    //Region fields
    private final CreditCard card;
    //end Region

    public Guest(String id, String name, String surname, String email, String telephone, String password, CreditCard card, UserType type) {
        super(id, name, surname, email, telephone, password, type);
        this.card = card;
    }

    public CreditCard getCard() {
        return card;
    }
}
