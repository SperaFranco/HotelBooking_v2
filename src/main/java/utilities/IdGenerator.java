package utilities;

public class IdGenerator {
    private static int roomCounter = 1;
    //TODO classe statica che mi serve per generare
    // gli ID degli user alla loro registrazione
    // gli ID delle prenotazioni
    // gli ID delle strutture
    // gli ID delle camere

    public static String generateRoomID(String hotelID, RoomType type){
        String roomType = getPrefixForRoomType(type);
        String roomID = hotelID + "-" + roomType + "-" + roomCounter;
        roomCounter++;
        return roomID;
    }

    private static String getPrefixForRoomType(RoomType type) {
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
}
