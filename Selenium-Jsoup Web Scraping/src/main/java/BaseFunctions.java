import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class BaseFunctions{

    static Connection connection;
    static Statement statement;
    public static void clearDB(){

        String dburl = "jdbc:mysql://localhost:3306/patent";
        String username = "root";
        String password = "";
        int counter = 1;


        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(dburl , username , password);
            statement = connection.createStatement();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            String sql = "DELETE FROM `patentdata`";
            statement.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}
