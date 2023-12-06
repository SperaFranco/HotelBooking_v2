package utilities;


public enum HotelRating {
    ONE_STAR_HOTEL("One star"),
    TWO_STAR_HOTEL("Two Stars"),
    THREE_STAR_HOTEL("Three stars"),
    FOUR_STAR_HOTEL("Four stars"),
    FIVE_STAR_HOTEL("Five stars");
    private final String displayName;

    HotelRating(String displayName) {
        this.displayName = displayName;
    }

}
