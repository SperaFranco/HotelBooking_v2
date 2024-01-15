package service_layer;

import data_access.UserDAO;
import domain_model.Guest;
import domain_model.HotelDirector;
import domain_model.User;

import java.sql.SQLException;

public class AccountManager {
    private static AccountManager accountManager;
    private final UserDAO userDao;

    public AccountManager(){
        this.userDao = new UserDAO();
    }
    public static AccountManager createAccountManager(){
        if(accountManager == null)
            accountManager = new AccountManager();
        return accountManager;
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
    public User findUserByID(String id) {
        try {
            return userDao.findUserByID(id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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
        String cardNumber = user.getCard().getCardNumber();

        try {
            balance = userDao.getBalance(cardNumber);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if (balance >= sum) {
            balance -= sum;
            try {
                userDao.setBalance(cardNumber, balance);
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
        String cardNumber = guest.getCard().getCardNumber();
        try {
            double balance = userDao.getBalance(guest.getCard().getCardNumber());
            userDao.setBalance(cardNumber, balance+sum);
        }catch (SQLException e){
            throw new RuntimeException(e);
        }

    }

}
