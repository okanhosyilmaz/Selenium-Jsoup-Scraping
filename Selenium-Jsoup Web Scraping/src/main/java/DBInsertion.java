import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class DBInsertion extends DriverSetUp{

    static String URLHEAD = "https://tmsearch.uspto.gov/";

    static Connection connection ;
    static Statement statement ;
    static Pattern pattern;
    static Matcher matcher;
    static SimpleDateFormat simpleDateFormat ;

    static public String dburl = "jdbc:mysql://localhost:3306/patent";
    static public String username = "root";
    static public String password = "";


    public static void usas() {

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
        BaseFunctions.closePopup();
        driver.findElement(By.cssSelector("input[name='p_s_PARA2']")).sendKeys("toshiba"); //// SEARCHBOX
        driver.findElement(By.xpath("//tbody/tr[4]/td[1]/input[3]")).click(); //// SEARCH BUTTON
        /////////////////// WEB ELEMENT LIST //////////////////////////////////////////////////////////
        List<WebElement> regNumber =  driver.findElements(By.cssSelector("table[id='searchResultTable']>tbody>tr>td:nth-child(3)")); ////REG NUMBER
        List<WebElement> serialNumberList = driver.findElements(By.cssSelector("table[id='searchResultTable']>tbody>tr>td:nth-child(2)")); ///// SERIAL NUMBER
        List<WebElement> serialNumberHrefList = driver.findElements(By.cssSelector("table[id='searchResultTable']>tbody>tr>td:nth-child(2)>a")); /// PRODUCT LINK
        List<WebElement> status = driver.findElements(By.cssSelector("table[id='searchResultTable']>tbody>tr>td:nth-child(6)")); /// LIVE DEAD
        List<WebElement> productNameList = driver.findElements(By.cssSelector("table[id='searchResultTable']>tbody>tr>td:nth-child(4)")); /// WORD MARK
        List<WebElement> checkStatusList = driver.findElements(By.cssSelector("table[id='searchResultTable']>tbody>tr>td:nth-child(5)")); /// TSDR

        ////////////////// DATABASE STRING DATA LIST  //////////////////////////////////////////////////////////
        List<String> serialNumber = new ArrayList<>();
        List<String> serialNumberHref = new ArrayList<>();
        List<String> statusList = new ArrayList<>();
        List<String> nameList = new ArrayList<>();
        List<String> registrationNumberList = new ArrayList<>();
        List<String> checkStatus = new ArrayList<>();

        for (WebElement serial : serialNumberList){

            serialNumber.add(serial.getText());
        }

        for (WebElement href : serialNumberHrefList){

            serialNumberHref.add(href.getAttribute("href"));
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

        for (WebElement check : checkStatusList){

            checkStatus.add(check.getText());
        }

        //////////////////// MYSQL USAS TABLE INSERTION ////////////////////////////////////////////////////////////
        for (int counter =0 ;counter<serialNumber.size();counter+=1) {
            try {
                String sql = "INSERT INTO `tm_usas` (`serial_number` , `reg_number` , `word_mark` ,`status` , `live_dead` , `link`) VALUES " +
                        "('" + serialNumber.get(counter) + "' ,'" + registrationNumberList.get(counter) + "' , '" + nameList.get(counter) + "' ," +
                        "'" + checkStatus.get(counter) + "' , '" + statusList.get(counter) + "' , '" + serialNumberHref.get(counter) + "')";
                statement.execute(sql);
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
        System.out.println("US TAMAMLANDI");
    }

    public static void cas(){

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
        driver.findElement(By.id("search-crit-1")).sendKeys("Apple");
        driver.findElement(By.cssSelector("button[class='btn btn-primary mrgn-rght-sm']")).click();

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //wait.until(driver -> driver.findElement(By.cssSelector("button[class='btn btn-primary mrgn-rght-sm']")).isDisplayed());

        /////////////////// WEB ELEMENT LIST //////////////////////////////////////////////////////////
        List<WebElement> applicationNumberList = driver.findElements(By.cssSelector("tr>td:nth-child(1)>div>a"));
        List<WebElement> trademarkList = driver.findElements(By.cssSelector("tr>td:nth-child(3)>a"));
        List<WebElement> typeList = driver.findElements(By.cssSelector("tr>td:nth-child(4)>div"));
        List<WebElement> CIPOList = driver.findElements(By.cssSelector("tr>td:nth-child(5)"));
        List<WebElement> niceClassList = driver.findElements(By.cssSelector("tr>td:nth-child(6)>section"));

        ////////////////// DATABASE STRING DATA LIST  //////////////////////////////////////////////////////////
        List<String> applicationNumber = new ArrayList<>();
        List<String> tradeMark = new ArrayList<>();
        List<String> CIPO = new ArrayList<>();
        List<String> imgSource = new ArrayList<>();
        List<String> type = new ArrayList<>();
        List<String> niceClass = new ArrayList<>();
        List<String> href = new ArrayList<>();


        for (WebElement appNumber : applicationNumberList){

            applicationNumber.add(appNumber.getText());
            href.add(appNumber.getAttribute("href"));

        }

        for (WebElement trademarkName : trademarkList){

            tradeMark.add(trademarkName.getText().replaceAll("(?=\\')." , ""));
        }

        for (WebElement cipo : CIPOList){

            CIPO.add(cipo.getText());
        }

        for (WebElement types : typeList){

            type.add(types.getText());
            if (types.getText().contains("Design")){

                imgSource.add(driver.findElement(By.cssSelector("tr>td:nth-child(7)>a>img")).getAttribute("src"));
            }
            else {
                imgSource.add("null");
            }
        }

        for (WebElement typeClass : niceClassList){

            niceClass.add(typeClass.getText());
        }

        //////////////////// MYSQL USAS TABLE INSERTION ////////////////////////////////////////////////////////////
        for (int counter =0 ;counter<applicationNumber.size();counter+=1) {
            try {
                String sql = "INSERT INTO `tm_cas` (`app_number` , `trademark` ,`types` , `cipo_status` , `nice_class` , `app_link` , `representation_link`) VALUES " +
                        "('" + applicationNumber.get(counter) + "' , '" + tradeMark.get(counter) + "' ," +
                        "'" + type.get(counter) + "' , '" + CIPO.get(counter) + "' , '" + niceClass.get(counter) + "' ," +
                        " '"+ href.get(counter) +"' , '"+ imgSource.get(counter) +"')";
                statement.execute(sql);
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }


    }

}
