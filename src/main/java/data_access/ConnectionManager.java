package data_access;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {
   private static Connection connection;
   private static final String dbPath = "/home/regino/Scrivania/SWE/HotelBooking_v2/HotelBooking.db"; //questo Franco lo devi modificare

   private ConnectionManager() throws SQLException, ClassNotFoundException {
       Class.forName("org.sqlite.JDBC"); //carico il driver di JDBC
       connection = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
       System.out.println("Connected to database");
   }
   public static Connection connect() {
       try {
           if(connection != null && !connection.isClosed()) {
               return connection;
           }

          new ConnectionManager();

       }catch (SQLException e){
           System.out.println("Failed to connect to the database.");
           e.printStackTrace();
       }catch (ClassNotFoundException e) {
           System.out.println("JDBC driver not found");
           e.printStackTrace();
       }
       return connection;
   }

   public static Connection disconnect(Connection connection) {
       try {
           if (connection != null && !connection.isClosed()) {
               connection.close();
               System.out.println("Disconnected from database");
           }
       }catch (SQLException e) {
           System.out.println("Failed to disconnect from database");
           e.printStackTrace();
       }
       return null;
   }

}
