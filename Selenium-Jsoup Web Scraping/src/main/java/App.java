public class App extends DBInsertion{

    public static String URLus = "https://tmsearch.uspto.gov/bin/gate.exe?f=login&p_lang=english&p_d=trmk";
    public static String URLcas = "https://www.ic.gc.ca/app/opic-cipo/trdmrks/srch/home";
    public static void main(String[] args) {

        driverSetUp(URLus);
        DBInsertion.usas();
        tearDown();

        driverSetUp(URLcas);
        DBInsertion.cas();
        tearDown();



    }
}
