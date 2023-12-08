package service_layer;

import data_access.UserDAO;
import domain_model.User;

import java.sql.SQLException;
import java.util.ArrayList;

public class AccountManager {
    private final UserDAO userDao;
    private final ReservationManager reservationManager;
    private final HotelManager hotelManager;
    private final CalendarManager calendarManager;

    public AccountManager(){
        this.calendarManager = new CalendarManager();
        this.reservationManager =  new ReservationManager(calendarManager);
        this.hotelManager = new HotelManager(reservationManager, calendarManager);
        this.userDao = new UserDAO(reservationManager, hotelManager);

    }
    public void doRegistration(User user) {
        if (user == null)  throw new RuntimeException("user is a null reference");
        try {
            userDao.addUser(user);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public User login(String email, String password) {

        User loginUser = null;
        try {
            loginUser = userDao.findUserByEmail(email);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if(loginUser == null)
            throw new RuntimeException("User not found");

        if(loginUser.getPassword().equals(password))
            return loginUser;
        else {
            throw new RuntimeException("Password is not correct");
        }

    }
    public void logout(User user) {
       // users.remove(user);
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

    //end Region

}
