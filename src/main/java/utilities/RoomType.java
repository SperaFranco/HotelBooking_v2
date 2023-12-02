package utilities;

public enum RoomType {
    SINGLE_ROOM("Single room"),
    DOUBLE_ROOM("Double room"),
    TRIPLE_ROOM("Triple room");

    private final String displayName;

    RoomType(String displayName) {
        this.displayName = displayName;
    }

    public static int[] setNumberOfRoomsPerType(int nrOfSingleRooms, int nrOfDoubleRooms, int nrOfTripleRooms) {
        int[] rooms = new int[3];
        rooms[0] = nrOfSingleRooms;
        rooms[1] = nrOfDoubleRooms;
        rooms[2] = nrOfTripleRooms;
        return rooms;
    }

    public static int getRoomCapacity(RoomType type) {
        switch (type) {
            case SINGLE_ROOM:
                return 1;

            case DOUBLE_ROOM:
                return 2;

            case TRIPLE_ROOM:
                return 3;

            default:
                return 0;
        }
    }
}