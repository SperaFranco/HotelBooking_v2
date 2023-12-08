package data_access;

import domain_model.*;
import service_layer.*;
import utilities.UserType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {
    private final Connection connection;
    private final CreditCardDAO creditCardDAO;

    private final ReservationManager reservationManager;
    private final HotelManager hotelManager;

    public UserDAO(ReservationManager reservationManager, HotelManager hotelManager) {
        connection = ConnectionManager.connect();
        creditCardDAO = new CreditCardDAO();
        this.reservationManager = reservationManager;
        this.hotelManager = hotelManager;
    }

    public void addUser(User user) throws SQLException {
        switch (user.getType()) {
            case GUEST ->
                addGuest((Guest)user);
            case HOTEL_DIRECTOR ->
                addHotelDirector((HotelDirector)user);
        }
    }

    private void addHotelDirector(HotelDirector user) throws SQLException {
        String sql = "INSERT OR IGNORE INTO HotelDirector (id, name , surname, email, telephone, password) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, user.getId());
        statement.setString(2, user.getName());
        statement.setString(3, user.getSurname());
        statement.setString(4, user.getEmail());
        statement.setString(5, user.getTelephone());
        statement.setString(6, user.getPassword());
        statement.executeUpdate();
        statement.close();
    }
    private void addGuest(Guest user) throws SQLException {
        String sql = "INSERT OR IGNORE INTO Guest (id, name , surname, email, telephone, password, card_number) VALUES (?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, user.getId());
        statement.setString(2, user.getName());
        statement.setString(3, user.getSurname());
        statement.setString(4, user.getEmail());
        statement.setString(5, user.getTelephone());
        statement.setString(6, user.getPassword());
        statement.setString(7, user.getCard().getCardNumber());
        statement.executeUpdate();
        statement.close();
    }

    public User findUserByEmail(String email) throws SQLException {
        //TODO Franco lo migliora --> unire le due tabelle
        //Per come ho progettato il db dovrei andare a cercare su due tabelle
        // e a seconda di dove trovo l'user impostare certi campi
        String sql = "SELECT 'HotelDirector' AS userType, *, NULL AS extra_column  FROM HotelDirector WHERE email = ?" +
                " UNION " + "SELECT 'Guest' AS userType, * FROM Guest WHERE email = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, email);
        statement.setString(2, email);
        ResultSet rs = statement.executeQuery();

        if (rs.next()) {
            String userType = rs.getString("userType");
            String id = rs.getString("id");
            String name = rs.getString("name");
            String surname = rs.getString("surname");
            String telephone = rs.getString("telephone");
            String password = rs.getString("password");

            if(userType.equals("Guest")) {
                String card_number = rs.getString("card_number");
                CreditCard card = creditCardDAO.findCreditCard(card_number);
                return new Guest(id, name, surname, email,telephone, password, card, reservationManager, UserType.GUEST);
            }
            return new HotelDirector(id, name, surname, email, telephone, password, hotelManager, UserType.HOTEL_DIRECTOR);
        }
        return null;
    }


}