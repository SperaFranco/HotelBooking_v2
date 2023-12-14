package data_access;

import domain_model.Hotel;
import domain_model.Room;
import service_layer.CalendarManager;
import service_layer.HotelManager;
import service_layer.ReservationManager;
import utilities.HotelRating;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class HotelDAO {
    private final Connection connection;
    private final HotelCalendarDAO hotelCalendarDAO;
    private final RoomDAO roomDAO;
    private final CalendarManager calendarManager;

    public HotelDAO(CalendarManager calendarManager) {
        connection = ConnectionManager.connect();
        this.calendarManager = calendarManager;
        this.hotelCalendarDAO = calendarManager.getCalendarDAO();
        this.roomDAO = new RoomDAO();
    }

    public void addHotel(Hotel hotel) throws SQLException {
        String sql = "INSERT OR IGNORE INTO Hotel (id, name , city, address, telephone, rating, description, director_id) VALUES (?, ?, ?, ?, ?, ?,?,?)";
        try(PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, hotel.getId());
            statement.setString(2, hotel.getName());
            statement.setString(3, hotel.getCity());
            statement.setString(4, hotel.getAddress());
            statement.setString(5, hotel.getTelephone());
            statement.setString(6, hotel.getRating().toString());
            statement.setString(7, hotel.getDescription());
            statement.setString(8, hotel.getDirectorID());
            statement.executeUpdate();
        }

        roomDAO.addRooms(hotel.getRooms(), hotel.getId());
        hotelCalendarDAO.addCalendar(hotel.getCalendar());
    }


    public void removeHotel(String id) throws SQLException {
        String sql = "DELETE FROM Hotel WHERE id = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        //TODO probabilmente vanno cancellate anche altre cose --> delete on cascade?
        statement.setString(1, id);
        statement.executeUpdate();
        statement.close();

    }

    public ArrayList<Hotel> getAllHotels(HotelManager hotelManager, ReservationManager reservationManager) throws SQLException {
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
                Hotel hotel = new Hotel(id, name, city, address,
                        telephone, HotelRating.getRatingFromString(rating), description, director_id, calendarManager);
                hotel.setRooms(roomDAO.getRoomsByID(id));
                hotel.setCalendar(hotelCalendarDAO.getCalendar(id, hotelManager, reservationManager));
                hotels.add(hotel);
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
