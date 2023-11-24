package domain_model;

import service_layer.CalendarManager;
import service_layer.HotelManager;
import service_layer.ReservationManager;
import utilities.Observer;
import utilities.Subject;

import java.util.ArrayList;

public class HotelDirector extends User implements Observer {
    //Il gestore dell'hotel

    //Region Fields
    private final ArrayList<String> hotels; //lui deve stare attento quando aggiungo/rimuovo un hotel
    //endRegion

    public HotelDirector(String id, String name, String surname, String email, String telephone, String password, HotelManager hotelManager) {
        super(id, name, surname, email, telephone, password);
        hotels = new ArrayList<>();
        hotelManager.addObserver(this);
    }

    public ArrayList<String> getHotels() {
        return hotels;
    }

    @Override
    public void update(Subject subject, Object argument, String message) {
        if (argument instanceof Hotel hotel) {
            if (hotel.getDirectorID().equals(this.getId()))
                updateHotels(hotel, message);
        }
    }


    //Region helper methods

    private void updateHotels(Hotel hotel, String message) {
        if(message.equals("New hotel added")){
            hotels.add(hotel.getId());
        }
        else if(message.equals("Hotel removed")){
            hotels.remove(hotel.getId());
        }

    }

    private void updateAvailability(Subject subject, Object argument) {
        //va presa la camera dell'hotel e messa la disponibilità a falso (se proprio si vuole implementare)
    }


    //end Region
}
