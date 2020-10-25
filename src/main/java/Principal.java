import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;
import java.util.HashSet;


public class Principal {
    static HashSet<Empresa>lista=new HashSet<Empresa>();


    public static void main(String[] args){
            int resul = 0;
            BDD.inicializarConexion();
            BDD.actualizarDatosSet(lista);
            do {
                resul = mostrarMenuPrincipal();
                menuPrincipal(resul);
            } while (resul != 5);
    }

    public static void menuPrincipal(int opcion){
        switch (opcion){
            case 1:
                //añadir empresa
                int resul=mostrarMenuAvanzado();
                menu_avanzado(resul);
                break;
            case 2:
                //eliminar empresa
                String ticker=pedirTickerEmpresa();
                borrarEmpresa(ticker);
                BDD.eliminarEmpresa(ticker);
                break;
            case 3:
                //nºtotal empresas
                //System.out.println(lista.size());
                System.out.println(BDD.listaEmpresas());
                break;
            case 4:
                //ver empresas
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
                System.out.println("Tiene que introducir uno de los números (1 al 5)");
        }
    }

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

    public static void menu_avanzado(int opcion){
        switch (opcion){
            case 1:
                //Análisis básico

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

    public static int mostrarMenuAvanzado(){
        System.out.println("Seleccione una de las 2 opciones: ");
        System.out.println("Opción 1. Si lo que desea es obtener un análisis basado en EV/FCF, EV/EBITDA y PayOut/FCF");
        System.out.println("Opción 2. Si además quieres tener un análisis más completo añadiendo P/B, P/S y PER");
        Scanner entrada=new Scanner(System.in);
        int opcion=entrada.nextInt();
        entrada.nextLine();
        return opcion;
    }

    public static String pedirTickerEmpresa(){
        System.out.println("Dime el ticker de la empresa: ");
        Scanner entrada = new Scanner(System.in);
        String ticker= entrada.next();
        entrada.nextLine();
        return ticker;
    }

    public static void borrarEmpresa(String ticker){
        Empresa e = new Empresa(ticker);
        lista.remove(e);
    }


    /*
    public Empresa obtenerEmpresa(String ticker){
        if(lista.contains(new Empresa(ticker))){

        }
    }

     */

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

