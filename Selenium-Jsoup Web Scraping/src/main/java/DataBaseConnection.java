import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;

public class DataBaseConnection extends DBInsertion {
/* CREATE TRIGGER `trademark_brand_trigger` AFTER INSERT ON `webscrap_products`
 FOR EACH ROW update webscrap_products a set
                    a.trademark_status = 1,
                    a.trademark_status_date = sysdate()
                    where `a`.`brand_name` is not null
                    and `a`.`trademark_status` = 0
                    and `a`.`trademark_status_date` is null
                        and a.brand_name in(
                        SELECT w.brand_name FROM webscrap_products w
                        WHERE w.trademark_status=1
                        and a.brand_name=w.brand_name
                        and datediff(date_format(w.created_at,'%Y-%m-%d'),date_format(w.trademark_status_date,'%Y-%m-%d'))<90)

CREATE TRIGGER `trademark_manufacturer_trigger` AFTER INSERT ON `webscrap_products`
 FOR EACH ROW update webscrap_products a set
                    a.trademark_status = 1,
                    a.trademark_status_date = sysdate()
                    where a.brand_name is null
                    and a.manufacturer is not null
                    and a.trademark_status = 0
                    and a.trademark_status_date is null
                        and a.manufacturer in(
                        SELECT w.manufacturer FROM webscrap_products w
                        WHERE w.trademark_status=1
                        and a.manufacturer=w.manufacturer
and datediff(date_format
(w.created_at,'%Y-%m-%d'),date_format(w.trademark_status_date,'%Y-%m-%d'))<90)
 */
    static Connection connection;
    static Statement statement;
    static public String DBURL = "jdbc:mysql://93.89.225.76/tbworion_effect";
    static public final String USERNAME = "tbworion_effect";
    static public final String PASSWORD = "1Ytn0x21$$";

    public static void main(String[] args) throws InterruptedException {

        ///////////////////////// XAMPP CONNECTION /////////////////////////////////
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(DBURL, USERNAME, PASSWORD);
            statement = connection.createStatement();
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println(e.getMessage());
        }

        ////////////////////////// GET BRANDNAME INSIDE DB ////////////////////
        List<String> brandList = new ArrayList<>();
        int counter = 0;

