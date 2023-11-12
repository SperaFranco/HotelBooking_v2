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
            int index = 1;
            System.out.println(hotel.printHotelInfo(index++));
        }

        System.out.println("Please enter the id of which one you want to delete:");
        id = scanner.nextLine();
        Hotel hotelRemoved = director.findHotelByID(id);
        hotelMap.remove(id);
        setChanged();
        notifyObservers(hotelRemoved, "Hotel removed");
    }

    public void doHotelResearch(User user){
        //Chiedo tutte le info (check-in, check-out, luogo, numero di persone)
        Scanner scanner = new Scanner(System.in);
        LocalDate checkIn, checkOut;
        String city;
        int numOfGuests;
        int numOfRooms = 0; //mi serve per capire in quante camere l'utente vorrebbe stare
        int hotelNumber, roomNumber;

        System.out.print("Please insert your destination:");
        city = scanner.nextLine();
        System.out.print("Please insert the check-in date:");
        checkIn = LocalDate.parse(scanner.nextLine());
        System.out.print("And then the check-out date:");
        checkOut = LocalDate.parse(scanner.nextLine());
        System.out.print("Please insert the number of guests that will stay:");
        numOfGuests = scanner.nextInt();
        scanner.nextLine();

        /*
        System.out.print("And the number of rooms needed:");
        numOfRooms = scanner.nextInt();
        scanner.nextLine();
         */

        //filtro i risultati secondo le richieste fatte dall'utente
        ArrayList<Hotel> hotels = filterHotels(city, checkIn, checkOut, numOfGuests, numOfRooms);
        System.out.println("These are the hotels found:");
        for (Hotel hotel : hotels) {
            int index = 1;
            System.out.println(hotel.printHotelInfo(index++));
        }

        //Chiedo all'utente di inserire il numero della camera scelta oppure zero se non gli interessa nessuna
        //nel caso invoco la prenotazione
        System.out.print("Please enter the number of the hotel you want to explore:");
        hotelNumber = scanner.nextInt();
        scanner.nextLine();

        if(hotelNumber > 0 && hotelNumber < hotels.size()) {
            Hotel hotelToReserve = hotels.get(hotelNumber - 1);
            ArrayList<Room> roomsAvailable = hotelToReserve.getRoomsAvailable(); //qui ci saranno le camere disponibili

            //A questo punto ne stampo le camere e le info ma solo delle camere disponibili
            System.out.println("These are the rooms available:");
            for(Room room : roomsAvailable) {
               int index = 1;
               System.out.println(hotelToReserve.getAllRoomInfo(index, checkIn, room.getId()));
             }

            //Chiedo all'utente di scegliere la camera che preferisce
            System.out.print("Please enter the number of the camera you want to book(or zero if you want to exit):");
            roomNumber = scanner.nextInt();
            scanner.nextLine();

            if(roomNumber > 0 && roomNumber < roomsAvailable.size()) {
                //Allora facciamo la prenotazione
                Room roomToReserve = roomsAvailable.get(roomNumber - 1);
                String idHotel = hotelToReserve.getId();
                String idRoom = roomToReserve.getId();
                doReservation(user, checkIn, checkOut, numOfGuests, hotelToReserve, roomToReserve);
                scanner.close();
            }
            else if (roomNumber == 0){
                //Ho fatto una semplice ricerca e non voglio prenotare
                scanner.close();
            }
            else
                throw new RuntimeException("Room not on the list!");
        }
        else
            throw new RuntimeException("Hotel not on the list!");
    }

    private ArrayList<Hotel> filterHotels(String city, LocalDate checkIn, LocalDate checkOut, int numOfGuests, int numOfRooms) {
        //Di tutti gli hotel nella map mi tengo solo quelli che soddisfano i criteri
        ArrayList<Hotel> filteredHotels = new ArrayList<>();
        ArrayList<Hotel> allHotels = new ArrayList<>(hotelMap.values());

        for(Hotel hotel:allHotels) {
            //Filtro per la città
            if(hotel.getCity().equalsIgnoreCase(city)) { //equalsIgnoreCase mi ignora maiuscole o minuscole differenti
                //Filtro per il numero di ospiti
                int totalCapacity = hotel.getHotelTotalCapacity();
                if(numOfGuests <= totalCapacity) {
                        //Controllo quindi se esistono camere disponibili per quelle richieste
                        if(hotel.isHotelAvailable(checkIn, checkOut, numOfGuests, numOfRooms))
                            filteredHotels.add(hotel);
                }
            }
        }
        return filteredHotels;
    }

    private void doReservation(User user, LocalDate checkIn, LocalDate checkOut, int numOfGuests,Hotel hotelReserved, Room roomReserverd) {
        //Nel caso l'user sia nullo chiedo di fare il login oppure di fare un nuovo account
        if(user == null){

        }

        //Altrimenti chiedi per la description e aggiungi la prenotazione
        Scanner scanner = new Scanner(System.in);
        System.out.print("Write something if there is anything you need in your reservation:");
        String description = scanner.nextLine();
        Reservation newReservation = new Reservation(IdGenerator.generateReservationID(), checkIn, checkOut,
                numOfGuests, description, hotelReserved, roomReserverd, (Guest) user);
        reservationManager.addReservation(newReservation);
        System.out.println("Reservation added! Thank you! :)");
    }

    private Hotel findHotelByID(String idHotel) {
        return null;
    }

    private Room findRoomByID(String idRoom) {
        return null;
    }


}
