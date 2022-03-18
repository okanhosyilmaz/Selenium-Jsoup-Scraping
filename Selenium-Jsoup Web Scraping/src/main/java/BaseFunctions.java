import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BaseFunctions extends DBInsertion {

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

    public static void closePopup(){

        try {
            /////////////////////// POP UP LOCATOR //////////////////////////////////////////////
            if (driver.findElement(By.id("fsrInvite")).isDisplayed());{

                wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[class='fsrButton fsrButton__inviteDecline fsrDeclineButton']"))).click();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static String currentDate(){

        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());

        return simpleDateFormat.format(date);
    }




}
