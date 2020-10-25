import java.sql.*;
import java.util.HashSet;

public class BDD {
    private static Connection myConn;
    public static void inicializarConexion(){

        String dbUrl = "jdbc:mysql://localhost:3306/stock?useSSL=false";
        String user = "stockuser";
        String pass = "stockuser";

        try{
            //Comunicación con la base de datos
            myConn=DriverManager.getConnection( dbUrl, user, pass);
            System.out.println("Database connection successful!\n");
        }
        catch (Exception exc){
            exc.printStackTrace();
        }
    }

    public static void aniadirNuevaEmpresa(Empresa e){
        try {

            Statement myStmt = myConn.createStatement();
            if (comprobarEmpresa(e)) {
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
            }
        }
        catch (Exception exc){
            exc.printStackTrace();
        }
    }

    public static boolean comprobarEmpresa(Empresa e){
        boolean encontrado=false;
        try {
            //PreparedStatement myStmt = myConn.prepareStatement("SELECT * FROM empresas WHERE `Ticker`=?");
            //myStmt.setString(1, e.getTicker());
            Statement myStmt = myConn.createStatement();
            ResultSet myRs = null;
            myRs = myStmt.executeQuery("SELECT * FROM empresas WHERE `Ticker`='"+e.getTicker()+"'");
            if(!myRs.next()){ //No existe esa empresa en la BBD
                encontrado=false;
            }else{
                encontrado=true;
            }
        }
        catch (Exception exc){
            exc.printStackTrace();
        }
        return encontrado;
    }

    public static void eliminarEmpresa(String ticker){
        try{
            Statement myStmt = myConn.createStatement();
            int rowsAffected=myStmt.executeUpdate("DELETE FROM empresas WHERE Ticker='"+ticker+"'");
        }
        catch (Exception exc){
            exc.printStackTrace();
        }
    }

    public static void mostrarBDD(){
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

    public static int listaEmpresas(){
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

    public static void actualizarDatosSet(HashSet<Empresa> lista){
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

                Empresa e = new Empresa(ticker, nombre, sector, subsector, payout_fcf, ev_fcf, ev_ebitda, p_b, p_s, per, puntuacion, acciones, cash, deuda, fcf, ebitda);
                lista.add(e);
            }
        }
        catch(Exception exc){
            exc.printStackTrace();
        }
    }

    public static void actualizarEmpresa(Empresa e){
        try{
            Statement myStmt = myConn.createStatement();
            String sql = "UPDATE empresas SET `EV/FCF` = '"+e.getEv_fcf()+"' WHERE `Ticker`='"+e.getTicker()+"'";
            String sql2 = "UPDATE empresas SET `EV/EBITDA` = '"+e.getEv_ebitda()+"' WHERE `Ticker`='"+e.getTicker()+"'";
            String sql3 = "UPDATE empresas SET `Puntuacion` = '"+e.getCalificacion()+"' WHERE `Ticker`='"+e.getTicker()+"'";
            int rowsAffected=myStmt.executeUpdate(sql);
            int rowsAffected2=myStmt.executeUpdate(sql2);
            int rowsAffected3=myStmt.executeUpdate(sql3);
        }catch(Exception exc){
            exc.printStackTrace();
        }
    }

    /*
    Codigo BDD para MySQL
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
    Puntuacion int(3) DEFAULT NULL,
    Numero_Acciones float(9,2) DEFAULT NULL,
    Dinero_Efectivo int(9) DEFAULT NULL,
    Deuda int(9) DEFAULT NULL,
    Flujo_Caja_Libre int(9) DEFAULT NULL,
    EBITDA int(9) DEFAULT NULL,
    PRIMARY KEY (Ticker)
    )


    SELECT Ticker, Nombre, Sector, Subsector, `Payout/FCF(%)`, `EV/FCF`, `EV/EBITDA`, Puntuacion FROM stock.empresas;
SELECT Ticker, Nombre, Sector, Subsector, `Payout/FCF(%)`, `EV/FCF`, `EV/EBITDA`, PER, `P/B`, `P/S` Puntuacion FROM stock.empresas;
     */






}
