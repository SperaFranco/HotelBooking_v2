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

    public void deleteUser(User user) throws SQLException {
        switch (user.getType()) {
            case GUEST ->
                    deleteGuest((Guest) user);
            case HOTEL_DIRECTOR ->
                    deleteHotelDirector(user.getId());
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

    private void deleteHotelDirector(String id) throws SQLException {
        String sql = "DELETE FROM HotelDirector WHERE id = ?";
        try(PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, id);
            statement.executeUpdate();
            //TODO ci sarebbe da cancellare anche i suoi hotel se presenti
        }
    }

    private void deleteGuest(Guest guest) throws SQLException {
        String sql = "DELETE FROM Guest WHERE id = ?";
        try(PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, guest.getId());
            statement.executeUpdate();
        }
        if (guest.getCard() != null)
            creditCardDAO.deleteCard(guest.getCard().getCardNumber());
        //TODO ci sarebbero da cancellare anche le sue prenotazioni
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
                    return new Guest(id, name, surname, email, telephone, password, card, UserType.GUEST);
                }
                return new HotelDirector(id, name, surname, email, telephone, password, UserType.HOTEL_DIRECTOR);
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
    public User findUserByID(String id) throws SQLException {
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
                    return new Guest(id, name, surname, email, telephone, password, card, UserType.GUEST);
                }
                return new HotelDirector(id, name, surname, email, telephone, password, UserType.HOTEL_DIRECTOR);
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

    public HotelDirector findHotelDirector(String hotelID) throws SQLException {
        String sql = "SELECT hd.id AS director_id, hd.name, hd.surname, hd.email, hd.telephone, hd.password " +
                    "FROM HotelDirector hd JOIN Hotel h on hd.id = h.director_id WHERE h.id = ?";
        try(PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, hotelID);

            try(ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String id = resultSet.getString("director_id");
                    String name = resultSet.getString("name");
                    String surname = resultSet.getString("surname");
                    String email = resultSet.getString("email");
                    String telephone = resultSet.getString("telephone");
                    String password = resultSet.getString("password");
                    return new HotelDirector(id, name, surname, email, telephone, password, UserType.HOTEL_DIRECTOR);
                }
            }
        }
        return null;
    }
}
