package domain_model;

import service_layer.HotelManager;
import service_layer.ReservationManager;
import utilities.Observer;
import utilities.Subject;

import java.util.ArrayList;

public class HotelDirector extends User implements Observer {
    //Il gestore dell'hotel

    //Region Fields
    private ArrayList<String> hotels;
    //endRegion

    public HotelDirector(String id, String name, String surname, String email, String telephone, String password) {
        super(id, name, surname, email, telephone, password);
        hotels = new ArrayList<>();
    }

    public ArrayList<String> getHotels() {
        return hotels;
    }

    @Override
    public void update(Subject subject, Object argument, String message) {
        if (argument instanceof Hotel hotel) {
            this.updateHotels(hotel, message);
        }
    }


    //Region helper methods

    private void updateHotels(Hotel hotel, String message) {
        if(message.equals("New hotel added")){
            hotels.add(hotel.getId());
        }
        else if(message.equals("Hotel removed")){
            hotels.remove(hotel);
        }

    }

    private void updateAvailability(Subject subject, Object argument) {
        //TODO va presa la camera dell'hotel e messa la disponibilità a falso (se proprio si vuole implementare)
    }


    //end Region
}
