import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class App extends DriverSetUp{

    static Connection connection ;
    static Statement statement ;
    static Pattern pattern;
    static Matcher matcher;
    static SimpleDateFormat simpleDateFormat ;

    static public String dburl = "jdbc:mysql://localhost:3306/patent";
    static public String username = "root";
    static public String password = "";


    public static void main(String[] args) {
        driverSetUp();

        //////////////////////////////// DATE FORMAT ///////////////////////////////////////////////////
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());

        ///////////////////////////// XAMP DATABASE CONNECTION ////////////////////////////////////////////////////////

        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(dburl , username , password);
            statement = connection.createStatement();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        /////////////////// LOCATORS //////////////////////////////////////////////////////
        driver.findElement(By.xpath("//a[contains(text(),'Basic Word Mark Search (New User)')]")).click();
        driver.findElement(By.cssSelector("input[name='p_s_PARA2']")).sendKeys("sony");
        driver.findElement(By.xpath("//tbody/tr[4]/td[1]/input[3]")).click();
        /////////////////// WEB ELEMENT LIST //////////////////////////////////////////////////////////
        List<WebElement> regNumber =  driver.findElements(By.cssSelector("table[id='searchResultTable']>tbody>tr>td:nth-child(3)"));
        List<WebElement> serialNumberList = driver.findElements(By.cssSelector("table[id='searchResultTable']>tbody>tr>td:nth-child(2)"));
        List<WebElement> status = driver.findElements(By.cssSelector("table[id='searchResultTable']>tbody>tr>td:nth-child(6)"));
        List<WebElement> productNameList = driver.findElements(By.cssSelector("table[id='searchResultTable']>tbody>tr>td:nth-child(4)"));

        ////////////////// DATABASE DATA LIST  //////////////////////////////////////////////////////////
        List<String> serialNumber = new ArrayList<>();
        List<String> statusList = new ArrayList<>();
        List<String> nameList = new ArrayList<>();
        List<String> registrationNumberList = new ArrayList<>();
        
        for (WebElement serial : serialNumberList){

            serialNumber.add(serial.getText());
        }

        for (WebElement registration : regNumber){
            String registrationNumberText = registration.getText();
            registrationNumberList.add(registrationNumberText);
        }

        for (WebElement stats : status){

            statusList.add(stats.getText());
        }

        for (WebElement names : productNameList){

            nameList.add(names.getText());
        }

        for (int counter =0 ;counter<serialNumber.size();counter+=1) {
            try {
                System.out.println(counter);
                String sql = "INSERT INTO `patentdata` (`no` , `product-name` , `serial-number` ,`reg-number` , `status` , `time`) VALUES " +
                        "('" + (counter + 1) + "' ,'" + nameList.get(counter) + "' , '" + serialNumber.get(counter) + "' ," +
                        "'" + registrationNumberList.get(counter) + "' , '" + statusList.get(counter) + "' , '" + simpleDateFormat.format(date) + "')";
                statement.execute(sql);
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }

        tearDown();

    }

}
