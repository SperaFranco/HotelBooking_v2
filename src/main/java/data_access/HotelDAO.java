package data_access;

import domain_model.Hotel;
import domain_model.HotelCalendar;
import domain_model.Room;
import service_layer.CalendarManager;
import utilities.HotelRating;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;

public class HotelDAO {
    private final Connection connection;
    private final HotelCalendarDAO hotelCalendarDAO;
    private final CalendarManager calendarManager;

    public HotelDAO(CalendarManager calendarManager) {
        connection = ConnectionManager.connect();
        this.hotelCalendarDAO = new HotelCalendarDAO();
        this.calendarManager = calendarManager;
    }

    public void addHotel(Hotel hotel) throws SQLException {
        String sql = "INSERT OR IGNORE INTO Hotel (id, name , city, address, telephone, rating, description, director_id) VALUES (?, ?, ?, ?, ?, ?,?,?)";
        PreparedStatement statement = connection.prepareStatement(sql);

        statement.setString(1, hotel.getId());
        statement.setString(2, hotel.getName());
        statement.setString(3, hotel.getCity());
        statement.setString(4, hotel.getAddress());
        statement.setString(5, hotel.getTelephone());
        statement.setString(6, hotel.getRating().toString());
        statement.setString(7, hotel.getDescription());
        statement.setString(8, hotel.getDirectorID());
        statement.executeUpdate();
        statement.close();

        addHotelRooms(hotel.getId(), hotel.getRooms());
        hotelCalendarDAO.addCalendar(hotel.getCalendar());
    }

    private void addHotelRooms(String hotelID, ArrayList<Room> rooms) throws SQLException {
        //TODO Vedere se è possible inserire piu valori insieme
        for (Room room : rooms) {
            String sql = "INSERT OR IGNORE INTO HotelRooms (hotel_id, room_id) VALUES (?,?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, hotelID);
            statement.setString(2, room.getId());
            statement.executeUpdate();
            statement.close();
        }

    }

    public void removeHotel(String id) throws SQLException {
        String sql = "DELETE FROM Hotel WHERE id = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        //TODO probabilmente vanno cancellate anche altre cose --> delete on cascade?
        statement.setString(1, id);
        statement.executeUpdate();
        statement.close();

    }

    public ArrayList<Hotel> getAllHotels() throws SQLException {
        ArrayList<Hotel> hotels = new ArrayList<>();
        //TODO ovviamente da cambiare
        PreparedStatement statement = null;
        ResultSet rs = null;

        String sql = "SELECT * FROM Hotel";
        try {
            statement = connection.prepareStatement(sql);
            rs = statement.executeQuery();

            while (rs.next()) {
                String id = rs.getString("id");
                String name = rs.getString("name");
                String city = rs.getString("city");
                String address = rs.getString("address");
                String telephone = rs.getString("telephone");
                String rating = rs.getString("rating");
                String description = rs.getString("description");
                String director_id = rs.getString("director_id");

                hotels.add(new Hotel(id, name, city, address,
                        telephone, HotelRating.getRatingFromString(rating), description, director_id, calendarManager));
            }
            return hotels;
        }
        finally {
            if (rs != null)
                rs.close();
            if (statement != null)
                statement.close();
        }
    }
}
