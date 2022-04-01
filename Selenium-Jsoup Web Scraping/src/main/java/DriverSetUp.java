import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.concurrent.TimeUnit;

public class DriverSetUp {

     static WebDriver driver;
     static WebDriverWait wait;
     static void driverSetUp(String URL){

        System.setProperty("webdriver.chrome.driver" , "src/driver/chromedriver.exe");
        driver = new ChromeDriver();
        driver.get(URL);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(30 , TimeUnit.SECONDS);
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
