import java.util.Scanner;
import java.util.HashSet;


public class Main {

    //HashSt necessary to keep the data from the BDD
    static HashSet<Stocks> list =new HashSet<Stocks>();

    //Main function that has got the menus
    public static void main(String[] args){
            int resul = 0;
            BDD.initializeConnection();
            BDD.updateDataSet(list);
            do {
                resul = mainMenuShow();
                if(resul != -1 && resul !=6){
                    mainMenu(resul);
                }
            } while (resul != -1 && resul != 6);
    }

    //Main menu display that shows all the possible options
    public static int mainMenuShow(){
        System.out.println("Choose an option from the menu: ");
        System.out.println("1.Add a stock");
        System.out.println("2.Delete a stock");
        System.out.println("3.Number of stocks");
        System.out.println("4.Watch the stocks");
        System.out.println("5.Update Data");
        System.out.println("6.Exit");
        Scanner entrada=new Scanner(System.in);
        int option=entrada.nextInt();
        entrada.nextLine();
        /*if(opcion==6){
            opcion=-1;
        }
         */
        return option;
    }

    //Main menu to choose the main options
    public static void mainMenu(int opcion){
        System.out.println("Seleccione una opción: ");
        switch (opcion){
            case 1:
                //Add a stock to the BDD
                int resul= advancedMenuShow();
                advancedMenu(resul);
                break;
            case 2:
                //Delete a stock from the BDD looking for the Ticker
                String ticker= askStockTicket();
                deleteStock(ticker);
                BDD.deleteStock(ticker);
                break;
            case 3:
                //Number of stocks that we've got in the BDD
                //System.out.println(lista.size());
                System.out.println("Number of stocks stored: ");
                System.out.println(BDD.listStocks());
                break;
            case 4:
                //Watch stock in the HashSet and the BDD
                for (Stocks e: list){
                    System.out.println(e.toString());
                }
                BDD.showBDD();
                break;
            case 5:
                //Updates the BDD based in the actual price
                updateBDD();
                break;
            case 6:
                //salir, no se hace nada
                break;
            default:
                System.out.println("Have to introduce a number between 1 and 6");
        }
    }

    //Advanced menu display that shows the options to add a stock
    public static int advancedMenuShow(){
        System.out.println("Select one of the 2 possible options: ");
        System.out.println("Option 1. If you wish to obtain an analysis based in EV/FCF, EV/EBITDA and PayOut/FCF");
        System.out.println("Option 2. If you also want to include in the analysis P/B, P/S and PER");
        Scanner entrada=new Scanner(System.in);
        int opcion=entrada.nextInt();
        entrada.nextLine();
        return opcion;
    }

    //Advanced menu to choose what type of analysis will be done to add a stock
    public static void advancedMenu(int opcion){
        switch (opcion){
            case 1:
                //Basic analysis
                Stocks e = new Stocks();
                e.introduceParameters();
                e.addPunctuation();
                list.add(e);
                BDD.addNewStock(e);
                break;
            case 2:
                //Advanced analysis
                Stocks b = new Stocks();
                b.introduceParameters();
                b.introduceParametersAdvanced();
                b.addPunctuationAdvanced();
                list.add(b);
                BDD.addNewStock(b);
                break;
            default:
                System.out.println("You have to choose an option between 1 and 2");
        }
    }

    //It return the Ticker of a stock
    public static String askStockTicket(){
        System.out.println("Write the ticker of the stock: ");
        Scanner entrada = new Scanner(System.in);
        String ticker= entrada.next();
        entrada.nextLine();
        return ticker;
    }

    //It deletes a stock from the HashSet
    public static void deleteStock(String ticker){
        Stocks e = new Stocks(ticker);
        list.remove(e);
    }

    //It updates the data from the BDD to the HashSet
    public static void updateBDD(){
        try{
            for (Stocks e: list) {
                e.updateData();
                BDD.updateStocks(e);
            }
        }
        catch(Exception exc){
            exc.printStackTrace();
        }
    }

}

