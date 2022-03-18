import org.junit.jupiter.api.BeforeAll;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class DriverSetUp {

    static WebDriver driver;
    static WebDriverWait wait;
    public static void driverSetUp(String URL){

        System.setProperty("webdriver.chrome.driver" , "src/driver/chromedriver.exe");
        driver = new ChromeDriver();
        driver.get(URL);
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver , 60);
    }

    public static WebDriver getWebDriver(){

        return driver;
    }

    public static WebDriver setWebDriver(WebDriver driver){

        return driver;
    }

    public static void tearDown(){

        driver.quit();
    }



}
