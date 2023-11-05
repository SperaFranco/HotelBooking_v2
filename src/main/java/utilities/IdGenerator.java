package utilities;

public class IdGenerator {
    private static int hotelCounter = 1;
    private static int roomCounter = 1;

    //TODO classe statica che mi serve per generare
    // gli ID degli user alla loro registrazione
    // gli ID delle prenotazioni
    // gli ID delle strutture --> fatto
    // gli ID delle camere --> fatto

    public static String generateHotelID(String city) {
        //TODO complica se vuoi
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

    public static void resetRoomCounter() {
        roomCounter = 1;
    }
}
