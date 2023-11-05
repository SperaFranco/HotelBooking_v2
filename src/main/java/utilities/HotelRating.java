package utilities;

import domain_model.Hotel;

public enum HotelRating {
    ONE_STAR_HOTEL("One star"),
    TWO_STAR_HOTEL("Two Stars"),
    THREE_STAR_HOTEL("Three stars"),
    FOUR_STAR_HOTEL("Four stars"),
    FIVE_STAR_HOTEL("Five stars");
    private String displayName;

    HotelRating(String displayName) {
        this.displayName = displayName;
    }

    public static HotelRating getRatingFromString(String input) {
        HotelRating[] ratings = HotelRating.values();

        for(HotelRating rating : ratings) {
            if (rating.displayName.equals(input)) {
                return rating;
            }
        }
        return null; //se non trova corrispondenza rimane null
    }

}
