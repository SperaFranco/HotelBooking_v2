package utilities;

import java.util.Scanner;

public enum RoomType {
    SINGLE_ROOM,
    DOUBLE_ROOM,
    TRIPLE_ROOM;

    public static int[] getRoomPreference() {
        //TODO vedere dove va messo questo metodo
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
            System.out.println("Please enter the number of " + roomType + " rooms: ");
            rooms[i] = scanner.nextInt();
        }
        return rooms;
    }
}