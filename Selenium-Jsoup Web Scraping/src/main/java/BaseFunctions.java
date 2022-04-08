import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class BaseFunctions extends DBInsertion {

    static Connection connection;
    static Statement statement;

    public static void clearCAS(){

        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(dburl , username , password);
            statement = connection.createStatement();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        try {
            String sql = "DELETE FROM `tm_cas`";
            statement.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static void clearUSAS(){

        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(dburl , username , password);
            statement = connection.createStatement();
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println(e);
        }

        try {
            String sql = "DELETE FROM `tm_usas`";
            statement.execute(sql);
        } catch (SQLException e) {
            System.out.println(e);
        }

    }

    public static void closePopup() {
        /////////////////////// POP UP LOCATOR //////////////////////////////////////////////
        List<WebElement> popup = driver.findElements(By.cssSelector("div[class='__fsr ']"));

        if (popup.size()>0) {
            driver.findElement(By.cssSelector("button[class='fsrButton fsrButton__inviteDecline fsrDeclineButton']")).click();
            System.out.println("popup occured");
        }
    }

    public static String currentDate(){

        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());

        return simpleDateFormat.format(date);
    }

    public static void wait(int seconds){

        try {
            Thread.sleep(seconds *1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }




}
