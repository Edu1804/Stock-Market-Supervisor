import java.util.Scanner;

public class Pruebas {




    public static void main(String[] args){

        /*
        private int valor2;



        public boolean comprobarValorPositivo2(float valor2){
            //We assume that the value is positive at the beginning
            boolean positive2=true;
            if(valor2<0){
                positive2=false;
            }
            return positive2;
        }

        public static float numeroAcciones2(){
            Scanner entrada = new Scanner(System.in);
            System.out.println("Introduzca el numero de acciones diluidas de la empresa: ");
            float acciones2=0;
            acciones2=entrada.nextFloat();
            entrada.nextLine();
            if(!comprobarValorPositivo2(acciones2)){
                System.out.println("Error, el número de acciones debe de ser positivo");
                numeroAcciones2();
            }
            return acciones2;
        }
    }

}
