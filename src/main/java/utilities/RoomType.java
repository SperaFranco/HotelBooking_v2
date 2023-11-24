package utilities;

import java.util.Scanner;

public enum RoomType {
    SINGLE_ROOM("Single room"),
    DOUBLE_ROOM("Double room"),
    TRIPLE_ROOM("Triple room");

    private final String displayName;

    RoomType(String displayName) {
        this.displayName = displayName;
    }

    public static int[] getRoomPreference() {
        Scanner scanner = new Scanner(System.in);
        int[] rooms = new int[3]; //0 single_room, 1 double_room, 2 triple_room

        for (int i = 0; i < rooms.length; i++) {
            String roomType = "";

            switch (i) {
                case 0:
                    roomType = "single";
                    break;
                case 1:
                    roomType = "double";
                    break;
                case 2:
                    roomType = "triple";
                    break;

            }
            System.out.print("Please enter the number of " + roomType + " rooms: ");
            rooms[i] = scanner.nextInt();
        }
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