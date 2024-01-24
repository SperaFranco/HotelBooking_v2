package service_layer;

import data_access.UserDAO;
import domain_model.Guest;
import domain_model.HotelDirector;
import domain_model.User;

import java.sql.SQLException;

public class AccountManager {
    private static AccountManager accountManager;
    private final UserDAO userDao;

    private AccountManager(){
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
            if(findUserByID(user.getId()) != null)
                throw new SQLException("utente già presente");
            userDao.addUser(user);
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
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

        loginUser = findUserByEmail(email);
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

    public User findUserByEmail(String email) {
        try {
            return userDao.findUserByEmail(email);
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
        String cardNumber = user.getCard().getCardNumber();
        double balance = user.getCard().getBalance();

        if (balance >= sum) {
            try {
                balance -= sum;
                userDao.setBalance(cardNumber, balance);
                user.getCard().setBalance(balance);
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
