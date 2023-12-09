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

        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, card.getCardHolderName());
        statement.setString(2, card.getCardNumber());
        statement.setString(3, card.getExpiryDate());
        statement.setInt(4, card.getCVV());
        statement.setDouble(5, card.getBalance());
        statement.executeUpdate();
        statement.close();

    }
}
