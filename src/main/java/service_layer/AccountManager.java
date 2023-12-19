package service_layer;

import data_access.UserDAO;
import domain_model.User;

import java.sql.SQLException;

public class AccountManager {
    //Questi non andrebbero qui... TODO vedi se fare classe singleton
    private final HotelManager hotelManager;
    private final ReservationManager reservationManager;
    private final CalendarManager calendarManager;
    private final UserDAO userDao;

    public AccountManager(){
        this.userDao = new UserDAO();
        calendarManager = new CalendarManager();
        reservationManager = new ReservationManager(calendarManager);
        hotelManager = new HotelManager(calendarManager, reservationManager);
    }
    public void doRegistration(User user) {
        if (user == null)  throw new RuntimeException("user is a null reference");
        try {
            userDao.addUser(user);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteUser(User user) {
        if (user == null)  throw new RuntimeException("user is a null reference");
        try {
            userDao.deleteUser(user);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public User login(String email, String password) {

        User loginUser = null;
        try {
            loginUser = userDao.findUserByEmail(email, reservationManager, hotelManager );
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
        user = null;
    }
    public User findUserByID(String id, ReservationManager reservationManager, HotelManager hotelManager) {
        try {
            return userDao.findUserByID(id, reservationManager, hotelManager);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public UserDAO getUserDao(){ return userDao; }

    public HotelManager getHotelManager() {
        return hotelManager;
    }

    public ReservationManager getReservationManager() {
        return reservationManager;
    }

    public CalendarManager getCalendarManager() {
        return calendarManager;
    }



//end Region

}
