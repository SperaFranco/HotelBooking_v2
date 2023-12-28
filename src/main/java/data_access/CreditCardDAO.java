package data_access;

import domain_model.CreditCard;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CreditCardDAO {
    private Connection connection;

    public CreditCardDAO() {
        connection = ConnectionManager.connect();
    }

    public CreditCard findCreditCard(String card_number) throws SQLException {
        String sql = "SELECT * FROM CreditCard WHERE card_number = ?";

        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, card_number);
        ResultSet rs = statement.executeQuery();

        if (rs.next()) {
            String cardHolderName = rs.getString("card_holder_name");
            String cardNumber = rs.getString("card_number");
            String expiry_date = rs.getString("expiry_date");
            int cvv = rs.getInt("cvv");

            return new CreditCard(cardHolderName, cardNumber, expiry_date, cvv);
        }
        return null;
    }

    public void addCreditCard(CreditCard card) throws SQLException {
        String sql = "INSERT OR IGNORE INTO CreditCard (card_holder_name, card_number, expiry_date, cvv, balance) VALUES (?,?,?,?,?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, card.getCardHolderName());
            statement.setString(2, card.getCardNumber());
            statement.setString(3, card.getExpiryDate());
            statement.setInt(4, card.getCVV());
            statement.setDouble(5, card.getBalance());
            statement.executeUpdate();
        }
    }

    public void deleteCard(String cardNumber) throws SQLException {
        String sql = "DELETE FROM CreditCard WHERE card_number = ?";
        try(PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, cardNumber);
            statement.executeUpdate();
        }
    }

    public double getBalance(String cardNumber) throws SQLException{
        String sql = "SELECT balance FROM CreditCard WHERE card_number = ?";

        try(PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, cardNumber);
            try(ResultSet rs = statement.executeQuery()) {

                if (rs.next()) {
                    return rs.getDouble("balance");
                }
            }
        }
        return 0;
    }

    public void setBalance(String cardNumber, double balance) throws SQLException {
        String sql = "UPDATE CreditCard SET balance = ? WHERE card_number = ?";
        try(PreparedStatement statement =  connection.prepareStatement(sql)) {
            statement.setDouble(1, balance);
            statement.setString(2, cardNumber);
            statement.executeUpdate();
        }
    }
}
