import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class App extends DriverSetUp{

    static Connection connection ;
    static Statement statement ;

    public static void main(String[] args) {
        driverSetUp();

        String dburl = "jdbc:mysql://localhost:3306/patent";
        String username = "root";
        String password = "";
        int counter = 1;

///////////////////////////////////// XAMPP DATABASE CONNECTION ////////////////////////////////////////////////////////
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
        List<WebElement> searchResults = driver.findElements(By.cssSelector("table[id='searchResultTable']>tbody>tr"));
        ////////////////// DATABASE DATA LIST CREATION //////////////////////////////////
        List<String> serialNumber = new ArrayList<>();
        List<String> status = new ArrayList<>();


        for (WebElement data :searchResults){
            String text = data.getText();
            String[] parts = text.split("(?= )");
            //System.out.println(parts[1].toString().trim());

            serialNumber.add(parts[1].toString().trim());
            for (String s : parts){
                if (s.contains("DEAD")){
                    status.add(s);
                }
                else if(s.contains("LIVE")){
                    status.add(s);
                }
            }
        }

        System.out.println(serialNumber);
        System.out.println(status);

        /////////////////////////////////////// DATABASE SQL COMMENT INJECTION ////////////////////////////////////////
        for (;counter<51;counter+=1){
            try {
                String sql = "INSERT INTO `patentdata` (`no` , `serial-number` , `status`) VALUES ('"+(counter)+"' , '"+serialNumber.get(counter)+"' , '"+ status.get(counter-1)+"')";
                statement.execute(sql);
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }

        tearDown();

    }


}
