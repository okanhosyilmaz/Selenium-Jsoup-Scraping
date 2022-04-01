import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataBaseConnection extends DBInsertion {

    static Connection connection;
    static Statement statement;
    static public String DBURL = "jdbc:mysql://93.89.225.76/tbworion_effect";
    static public final String USERNAME = "tbworion_effect";
    static public final String PASSWORD = "1Ytn0x21.,";

    public static void main(String[] args) throws InterruptedException {

        ///////////////////////// XAMPP CONNECTION /////////////////////////////////
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(DBURL, USERNAME, PASSWORD);
            statement = connection.createStatement();
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println(e);
        }

        ////////////////////////// GET BRANDNAME INSIDE DB ////////////////////
        List<String> brandList = new ArrayList<>();
        int counter = 0;
        try {
            String sql = "SELECT brand_name FROM `webscrap_products` WHERE id BETWEEN 2521 AND 2526 ";
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
            System.out.println(e);
        }

        System.out.println(brandList);

        //////////////////// FUNCTION STARTED ////////////////////////////////

        while (brandList.size() >= 0) {

            if (brandList.size() == 0){
                Thread.sleep(300000);
            }

            for (String brandName : brandList) {

                Thread threadUS = new Thread(()->{
                    System.out.println("Thread US Started to Search: " +brandName);
                    DBInsertion.usas(brandName);
                });

                Thread threadCAS = new Thread(()->{
                    System.out.println("Thread CAS Started to Search: " +brandName);
                    DBInsertion.cas(brandName);
                });

                threadUS.start();
                threadUS.join();
                threadCAS.start();
                threadCAS.join();

            }
            System.out.println("Thread Will Sleep 5 min");
            Thread.sleep(300000);
        }
    }
}
