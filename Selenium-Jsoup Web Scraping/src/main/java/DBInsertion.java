import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


 public class DBInsertion extends DriverSetUp{

    static Connection connection ;
    static Statement statement ;
    static SimpleDateFormat simpleDateFormat ;

    static public String dburl = "jdbc:mysql://93.89.225.76/tbworion_effect";
    static public final String username = "tbworion_effect";
    static public final String password = "1Ytn0x21$$";

     public static String URLus = "https://tmsearch.uspto.gov/bin/gate.exe?f=login&p_lang=english&p_d=trmk";
     public static String URLcas = "https://www.ic.gc.ca/app/opic-cipo/trdmrks/srch/home";

     static List<String> errorOccuredProducts = new ArrayList<>();

    public static void usas(String brandName) {

        DriverSetUp.driverSetUp(URLus);

        ///////////////////////////// XAMP DATABASE CONNECTION ////////////////////////////////////////////////////////

        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(dburl , username , password);
            statement = connection.createStatement();
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println(e.getMessage());
        }

        try{
            /////////////////// LOCATORS //////////////////////////////////////////////////////
            driver.findElement(By.xpath("//a[contains(text(),'Basic Word Mark Search (New User)')]")).click();
            BaseFunctions.closePopup();
            driver.findElement(By.cssSelector("input[name='p_s_PARA2']")).sendKeys(brandName); //// SEARCHBOX
            driver.findElement(By.xpath("//tbody/tr[4]/td[1]/input[3]")).click(); //// SEARCH BUTTON
        } catch (Exception e) {
            System.out.println("ERROR US " + e.getMessage() + " Occured");
            ////////////////// ADD MISSED BRANDS TO LIST //////////////////////////////////
            if (errorOccuredProducts.contains(brandName)){
                driver.close();
            }
            else {
                errorOccuredProducts.add(brandName);
                driver.close();
            }
        }


        ///////////////////// JUST 1 RECORD OCCURED ////////////////////////////////////////

        System.out.println(driver.getTitle());
        if (driver.getTitle().contains("Trademark")){

            System.out.println("SINGLE PRODUCT");
            BaseFunctions.closePopup();

            List<WebElement> serialNumberList = driver.findElements(By.xpath("//td/b[text()=\"Serial Number\"]/following::td[1]"));
            List<WebElement> statusList = driver.findElements(By.xpath("//td/b[text()=\"Live/Dead Indicator\"]/following::td[1]"));
            List<WebElement> productNameList = driver.findElements(By.xpath("//td/b[text()=\"Word Mark \"]/following::td[1]/b"));

            List<String> serialNumber = new ArrayList<>();
            List<String> status = new ArrayList<>();
            List<String> productName = new ArrayList<>();
        try {


            for (WebElement element : serialNumberList){

                serialNumber.add(element.getText());
            }
            for (WebElement element : statusList){

                status.add(element.getText());
            }
            for (WebElement element : productNameList){

                productName.add(element.getText().replaceAll("(?=\\')." , ""));
            }
        } catch (Exception e) {
            System.out.println("ERROR US " + e.getMessage() + " Occured");
            ////////////////// ADD MISSED BRANDS TO LIST //////////////////////////////////
            if (errorOccuredProducts.contains(brandName)){
                driver.close();
            }
            else {
                errorOccuredProducts.add(brandName);
                driver.close();
            }
        }
            for (int counter =0 ;counter<serialNumber.size();counter+=1) {
                try {
                    String sql = "INSERT INTO `tm_usas` (`serial_number` , `reg_number` , `word_mark` ,`status` , `live_dead` , `link`) VALUES " +
                            "('" + serialNumber.get(counter) + "' ,'null' , '" + productName.get(counter) + "' ," +
                            "'" + status.get(counter) + "' , 'null' , 'null')";
                    statement.execute(sql);
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
        ///////////////////////// MORE RESULTS OCCURED ///////////////////////////////////////////////////////////////
        else if (driver.getTitle().contains("Record")){


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
    try {

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

            nameList.add(names.getText().replaceAll("(?=\\')." , ""));
        }

        for (WebElement check : checkStatusList){

            checkStatus.add(check.getText());
        }
    } catch (Exception e) {
        System.out.println("ERROR US " + e.getMessage() + " Occured");
        ////////////////// ADD MISSED BRANDS TO LIST //////////////////////////////////
        if (errorOccuredProducts.contains(brandName)){
            driver.close();
        }
        else {
            errorOccuredProducts.add(brandName);
            driver.close();
        }
    }
            //////////////////// MYSQL USAS TABLE INSERTION ////////////////////////////////////////////////////////////
        for (int counter =0 ;counter<serialNumber.size();counter+=1) {
            try {
                String sql = "INSERT INTO `tm_usas` (`serial_number` , `reg_number` , `word_mark` ,`status` , `live_dead` , `link`) VALUES " +
                        "('" + serialNumber.get(counter) + "' ,'" + registrationNumberList.get(counter) + "' , '" + nameList.get(counter) + "' ," +
                        "'" + checkStatus.get(counter) + "' , '" + statusList.get(counter) + "' , '" + serialNumberHref.get(counter) + "')";
                statement.execute(sql);
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }

        }
        }
        ////////////////////////////// 0 TRADEMARK AVAILABLE /////////////////////////////////////////////////////////
        else {
            System.out.println("Not Trademark Available");
            System.out.println("Product Can Be Sellable");
            ///////////////////////// MYSQL INSERTION ///////////////////////////////////////////////////////////////
            try {
                String sql = "INSERT INTO `tm_usas` (`serial_number` , `reg_number` , `word_mark` ,`status` , `live_dead` , `link`) VALUES" +
                        "('null' , 'null' , '"+brandName+"' , 'null' , 'null' , 'null'";
                statement.execute(sql);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        System.out.println("US COMPLETED");
    tearDown();
    }

    public static void cas(String brandName) {

        DriverSetUp.driverSetUp(URLcas);

        ///////////////////////////// XAMP DATABASE CONNECTION ////////////////////////////////////////////////////////
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(dburl, username, password);
            statement = connection.createStatement();
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println(e.getMessage());
        }

        /////////////////// LOCATORS //////////////////////////////////////////////////////
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.id("search-crit-1"))));
        try {
            Thread.sleep(500);
            try {
                driver.findElement(By.id("search-crit-1")).sendKeys(brandName);
                driver.findElement(By.cssSelector("button[class='btn btn-primary mrgn-rght-sm']")).click();
            } catch (Exception e) {
                //////////////// ADD MISSED BRANDS TO LIST /////////////////////////////////
                if (errorOccuredProducts.contains(brandName)){
                    driver.close();
                }
                else {
                    errorOccuredProducts.add(brandName);
                    driver.close();
                }
            }
            ///////////////// ASSERTION FOR CORRECT PRODUCT ///////////////////////////////////
            try {
                wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.cssSelector("h2[id='result-title']>span"))));
            } catch (Exception e) {
                System.out.println("ERROR " + e.getMessage() + " occured");
                ////////////////// ADD MISSED BRANDS TO LIST /////////////////////////////////
                if (errorOccuredProducts.contains(brandName)){
                    driver.close();
                }
                else {
                    errorOccuredProducts.add(brandName);
                    driver.close();
                }

            }
            driver.findElement(By.id("select-results-view-grid")).click();
            driver.findElement(By.id("select-results-view-list")).click();

            //////////////// ASSERTION FOR CORRECT PRODUCT ///////////////////////////////////
            String searchResult = driver.findElement(By.cssSelector("span[id='search-criteria-desc']>div:nth-child(2)>div+div")).getText();
            if (searchResult.equals("All")){
                driver.findElement(By.id("select-results-view-list")).click();
            }
        } catch (InterruptedException e) {
            System.out.println("ERROR " + e.getMessage() + " occured");
            ////////////////// ADD MISSED BRANDS TO LIST //////////////////////////////////
            errorOccuredProducts.add(brandName);
            driver.close();
        }


