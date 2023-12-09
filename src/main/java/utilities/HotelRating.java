package utilities;


import java.lang.invoke.SwitchPoint;

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

    public static HotelRating getRatingFromString(String rating) {
        switch (rating) {
            case "One star":
                return ONE_STAR_HOTEL;

            case "Two stars":
                return TWO_STAR_HOTEL;

            case "Three stars":
                return THREE_STAR_HOTEL;

            case "Four stars":
                return FOUR_STAR_HOTEL;

            case "Five stars":
                return FIVE_STAR_HOTEL;

            default:
                return null;
        }
    }
}
