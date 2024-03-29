import java.sql.*;
import java.util.HashSet;

public class BDD {
    private static Connection myConn;

    //Function that provides the connection with our Database in MySQL
    //Sometimes, there is an error related with the 'Time Zone'
    //Just execute this query in mysql: SET GLOBAL time_zone = '+1:00'; (for Spain)
    public static void initializeConnection(){
        String dbUrl = "jdbc:mysql://localhost:3306/stock?useSSL=false";
        String user = "root";
        String pass = "root";
        try{
            //Comunicación con la base de datos
            myConn=DriverManager.getConnection( dbUrl, user, pass);
            System.out.println("Database connection successful!\n");
        }
        catch (Exception exc){
            exc.printStackTrace();
        }
    }

        /*

    Queries for MySQL in order to create the table:

use stock;
drop table if exists empresas;
CREATE TABLE empresas (
    Ticker varchar(15) NOT NULL,
    Nombre varchar(64) DEFAULT NULL,
    Sector varchar(64) DEFAULT NULL,
    Subsector varchar(64) DEFAULT NULL,
    `Payout/FCF(%)`float(4,2) DEFAULT NULL,
    `EV/FCF` float(4,2) DEFAULT NULL,
    `EV/EBITDA` float(4,2) DEFAULT NULL,
    PER float(4,2) DEFAULT NULL,
    `P/B` float(6,3) DEFAULT NULL,
    `P/S` float(6,3) DEFAULT NULL,
    Puntuacion float(6, 3) DEFAULT NULL,
    Numero_Acciones float(9,2) DEFAULT NULL,
    Dinero_Efectivo float(9,2) DEFAULT NULL,
    Deuda float(9,2) DEFAULT NULL,
    Flujo_Caja_Libre float(9,2) DEFAULT NULL,
    EBITDA float(9,2) DEFAULT NULL,
    Beneficio_Neto float(9,2) DEFAULT NULL,
    Equity float(9,2) DEFAULT NULL,
    Net_Revenues float(9,2) DEFAULT NULL,
    PRIMARY KEY (Ticker)
    )

    It prints basic information from the table:

    SELECT Ticker, Nombre, Sector, Subsector, `Payout/FCF(%)`, `EV/FCF`, `EV/EBITDA`, Puntuacion FROM stock.empresas;
    SELECT Ticker, Nombre, Sector, Subsector, `Payout/FCF(%)`, `EV/FCF`, `EV/EBITDA`, PER, `P/B`, `P/S` Puntuacion FROM stock.empresas;

    View that shows the main columns:

    create view empresas_vision as select Ticker, Nombre, `Payout/FCF(%)`, `EV/FCF`, `EV/EBITDA`, PER, `P/B`, `P/S`, Puntuacion
from empresas;
     */

    //This function add a stock into the BDD
    public static void addNewStock(Stocks e){
        try {
            Statement myStmt = myConn.createStatement();
            if (checkStock(e)) {
                System.out.println("Esta empresa ya se ha añadido a la base de datos\n");
            } else {
                int rowsAffected = myStmt.executeUpdate("insert into empresas " +
                        "(Ticker, Nombre, Sector, Subsector, `Payout/FCF(%)`, `EV/FCF`, `EV/EBITDA`, `PER`, `P/B`, `P/S`, `Puntuacion`, `Numero_Acciones`, `Dinero_Efectivo`, `Deuda`, `Flujo_Caja_Libre`, `EBITDA`)" +
                        " values " + "('"+e.getTicker()+"'," +
                        "" + " '"+e.getNombre()+"'," +
                        "" + " '"+e.getSector()+"'," +
                        "" + " '"+e.getSubsector()+"'," +
                        "" + " '"+e.getPayout_fcf()+"'," +
                        "" + "" + " '"+e.getEv_fcf()+"'," +
                        "" + " '"+e.getEv_ebitda()+"'," +
                        "" + " '"+e.getPer()+"'," +
                        "" + " '"+e.getP_b()+"'," +
                        "" + "'"+e.getP_s()+"'," +
                        "" + " '"+e.getCalificacion()+"'," +
                        "" + " '"+e.getAcciones()+"',"+
                        "" + " '"+e.getCash()+"',"+
                        "" + " '"+e.getDeuda()+"',"+
                        "" + " '"+e.getFcf()+"',"+
                        "" + " '"+e.getEbitda()+"')");
                System.out.println("Añadida correctamente: "+rowsAffected);
            }
        }
        catch (Exception exc){
            exc.printStackTrace();
        }
    }

    //Auxiliary function for the function 'addNewStockaddNewStock' that checks if a stock already exists in the BDD
    public static boolean checkStock(Stocks e){
        boolean check=false;
        try {
            //PreparedStatement myStmt = myConn.prepareStatement("SELECT * FROM empresas WHERE `Ticker`=?");
            //myStmt.setString(1, e.getTicker());
            Statement myStmt = myConn.createStatement();
            ResultSet myRs = null;
            myRs = myStmt.executeQuery("SELECT * FROM empresas WHERE `Ticker`='"+e.getTicker()+"'");
            if(!myRs.next()){ //The stock doesn't exist in the BDD
                check=false;
            }else{
                check=true;
            }
        }
        catch (Exception exc){
            exc.printStackTrace();
        }
        return check;
    }