/*        String attribute = driver.findElement(By.id("printCriteria")).getAttribute("open");
        System.out.println(attribute);
        if (attribute.contains("true")){
            String inlineLocator = driver.findElement(By.cssSelector("span[id='search-criteria-desc']>div:nth-child(2)>div+div")).getText();
            if (inlineLocator.contains(brandName)){
                System.out.println("Correct Product");
            }
            else {
                driver.navigate().refresh();
                driver.findElement(By.xpath("//div[@id=\"regSelect2\"]/div/a")).click();
                wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[class='btn btn-primary mrgn-rght-sm']"))).click();
            }
        }*/
/*        driver.navigate().refresh();
       while (driver.findElement(By.id("criteria-2")).getCssValue("display").contains("block")){

           driver.findElement(By.xpath("//div[@id=\"regSelect2\"]/div/a")).click();
           wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[class='btn btn-primary mrgn-rght-sm']"))).click();
        }*/
/*
        String searchResultAppear = driver.findElement(By.id("verity-search-section")).getCssValue("display");
        System.out.println(searchResultAppear);
        if (searchResultAppear.equals("none")){
            driver.navigate().refresh();
            driver.findElement(By.xpath("//div[@id=\"regSelect2\"]/div/a")).click();
            wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[class='btn btn-primary mrgn-rght-sm']"))).click();
        }*/

        ////////////////////////////////////////// 0 TRADEMARK AVAILABLE ///////////////////////////////////////////////
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.cssSelector("h2[id='result-title']>span"))));
        String resultFound = driver.findElement(By.cssSelector("h2[id='result-title']>span")).getText();
        System.out.println(resultFound);
        if (resultFound.equals("0")) {
            System.out.println("There is 0 Trademark at this " + brandName);
            System.out.println("This product is sellable");

            try {
                String sql = "INSERT INTO `tm_cas` (`app_number` , `trademark` ,`types` , `cipo_status` , `nice_class` , `app_link` , `representation_link`) VALUES" +
                        " ('null' , '" + brandName.replaceAll("(?=\\').", "") + "' , 'null' , 'null' , 'null' , 'null' , 'null' )  ";
                statement.execute(sql);
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }


        ///////////////////////////////  MANY TRADEMARK AVAILABLE //////////////////////////////////////////////////////
        else {

            //////////////////////////// EXPLICIT WAIT FOR CAS PAGE LATENCY ///////////////////////////////////////////////
            wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.cssSelector("h2[id='result-title']>span"))));

            /////////////////// WEB ELEMENT LIST //////////////////////////////////////////////////////////
            List<WebElement> applicationNumberList = driver.findElements(By.cssSelector("tr>td:nth-child(1)>div>a"));
            List<WebElement> trademarkList = driver.findElements(By.cssSelector("tr>td:nth-child(3)>a"));
            List<WebElement> typeList = driver.findElements(By.cssSelector("tr>td:nth-child(4)>div"));
            List<WebElement> CIPOList = driver.findElements(By.cssSelector("tr>td:nth-child(5)"));
            List<WebElement> niceClassList = driver.findElements(By.cssSelector("tr>td:nth-child(6)>section"));
            List<WebElement> imgSourceList = driver.findElements(By.cssSelector("tr>td:nth-child(7)"));

            ////////////////// DATABASE STRING DATA LIST  //////////////////////////////////////////////////////////
            List<String> applicationNumber = new ArrayList<>();
            List<String> tradeMark = new ArrayList<>();
            List<String> CIPO = new ArrayList<>();
            List<String> imgSource = new ArrayList<>();
            List<String> type = new ArrayList<>();
            List<String> niceClass = new ArrayList<>();
            List<String> href = new ArrayList<>();

        try {
            for (WebElement appNumber : applicationNumberList) {

                applicationNumber.add(appNumber.getText());
                href.add(appNumber.getAttribute("href"));
            }

            for (WebElement trademarkName : trademarkList) {

                tradeMark.add(trademarkName.getText().replaceAll("(?=\\').", ""));
            }

            for (WebElement cipo : CIPOList) {

                CIPO.add(cipo.getText());
            }
            /////////////////// FOR TAKE CORRECT ORDER OF IMG SRC ////////////////////////////////////////////
            int innerCounter = 1;
            for (WebElement types : typeList) {

                type.add(types.getText());
                if (types.getText().contains("Design")) {
                    String s = imgSourceList.get(innerCounter - 1).findElement(By.cssSelector("tr>td:nth-child(7)>a>img")).getAttribute("src") + ".png";
                    imgSource.add(s);
                } else {
                    imgSource.add("null");
                }
                innerCounter += 1;
            }

            for (WebElement typeClass : niceClassList) {

                niceClass.add(typeClass.getText());
            }
        } catch (Exception e) {
            System.out.println("ERROR "+ e.getMessage()+" occured");
            //////////////// MISSED PRODUCT ADD TO LIST ////////////////////////////
            if (errorOccuredProducts.contains(brandName)){
                driver.close();
            }
            else {
                errorOccuredProducts.add(brandName);
                driver.close();
            }
        }


            //////////////////// MYSQL CAS TABLE INSERTION ////////////////////////////////////////////////////////////
            for (int counter = 0; counter < applicationNumber.size(); counter += 1) {
                try {
                    String sql = "INSERT INTO `tm_cas` (`app_number` , `trademark` ,`types` , `cipo_status` , `nice_class` , `app_link` , `representation_link`) VALUES " +
                            "('" + applicationNumber.get(counter) + "' , '" + tradeMark.get(counter) + "' ," +
                            "'" + type.get(counter) + "' , '" + CIPO.get(counter) + "' , '" + niceClass.get(counter) + "' ," +
                            " '" + href.get(counter) + "' , '" + imgSource.get(counter) + "')";
                    statement.execute(sql);
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }

            }
            System.out.println("CAS COMPLETED");
            System.out.println(errorOccuredProducts);
        }
    tearDown();
    }

}
