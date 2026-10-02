import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String URL = "jdbc:mysql://localhost:3306/tackledtask";
    private static final String USER = "root";
    private static final String PASSWORD = ""; // XAMPP default (empty)

    public static Connection getConnection() {
        Connection conn = null;
        try {
            // Load MySQL driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Create connection
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Database connected successfully!");

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            System.out.println("Database connection failed!");
        }
        return conn;
    }
}