    //A function that deletes a stock from the BDD
    //You need to give it the ticker in order to find it in the BDD
    public static void deleteStock(String ticker){
        try{
            Statement myStmt = myConn.createStatement();
            int rowsAffected=myStmt.executeUpdate("DELETE FROM empresas WHERE Ticker='"+ticker+"'");
        }
        catch (Exception exc){
            exc.printStackTrace();
        }
    }

    //It shows the BDD in the console of IntelliJ
    public static void showBDD(){
        try{
            Statement myStmt = myConn.createStatement();
            ResultSet rowsAffected=myStmt.executeQuery("SELECT * FROM empresas");
            while(rowsAffected.next()){
                //System.out.println("Ticker Nombre Sector Subsector PayOut/FCF(%)  EV/FCF  EV/EBITDA  PER  P/B  P/S\n");
                System.out.println(rowsAffected.getString("Ticker")+
                                    " "+rowsAffected.getString("Nombre")+
                                    "  "+rowsAffected.getString("Sector")+
                                    "  "+rowsAffected.getString("Subsector")+
                                    "  "+rowsAffected.getFloat("PayOut/FCF(%)")+
                                    "  "+rowsAffected.getFloat("EV/FCF")+
                                    "  "+rowsAffected.getFloat("EV/EBITDA")+
                                    "  "+rowsAffected.getFloat("PER")+
                                    "  "+rowsAffected.getFloat("P/B")+
                                    "  "+rowsAffected.getFloat("P/S")+
                                    "  "+rowsAffected.getFloat("Puntuacion"));
                System.out.println("\n");
            }
        }
        catch (Exception exc){
            exc.printStackTrace();
        }
    }

    //It returns the number of stocks that we've got in our BDD
    public static int listStocks(){
        int num=0;
        try{
            Statement myStmt = myConn.createStatement();
            ResultSet rowsAffected=myStmt.executeQuery("SELECT * FROM empresas");
            while(rowsAffected.next()){
                num++;
            }
        }
        catch (Exception exc){
            exc.printStackTrace();
        }
        return num;
    }

    //It updates the data of our 'Set' in Java
    //The update is made with the BDD data
    public static void updateDataSet(HashSet<Stocks> lista){
        try{
            Statement myStmt = myConn.createStatement();
            lista.clear();
            ResultSet rowsAffected = myStmt.executeQuery("SELECT * FROM empresas");
            if(rowsAffected.next()) {
                String ticker = rowsAffected.getString("Ticker");
                String nombre = rowsAffected.getString("Nombre");
                String sector = rowsAffected.getString("Sector");
                String subsector = rowsAffected.getString("Subsector");
                float payout_fcf = rowsAffected.getFloat("PayOut/FCF(%)");
                float ev_fcf=rowsAffected.getFloat("EV/FCF");
                float ev_ebitda=rowsAffected.getFloat("EV/EBITDA");
                float p_b=rowsAffected.getFloat("PER");
                float p_s=rowsAffected.getFloat("P/B");
                float per=rowsAffected.getFloat("P/S");
                float puntuacion= rowsAffected.getFloat("Puntuacion");
                float acciones=rowsAffected.getFloat("Numero_Acciones");
                float cash=rowsAffected.getFloat("Dinero_Efectivo");
                float deuda=rowsAffected.getFloat("Deuda");
                float fcf=rowsAffected.getFloat("Flujo_Caja_Libre");
                float ebitda=rowsAffected.getFloat("EBITDA");
                float ben_neto=rowsAffected.getFloat("Beneficio_Neto");
                float equity=rowsAffected.getFloat("Equity");
                float net_revenues=rowsAffected.getFloat("Net_Revenues");

                Stocks e = new Stocks(ticker, nombre, sector, subsector, payout_fcf, ev_fcf, ev_ebitda, p_b, p_s, per, puntuacion, acciones, cash, deuda, fcf, ebitda, ben_neto, equity, net_revenues);
                lista.add(e);
            }
        }
        catch(Exception exc){
            exc.printStackTrace();
        }
    }

    //It updates a stock based in the price(that is always changing)
    public static void updateStocks(Stocks e) {
        try {
            Statement myStmt = myConn.createStatement();
            String sql = "UPDATE empresas SET `EV/FCF` = '" + e.getEv_fcf() + "' WHERE `Ticker`='" + e.getTicker() + "'";
            String sql2 = "UPDATE empresas SET `EV/EBITDA` = '" + e.getEv_ebitda() + "' WHERE `Ticker`='" + e.getTicker() + "'";
            String sql3 = "UPDATE empresas SET `Puntuacion` = '" + e.getCalificacion() + "' WHERE `Ticker`='" + e.getTicker() + "'";
            int rowsAffected = myStmt.executeUpdate(sql);
            int rowsAffected2 = myStmt.executeUpdate(sql2);
            int rowsAffected3 = myStmt.executeUpdate(sql3);
        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }
}
