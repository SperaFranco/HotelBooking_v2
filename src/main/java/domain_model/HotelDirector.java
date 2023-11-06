package domain_model;

import service_layer.HotelManager;
import service_layer.ReservationManager;
import utilities.Observer;
import utilities.Subject;

import java.util.ArrayList;

public class HotelDirector extends User implements Observer {
    //Il gestore dell'hotel

    //Region Fields
    private ArrayList<Hotel> hotels;
    private HotelManager hotelManager;
    private ReservationManager reservationManager;
    //endRegion

    public HotelDirector(String id, String name, String surname, String email, String telephone, String password) {
        super(id, name, surname, email, telephone, password);
        hotels = new ArrayList<>();
        hotelManager.addObserver(this);
    }

    public ArrayList<Hotel> getHotels() {
        return hotels;
    }

    @Override
    public void update(Subject subject, Object argument, String message) {
        if (argument instanceof Hotel) {
            Hotel hotel = (Hotel) argument;
            this.updateHotels(hotel, message);
        }
    }




    //Region helper methods
    public Hotel findHotelByID(String id){
        Hotel myHotel = null;
        for(Hotel hotel: hotels) {
            if(hotel.getId().equals(id)) {
                myHotel = hotel;
                break;
            }
        }
        return myHotel;
    }

    public void updateHotels(Hotel hotel, String message) {
        if(message.equals("New hotel added")){
            hotels.add(hotel);
        }
        else if(message.equals("Hotel removed")){
            hotels.remove(hotel);
        }

    }

    private void updateAvailability(Subject subject, Object argument) {
        //TODO va presa la camera dell'hotel e messa la disponibilità a falso (se proprio si vuole implementare)
        //Inizialmente ottengo tutte le camere dell'hotel
        Reservation reservation = (Reservation) argument;
        Hotel hotelReserved = reservation.getHotel();
        Room roomReserved = reservation.getRoomReserved();

        Hotel hotelToModify = findHotelByID(hotelReserved.getId());
        if (hotelToModify!= null) {
            //ottenuto le camere vado a ricercare la camera e imposto la disponibilità a falso
            Room roomToModify = hotelToModify.findRoomByID(roomReserved.getId());
            if (roomToModify != null);
            //roomToModify.setAvailability(false);
        }
    }


    //end Region
}
