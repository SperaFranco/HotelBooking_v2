package utilities;

import java.time.LocalDate;

public class Research {
    String city;
    LocalDate checkIn;
    LocalDate checkOut;
    int numOfGuest;

    public Research(String city, LocalDate checkIn, LocalDate checkOut, int numOfGuest){
        this.city = city;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.numOfGuest = numOfGuest;
    }

    public String getCity() {
        return city;
    }

    public LocalDate getCheckIn() {
        return checkIn;
    }

    public LocalDate getCheckOut() {
        return checkOut;
    }

    public int getNumOfGuest() {
        return numOfGuest;
    }
}
