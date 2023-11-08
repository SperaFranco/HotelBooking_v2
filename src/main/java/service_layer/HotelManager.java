package service_layer;

import domain_model.*;
import utilities.HotelRating;
import utilities.IdGenerator;
import utilities.Subject;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class HotelManager extends Subject {
    //TODO nell'hotel manager aggiungiamo pure i metodi per modificare le camere dell'hotel?
    private Map<String, Hotel> hotelMap;
    private ReservationManager reservationManager; //va qui???
    public HotelManager(){
        hotelMap = new HashMap<>();
    }
    public void addHotel(HotelDirector director) {
        Scanner scanner = new Scanner(System.in);
        String name, city, address, telephone, email, description;
        HotelRating rating;
        System.out.print("Please insert the name of the hotel:");
        name = scanner.nextLine();
        System.out.print("Please insert the city of the hotel:");
        city = scanner.nextLine();
        System.out.print("Please insert the address of the hotel:");
        address = scanner.nextLine();
        System.out.print("Please insert the telephone number of the hotel:");
        telephone = scanner.nextLine();
        System.out.print("Please insert the email of the hotel:");
        email = scanner.nextLine();
        System.out.print("Please insert the rating of the hotel (for example One star or Two stars):");
        rating = HotelRating.getRatingFromString(scanner.nextLine());
        System.out.print("Please insert a brief description of the hotel:");
        description = scanner.nextLine();

        Hotel newHotel = new Hotel(IdGenerator.generateHotelID(city),
                name, city, address, telephone, email, rating, description, director);

        hotelMap.put(newHotel.getId(), newHotel);
        setChanged();
        notifyObservers(newHotel, "New hotel added");
    }

    public void modifyHotel() {
        //Cosa modificare? hotel stesso oppure camere? e cosa delle camere?
    }
    public void removeHotel(HotelDirector director) {
        //Attenzione cancellare un hotel significa anche eliminare l'arrayList di tutte le camere
        //Dalla lista di strutture il gestore decide quale hotel eliminare
        // (immagino che ogni gestore di hotel possa vedere solo le proprie strutture)
        Scanner scanner = new Scanner(System.in);
        String id;

        ArrayList<Hotel> hotels = director.getHotels();
        System.out.println("These are your hotels:");
        for (Hotel hotel: hotels) {
            System.out.print("HotelID:" + hotel.printHotelInfo());
        }

        System.out.println("Please enter the id of which one you want to delete:");
        id = scanner.nextLine();
        Hotel hotelRemoved = director.findHotelByID(id);
        hotelMap.remove(id);
        setChanged();
        notifyObservers(hotelRemoved, "Hotel removed");
    }

    public void doHotelReseach(User user){
        //Chiedo tutte le info (check-in, check-out, luogo, numero di persone)

        //lascio i risultati secondo le richieste fatte dall'utente

        //Chiedo all'utente di inserire il numero della camera scelta oppure zero se non gli interessa nessuna
        //nel caso invoco la prenotazione
        int numOfGuests = 0;

        LocalDate checkIn = LocalDate.now();
        LocalDate checkOut = LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonth(), LocalDate.now().getDayOfMonth() +1);
        //Dalla camera scelta prendo
        String idHotel = null;
        String idRoom = null;

        doReservation(user, checkIn, checkOut, numOfGuests, idHotel, idRoom);
    }


    private void doReservation(User user, LocalDate checkIn, LocalDate checkOut, int numOfGuests,String idHotel, String idRoom) {
        //Trova l'hotel secondo l'id;
        Hotel hotelReserved = findHotelByID(idHotel);
        //Trova la camera secondo l'id
        Room roomReserverd = findRoomByID(idRoom);

        //Chiedi per la description
        String description = "";
        Reservation newReservation = new Reservation(IdGenerator.generateReservationID(), checkIn,checkOut,
                1, description,hotelReserved, roomReserverd, (Guest) user);
        reservationManager.addReservation(newReservation);
    }

    private Hotel findHotelByID(String idHotel) {
        return null;
    }

    private Room findRoomByID(String idRoom) {
        return null;
    }


}