        //////////// INFINITE LOOP FOR CONTINIOUS RUN ////////////////////////////
        while (true) {
          /*  //////////////// NEW VARIABLE WHICH SCANNED BEFORE TRADEMARK_STATUS CHANGE //////////////////////
            ///////////////// BRANDNAME UPDATE //////////////////////////////
            String sql = "update webscrap_products a set \n" +
                    "a.trademark_status = 1,\n" +
                    "a.trademark_status_date = sysdate()\n" +
                    "where `a`.`brand_name` is not null \n" +
                    "and `a`.`trademark_status` = 0 \n" +
                    "and `a`.`trademark_status_date` is null \n" +
                        "and a.brand_name in(\n" +
                        "SELECT w.brand_name FROM webscrap_products w \n" +
                        "WHERE w.trademark_status=1\n" +
                        "and a.brand_name=w.brand_name\n" +
                        "and datediff(date_format(w.created_at,'%Y-%m-%d'),date_format(w.trademark_status_date,'%Y-%m-%d'))<90)";
            try {
                statement.execute(sql);
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
            /////////// MANUFACTURER UPDATE //////////////////////////////////////////
            String sql = "update webscrap_products a set \n" +
                    "a.trademark_status = 1,\n" +
                    "a.trademark_status_date = sysdate()\n" +
                    "where `a`.`brand_name` is null\n" +
                    "and `a`.`manufacturer` is not null\n" +
                    "and `a`.`trademark_status` = 0 \n" +
                    "and `a`.`trademark_status_date` is null \n" +
                    "and a.manufacturer in(\n" +
                    "SELECT w.manufacturer FROM webscrap_products w \n" +
                    "WHERE w.trademark_status=1\n" +
                    "and a.manufacturer=w.manufacturer\n" +
                    "and datediff(date_format(w.created_at,'%Y-%m-%d'),date_format(w.trademark_status_date,'%Y-%m-%d'))<90)";
            try {
                statement.execute(sql);
            } catch (SQLException e) {
                System.out.printf(e.getMessage());
            }*/

            //////////////// 90 DAY CONTROL SQL ///////////////////////////////////////
            String sql = "update webscrap_products a SET\n" +
                    "a.trademark_status=0,\n" +
                    "a.trademark_status_date=null\n" +
                    "where a.trademark_status=1\n" +
                    "and datediff(date_format(sysdate(),'%Y-%m-%d'),date_format(a.trademark_status_date,'%Y-%m-%d'))>90";
            try {
                statement.execute(sql);
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }

            try {
                /////////////////// BRAND NAME FROM VIEW //////////////////////////
                sql = "SELECT brand_name FROM `webscrap_trademark_view` ";
                ResultSet resultSet = statement.executeQuery(sql);
                while (resultSet.next()) {
                    counter += 1;
                    for (int i = 0; i < counter; i += 1) {
                        String resultStr = resultSet.getString("brand_name");
                        /////////// DO NOT ADD SAME VARIABLE /////////////////////////////
                        if (brandList.contains(resultStr)) {
                            continue;
                        }
                        ////////// DO NOT ADD NULL VARIABLE ///////////////////////
                        else if (resultStr == null) {
                            continue;
                        } else {
                            brandList.add(resultStr);
                        }
                    }
                }

            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }

            System.out.println(brandList);

            //////// IF LIST IS NULL THEN SLEEP 5 MIN //////////////////////////////////////
            if (brandList.size() == 0) {
                System.out.println("List is null");
                System.out.println("Threads will sleep 5 min more!");
                Thread.sleep(5*60000);
            }
            //////////////////// FUNCTION STARTED ////////////////////////////////
            for (String brandName : brandList) {

                Thread threadUS = null;
                try {
                    threadUS = new Thread(() -> {
                        System.out.println("Thread US Started to Search: " + brandName);
                        DBInsertion.usas(brandName);
                    });
                } catch (Exception e) {
                    driver.close();
                }

                Thread threadCAS = null;
                try {
                    threadCAS = new Thread(() -> {
                        System.out.println("Thread CAS Started to Search: " + brandName);
                        DBInsertion.cas(brandName);
                    });
                } catch (Exception e) {
                    driver.close();
                }
                ///////////////// UPDATE TABLE VARIABLES AFTER SCANNING SQL ///////////////////////////////
                Thread threadUPDATE = new Thread(() -> {
                    System.out.println("Thread UPDATE Started to update " + brandName + " variable!");
                    try {
                        String update = "UPDATE `webscrap_products` SET `trademark_status` = 1 , `trademark_status_date` = sysdate() WHERE " +
                                "( `brand_name`='" + brandName + "' OR `manufacturer` = '" + brandName + "')";
                        statement.execute(update);
                    } catch (SQLException e) {
                        System.out.println(e.getMessage());
                    }
                });
                //////// THREAD STARTED AND JOINED(wait for each other thread execution) ////////////////////////////
                threadUS.start();
                threadUS.join();
                threadCAS.start();
                threadCAS.join();
                threadUPDATE.start();
                threadUPDATE.join();
            }
            /////////////////// SLEEP 5 MIN ///////////////////////////////////////
            System.out.println("Thread Will Sleep 5 min");
            Thread.sleep(5*60000);
            ////// BRANDLIST CLEAR FOR PREVENT OVERWRITE /////////////////////////
            brandList.clear();
            ///////////////// CHANGE STATUS OF MISSED DATA AND TRY AGAIN //////////
            if (DBInsertion.errorOccuredProducts.size()>0){
                for (String errorData : DBInsertion.errorOccuredProducts){
                    try {
                        sql = "UPDATE `webscrap_products` SET `trademark_status` = 0 ," +
                                "`trademark_status_date` = '' " +
                                "WHERE `brand_name` = "+errorData+" OR  `manufacturer` = "+errorData+" ";
                        statement.execute(sql);
                    } catch (Exception e) {
                        System.out.println("ERROR " + e.getMessage() + " occured when ERRORED data insertion!");
                    }
                }
                DBInsertion.errorOccuredProducts.clear();
            }
        }
    }
}
