package data_access;

import domain_model.*;
import service_layer.*;
import utilities.UserType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {
    private Connection connection;
    private final CreditCardDAO creditCardDAO;


    public UserDAO() {
        connection = ConnectionManager.connect();
        creditCardDAO = new CreditCardDAO();
    }

    public void addUser(User user) throws SQLException {
        switch (user.getType()) {
            case GUEST ->
                addGuest((Guest)user);
            case HOTEL_DIRECTOR ->
                addHotelDirector((HotelDirector)user);
        }
    }

    public void DeleteUser(User user) throws SQLException {
        switch (user.getType()) {
            case GUEST ->
                    DeleteGuest(user.getId());
            case HOTEL_DIRECTOR ->
                    DeleteHotelDirector(user.getId());
        }
    }



    private void addHotelDirector(HotelDirector user) throws SQLException {
        String sql = "INSERT OR IGNORE INTO HotelDirector (id, name , surname, email, telephone, password) VALUES (?, ?, ?, ?, ?, ?)";
        try(PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, user.getId());
            statement.setString(2, user.getName());
            statement.setString(3, user.getSurname());
            statement.setString(4, user.getEmail());
            statement.setString(5, user.getTelephone());
            statement.setString(6, user.getPassword());
            statement.executeUpdate();
        }
    }
    private void addGuest(Guest user) throws SQLException {
        String sql = "INSERT OR IGNORE INTO Guest (id, name , surname, email, telephone, password, card_number) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try(PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, user.getId());
            statement.setString(2, user.getName());
            statement.setString(3, user.getSurname());
            statement.setString(4, user.getEmail());
            statement.setString(5, user.getTelephone());
            statement.setString(6, user.getPassword());
            statement.setString(7, user.getCard().getCardNumber());
            statement.executeUpdate();
        }
        creditCardDAO.addCreditCard(user.getCard());
    }

    private void DeleteHotelDirector(String id) throws SQLException {
        String sql = "DELETE FROM HotelDirector WHERE id = ?";
        try(PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, id);
            statement.executeUpdate();
        }
    }

    private void DeleteGuest(String id) throws SQLException {
        String sql = "DELETE FROM Guest WHERE id = ?";
        try(PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, id);
            statement.executeUpdate();
        }
    }

    public User findUserByEmail(String email, ReservationManager reservationManager, HotelManager hotelManager) throws SQLException {
        //Per come ho progettato il db dovrei andare a cercare su due tabelle
        // e a seconda di dove trovo l'user impostare certi campi
        String sql = "SELECT 'HotelDirector' AS userType, *, NULL AS card_number  FROM HotelDirector WHERE email = ?" +
                " UNION " + "SELECT 'Guest' AS userType, * FROM Guest WHERE email = ?";
        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            statement = connection.prepareStatement(sql);
            statement.setString(1, email);
            statement.setString(2, email);
             rs = statement.executeQuery();

            if (rs.next()) {
                String userType = rs.getString("userType");
                String id = rs.getString("id");
                String name = rs.getString("name");
                String surname = rs.getString("surname");
                String telephone = rs.getString("telephone");
                String password = rs.getString("password");

                if (userType.equals("Guest")) {
                    String card_number = rs.getString("card_number");
                    CreditCard card = creditCardDAO.findCreditCard(card_number);
                    return new Guest(id, name, surname, email, telephone, password, card, reservationManager, UserType.GUEST);
                }
                return new HotelDirector(id, name, surname, email, telephone, password, hotelManager, UserType.HOTEL_DIRECTOR);
            }
        }
        finally {
            if (rs != null)
                rs.close();
            if (statement != null)
                statement.close();
        }
        return null;
    }
    public User findUserByID(String id, ReservationManager reservationManager, HotelManager hotelManager) throws SQLException {
        //Per come ho progettato il db dovrei andare a cercare su due tabelle
        // e a seconda di dove trovo l'user impostare certi campi
        String sql = "SELECT 'HotelDirector' AS userType, *, NULL AS card_number  FROM HotelDirector WHERE email = ?" +
                " UNION " + "SELECT 'Guest' AS userType, * FROM Guest WHERE id = ?";
        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            statement = connection.prepareStatement(sql);
            statement.setString(1, id);
            statement.setString(2, id);
            rs = statement.executeQuery();

            if (rs.next()) {
                String userType = rs.getString("userType");
                String name = rs.getString("name");
                String surname = rs.getString("surname");
                String email = rs.getString("email");
                String telephone = rs.getString("telephone");
                String password = rs.getString("password");

                if (userType.equals("Guest")) {
                    String card_number = rs.getString("card_number");
                    CreditCard card = creditCardDAO.findCreditCard(card_number);
                    return new Guest(id, name, surname, email, telephone, password, card, reservationManager, UserType.GUEST);
                }
                return new HotelDirector(id, name, surname, email, telephone, password, hotelManager, UserType.HOTEL_DIRECTOR);
            }
        }
        finally {
            if (rs != null)
                rs.close();
            if (statement != null)
                statement.close();
        }
        return null;
    }
    public void disconnect() {
        this.connection = ConnectionManager.disconnect(this.connection);
    }

}
