package service_layer;

import data_access.CreditCardDAO;
import data_access.UserDAO;
import domain_model.Guest;
import domain_model.HotelDirector;
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
        reservationManager = new ReservationManager(this, calendarManager );
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
        user = null;
    }
    public User findUserByID(String id) {
        try {
            return userDao.findUserByID(id);
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

    public HotelDirector findHotelDirector(String hotelID) {
        try {
            return userDao.findHotelDirector(hotelID);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean doPayment(Guest user, double sum){
        double balance = 0;
        CreditCardDAO creditCardDAO = userDao.getCreditCardDAO();
        String cardNumber = user.getCard().getCardNumber();

        try {
            balance = creditCardDAO.getBalance(cardNumber);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if (balance >= sum) {
            balance -= sum;
            try {
                creditCardDAO.setBalance(cardNumber, balance);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Payment successful. Remaining balance: " + balance);
            return true;
        }else {
            throw new RuntimeException("Insufficient balance for the payment.");
        }
    }

    public void addBalance(Guest guest, double sum) {
        CreditCardDAO creditCardDAO = userDao.getCreditCardDAO();
        String cardNumber = guest.getCard().getCardNumber();
        try {
            double balance = creditCardDAO.getBalance(guest.getCard().getCardNumber());
            creditCardDAO.setBalance(cardNumber, balance+sum);
        }catch (SQLException e){
            throw new RuntimeException(e);
        }

    }


//end Region

}
