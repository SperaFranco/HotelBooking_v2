package service_layer;

import domain_model.*;
import utilities.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class HotelManager extends Subject {
    //Classe per la gestione e la modifica delle strutture degli hotel
    //TODO nell'hotel manager aggiungiamo pure i metodi per modificare le camere dell'hotel?
    private Map<String, Hotel> hotelMap;
    private ReservationManager reservationManager;
    private CalendarManager calendarManager;
    private final Scanner scanner;

    public HotelManager(ReservationManager reservationManager, CalendarManager calendarManager, Scanner scanner){
        hotelMap = new HashMap<>();
        this.reservationManager = reservationManager;
        this.calendarManager = calendarManager;
        this.scanner = scanner;
    }

    public Hotel addHotel(HotelDirector director) {
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
        System.out.print("Please insert the rating of the hotel (for example \"One star\" or \"Two stars\" etc...):");
        rating = HotelRating.getRatingFromString(scanner.nextLine());
        System.out.print("Please insert a brief description of the hotel:");
        description = scanner.nextLine();

        Hotel newHotel = new Hotel(IdGenerator.generateHotelID(city),
                name, city, address, telephone, email, rating, description, director.getId());
        System.out.println("Hotel created! Now add your rooms...");

        ArrayList<Room> rooms = createRooms(newHotel.getId());
        newHotel.setRooms(rooms);
        System.out.println("Rooms added!");

        HotelCalendar calendar = calendarManager.createCalendar(rooms, newHotel.getId(), this);
        newHotel.setCalendar(calendar);
        System.out.println("Calendar configured!");

        hotelMap.put(newHotel.getId(), newHotel);
        setChanged();
        notifyObservers(newHotel, "New hotel added");
        return newHotel;
    }
    public void modifyHotel() {
        //Cosa modificare? hotel stesso oppure camere? e cosa delle camere?
        // per ora facciamo che l'hotel non è modificabile.
    }
    public void removeHotel(HotelDirector director) {
        //Attenzione cancellare un hotel significa anche eliminare l'arrayList di tutte le camere
        //Dalla lista di strutture il gestore decide quale hotel eliminare
        // (immagino che ogni gestore di hotel possa vedere solo le proprie strutture)
        int id;
        ArrayList<Hotel> hotels = findHotelsByDirector(director);
        System.out.println("These are your hotels:");
        for (Hotel hotel: hotels) {
            int index = 1;
            System.out.println(hotel.printHotelInfo(index++));
        }

        System.out.println("Please enter the number of which hotel you want to delete:");
        id = scanner.nextInt();
        scanner.nextLine();
        Hotel hotelRemoved = hotels.get(id);

        if(hotelRemoved != null) {
            hotelMap.remove(hotelRemoved.getId());
            setChanged();
            notifyObservers(hotelRemoved, "Hotel removed");
        }
    }

    public void doHotelResearch(User user){
        //TODO capire se effettivamente ci serve chiedere il numero di camere
        //Chiedo tutte le info (check-in, check-out, luogo, numero di persone)
        Research info =  askResearchInfo();

        //filtro i risultati secondo le richieste fatte dall'utente
        ArrayList<Hotel> hotels = filterHotels(info.getCity(), info.getCheckIn(),
                info.getCheckOut(), info.getNumOfGuest(), 0);

        System.out.println("These are the hotels found:");
        for (Hotel hotel : hotels) {
            int index = 1;
            System.out.println(hotel.printHotelInfo(index++));
        }

        //Chiedo all'utente di inserire il numero della camera scelta oppure zero se non gli interessa nessuna
        //nel caso invoco la prenotazione
        System.out.print("Please enter the number of the hotel you want to explore:");
        int hotelNumber = scanner.nextInt();
        scanner.nextLine();

        if(hotelNumber > 0 && hotelNumber < hotels.size()) {
            Hotel hotelToReserve = hotels.get(hotelNumber - 1);
            ArrayList<Room> roomsAvailable = hotelToReserve.getRoomsAvailable(info.getCheckIn(), info.getCheckOut()); //qui ci saranno le camere disponibili

            //A questo punto ne stampo le camere e le info ma solo delle camere disponibili
            System.out.println("These are the rooms available:");
            for (Room room : roomsAvailable) {
                HotelCalendar calendar = hotelToReserve.getCalendar();
                int index = 1;
                System.out.println(room.getRoomInfo(index) + "\nPrice:" + calendar.getPrice(info.getCheckIn(), room.getId()));
            }
            //Chiedo all'utente di scegliere la camera che preferisce
            System.out.print("Please enter the number of the camera you want to book(or zero if you want to exit):");
            int roomNumber = scanner.nextInt();
            scanner.nextLine();

            if(roomNumber > 0 && roomNumber < roomsAvailable.size()) {
                //Allora facciamo la prenotazione
                Room roomToReserve = roomsAvailable.get(roomNumber - 1);
                String idHotel = hotelToReserve.getId();
                String idRoom = roomToReserve.getId();
                reservationManager.addReservation(user, info.getCheckIn(), info.getCheckOut(), info.getNumOfGuest(), hotelToReserve, roomToReserve);
            }
            else if (roomNumber == 0){
                //Ho fatto una semplice ricerca e non voglio prenotare
                System.out.println("Returning back to the start menù!");
            }
            else
                throw new RuntimeException("Room not on the list!");
        }
        else
            throw new RuntimeException("Hotel not on the list!");
    }

    public Hotel chooseHotel(HotelDirector director) {
        ArrayList<Hotel> hotels = findHotelsByDirector(director);

        if(!hotels.isEmpty()) {
            for (Hotel hotel : hotels) {
                int index = 1;
                hotel.printHotelInfo(index++);
            }
            System.out.print("Hotel number:");
            int hotelNumber = scanner.nextInt();
            scanner.nextLine();
            return hotels.get(hotelNumber);
        }
        else {
            System.out.println("Sorry you have to put first at least an hotel!");
        }
        return null;
    }
    //Region Helpers
    private ArrayList<Hotel> filterHotels(String city, LocalDate checkIn, LocalDate checkOut, int numOfGuests, int numOfRooms){
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
    private ArrayList<Room> createRooms(String id){
        //Al momento lo lasciamo qui magari il roomManager non ci serve
        ArrayList<Room> rooms = new ArrayList<>();
        int[] numRooms = RoomType.getRoomPreference(); //qui chiederò il numero di camere singole - doppie - triple
        RoomType[] types = RoomType.values();
        IdGenerator.resetRoomCounter(); //rimette a 1 il counter delle stanze per l'hotel

        for (int i = 0; i < numRooms.length; i++) {
            for (int j = 0; j < numRooms[i]; j++) {
                String description, roomID;
                roomID = IdGenerator.generateRoomID(id, types[i]);
                System.out.println("Please insert more info for room " + roomID +
                        " (like the dimensions of the room and/or if it has some amenities like tv or wifi):");
                description = scanner.nextLine();
                Room room = new Room(roomID, types[i], description);
                rooms.add(room);
            }
        }
        return rooms;
    }
    private ArrayList<Hotel> findHotelsByDirector(HotelDirector director) {
        ArrayList<Hotel> allHotels = new ArrayList<>(hotelMap.values());
        ArrayList<Hotel> hotelsByDirector = new ArrayList<>();

        for (Hotel hotel: allHotels) {
            if (hotel.getManager().equals(director.getId())){
                hotelsByDirector.add(hotel);
            }
        }

        return hotelsByDirector;
    }
    public Research askResearchInfo() {
        //TODO creare oggetto ricerca magari anche static
        LocalDate checkIn, checkOut;
        String city;
        int numOfGuests;
        int numOfRooms = 0; //mi serve per capire in quante camere l'utente vorrebbe stare

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

        return new Research(city, checkIn, checkOut, numOfGuests);

    }

    //End Region

}
