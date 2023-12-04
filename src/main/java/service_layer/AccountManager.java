package service_layer;

import domain_model.CreditCard;
import domain_model.User;
import domain_model.Guest;
import domain_model.HotelDirector;
import utilities.IdGenerator;
import utilities.UserType;

import java.util.ArrayList;
import java.util.Scanner;

public class AccountManager {
    private final ArrayList<User> users = new ArrayList<>();
    private final ReservationManager reservationManager;
    private final HotelManager hotelManager;
    private final CalendarManager calendarManager;

    public AccountManager(){

        this.calendarManager = new CalendarManager();
        this.reservationManager =  new ReservationManager( this, calendarManager);
        this.hotelManager = new HotelManager(reservationManager, calendarManager);

    }
    public void doRegistration(User user) {
        if (user == null)  throw new RuntimeException("user is a null reference");
        users.add(user);
    }
    public User login(String email, String password) {

        User loginUser = findUserByEmail(email);
        if(loginUser == null)
            throw new RuntimeException("User not found");
        if(loginUser.getPassword().equals(password))
            return loginUser;
        else {
            throw new RuntimeException("Password is not correct");
        }

    }
    public void logout(User user) {
        users.remove(user);
        user = null;
    }
    public ReservationManager getReservationManager() {
        return reservationManager;
    }
    public HotelManager getHotelManager() {
        return hotelManager;
    }
    public CalendarManager getCalendarManager() {
        return calendarManager;
    }
    private User findUserByEmail(String email) {

        for (User user:users) {
            if (user.getEmail().equalsIgnoreCase(email))
                return user;
        }

        return null;
    }

    //end Region

}
