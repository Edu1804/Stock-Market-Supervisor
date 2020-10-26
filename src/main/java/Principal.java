import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;
import java.util.HashSet;


public class Principal {

    //HashSt necessary to keep the data from the BDD
    static HashSet<Empresa>lista=new HashSet<Empresa>();

    //Main function that has got the menus
    public static void main(String[] args){
            int resul = 0;
            BDD.inicializarConexion();
            BDD.actualizarDatosSet(lista);
            do {
                resul = mostrarMenuPrincipal();
                menuPrincipal(resul);
            } while (resul != 5);
    }

    //Main menu display that shows all the possible options
    public static int mostrarMenuPrincipal(){
        System.out.println("Seleccione una opción del menú: ");
        System.out.println("1.Añadir empresa");
        System.out.println("2.Eliminar empresa");
        System.out.println("3.Número total de empresas guardadas");
        System.out.println("4.Ver empresas guardadas");
        System.out.println("5.Actualizar Datos");
        System.out.println("6.Salir");
        Scanner entrada=new Scanner(System.in);
        int opcion=entrada.nextInt();
        entrada.nextLine();
        return opcion;
    }

    //Main menu to choose the main options
    public static void menuPrincipal(int opcion){
        System.out.println("Seleccione una opción: ");
        switch (opcion){
            case 1:
                //Add a stock to the BDD
                int resul=mostrarMenuAvanzado();
                menu_avanzado(resul);
                break;
            case 2:
                //Delete a stock from the BDD looking for the Ticker
                String ticker=pedirTickerEmpresa();
                borrarEmpresa(ticker);
                BDD.eliminarEmpresa(ticker);
                break;
            case 3:
                //Number of stocks that we've got in the BDD
                //System.out.println(lista.size());
                System.out.println("Número de empresas guardadas: ");
                System.out.println(BDD.listaEmpresas());
                break;
            case 4:
                //Watch stock in the HashSet and the BDD
                for (Empresa e:lista){
                    System.out.println(e.toString());
                }
                BDD.mostrarBDD();
                break;
            case 5:
                //Actualizar los datos de la base de datos respecto a los precios actuales
                actualizarDatosBDD();
                break;
            case 6:
                //salir, no se hace nada
                break;
            default:
                System.out.println("Tiene que introducir uno de los números (1 al 6)");
        }
    }

    //Advanced menu display that shows the options to add a stock
    public static int mostrarMenuAvanzado(){
        System.out.println("Seleccione una de las 2 opciones: ");
        System.out.println("Opción 1. Si lo que desea es obtener un análisis basado en EV/FCF, EV/EBITDA y PayOut/FCF");
        System.out.println("Opción 2. Si además quieres tener un análisis más completo añadiendo P/B, P/S y PER");
        Scanner entrada=new Scanner(System.in);
        int opcion=entrada.nextInt();
        entrada.nextLine();
        return opcion;
    }

    //Advanced menu to choose what type of analysis will be done to add a stock
    public static void menu_avanzado(int opcion){
        switch (opcion){
            case 1:
                //Basic analysis
                Empresa e = new Empresa();
                e.introducirParametros();
                e.aniadirPuntuacion();
                lista.add(e);
                BDD.aniadirNuevaEmpresa(e);
                break;
            case 2:
                //Análisis avanzado
                Empresa b = new Empresa();
                b.introducirParametros();
                b.introducirParametrosAvanzados();
                b.aniadirPuntuacionAvanzada();
                lista.add(b);
                BDD.aniadirNuevaEmpresa(b);
                break;
            default:
                System.out.println("Tienes que introducir uno de los números(1 o 2)");
        }
    }

    //It return the Ticker of a stock
    public static String pedirTickerEmpresa(){
        System.out.println("Dime el ticker de la empresa: ");
        Scanner entrada = new Scanner(System.in);
        String ticker= entrada.next();
        entrada.nextLine();
        return ticker;
    }

    //It deletes a stock from the HashSet
    public static void borrarEmpresa(String ticker){
        Empresa e = new Empresa(ticker);
        lista.remove(e);
    }

    //It updates the data from the BDD to the HashSet
    public static void actualizarDatosBDD(){
        try{
            for (Empresa e:lista) {
                e.actualizarDatos();
                BDD.actualizarEmpresa(e);
            }
        }
        catch(Exception exc){
            exc.printStackTrace();
        }
    }

}

