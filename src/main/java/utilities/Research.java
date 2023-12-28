package utilities;

import java.time.LocalDate;

public class Research {
    String city;
    LocalDate checkIn;
    LocalDate checkOut;
    int numOfGuest;

    public Research(String city, LocalDate checkIn, LocalDate checkOut, int numOfGuest){
        this.city = city;
        if (checkIn.isBefore(LocalDate.now()))
            throw new RuntimeException("Please insert a date not in the past");
        this.checkIn = checkIn;
        if (checkOut.isBefore(LocalDate.now()) && checkOut.isBefore(checkIn))
            throw new RuntimeException("Please insert a date not in the past");
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
