package utilities;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class IdGenerator {
    //classe statica che mi serve per generare
    // gli ID degli user alla loro registrazione
    // gli ID delle strutture
    // gli ID delle camere
    // gli ID delle prenotazioni

    private static int userCounter = 1;
    private static int hotelCounter = 1;
    private static int roomCounter = 1;
    private static int reservationCounter = 1;



    public static String generateUserID(UserType type, String name, String username) {
        String prefix = getPrefixForUserType(type);
        String userID = prefix + "-" + name.substring(0,1).toUpperCase() + username.substring(0,1).toUpperCase()+userCounter;
        userCounter++;
        return userID;
    }
    public static String generateHotelID(String city) {
        String cityPrefix = getCityPrefix(city);
        String hotelID = "H" + hotelCounter + cityPrefix;
        hotelCounter++;
        return hotelID;
    }
    public static String generateRoomID(String hotelID, RoomType type){
        String roomType = getPrefixForRoomType(type);
        String roomID = hotelID + "-" + roomType + roomCounter;
        roomCounter++;
        return roomID;
    }
    public static void resetRoomCounter() {
        roomCounter = 1;
    }
    public static String generateReservationID(String hotelID, String clientName, String clientSurname, LocalDate checkInDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMdd");
        String timeStamp = checkInDate.format(formatter);
        String reservationID = hotelID + "-" + clientName.substring(0,1).toUpperCase()
                + clientSurname.substring(0,1).toUpperCase() +timeStamp + "-" + reservationCounter;
        reservationCounter++;
        return reservationID;
    }

    //Region helpers
    private static String getCityPrefix(String city) {
        //Estrae le prime due lettere di una città
        if(city.length() >= 2) {
            return city.substring(0,2).toUpperCase();
        }else {
            throw new IllegalArgumentException("City name is too short.");
        }
    }
    private static String getPrefixForRoomType(RoomType type) {
        //Estrae la prima lettera a seconda del tipo della camera
        switch (type) {
            case SINGLE_ROOM:
                return "S";

            case DOUBLE_ROOM:
                return "D";

            case TRIPLE_ROOM:
                return "T";

            default:
                throw new IllegalArgumentException("Room type not valid!");
        }
    }
    private static String getPrefixForUserType(UserType type) {
        switch (type){
            case GUEST:
                return "G";
            case HOTEL_DIRECTOR:
                return "HD";
            default:
                throw new IllegalArgumentException("User type not valid!");
        }
    }
    //endRegion

}
