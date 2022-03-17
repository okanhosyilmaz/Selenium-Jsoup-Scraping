import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class BaseFunctions extends App{

    static Connection connection;
    static Statement statement;

    public static void clearDB(){

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
