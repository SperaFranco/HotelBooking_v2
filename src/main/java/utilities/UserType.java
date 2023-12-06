package utilities;

public enum UserType {
    GUEST("guest"),
    HOTEL_DIRECTOR("hotel director");

    private final String displayName;

    UserType(String displayName) {
        this.displayName = displayName;
    }
}
