import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;
import java.util.Scanner;

public class Empresa {

    private String nombre;
    private String sector;
    private String subsector;
    private String ticker;

    private float payout_fcf;
    private float ev_fcf;
    private float ev_ebitda;

    private float p_b;
    private float p_s;
    private float per;

    private float calificacion;

    private BigDecimal cotizacion;

    private float acciones;
    private float cash;
    private float deuda;
    private float fcf;
    private float ebitda;

    static String[] sectores=new String[]{"Defensivo","Sensitivo","Ciclico"};
    static String[] subsectores1=new String[]{"Consumo Defensivo","Salud","Utilities"};
    static String[] subsectores2=new String[]{"Industrial","Servicios de Telecomunicacion","Tecnologia","Energia"};
    static String[] subsectores3=new String[]{"Consumo Ciclico","Servicios Financieros","Real Estate","Materiales Basicos"};

    public Empresa(){
        this.nombre="";
        this.sector="";
        this.subsector="";
        this.ticker="";
    }

    public Empresa(String ticker){
        this();
        this.ticker=ticker;
    }

    public Empresa(String ticker, String nombre, String sector, String subsector, float payout_fcf, float ev_fcf, float ev_ebitda, float p_b, float p_s, float per, float puntuacion, float acciones, float cash, float deuda, float fcf, float ebitda) {
        this.nombre = nombre;
        this.sector = sector;
        this.subsector = subsector;
        this.ticker = ticker;
        this.payout_fcf = payout_fcf;
        this.ev_fcf = ev_fcf;
        this.ev_ebitda = ev_ebitda;
        this.p_b = p_b;
        this.p_s = p_s;
        this.per = per;
        this.calificacion = calificacion;
        this.cash=cash;
        this.deuda= deuda;
        this.acciones=acciones;
        this.fcf=fcf;
        this.ebitda=ebitda;
    }

    public void introducirParametros(){
        Scanner entrada = new Scanner(System.in);
        System.out.println("Digite las caracteristicas de la empresa:");
        System.out.println("Introduzca el nombre de la empresa: ");
        this.nombre = entrada.nextLine();
        System.out.println("Elige la opción del sector al que pertenece: ");
        int indice=elegir(sectores);
        this.sector=sectores[indice];
        System.out.println("Elige la opción del subsector al que pertenece: ");
        this.subsector=elegirSubSector(indice);
        System.out.println("Introduzca el ticker de la empresa: ");
        this.ticker=entrada.nextLine();
        this.cotizacion=Finance.encontrarPrecioApi(ticker);
        System.out.println("Introduzca el numero de acciones diluidas de la empresa: ");
        this.acciones=entrada.nextFloat();
        entrada.nextLine();
        System.out.println("Indique de cuanto es el dividendo cada vez que te pagan: ");
        float dividendo=entrada.nextFloat();
        entrada.nextLine();
        dividendo=calculo_dividendo(tipoDividendo(), dividendo);
        float payout_total=calculo_payout(acciones, dividendo);
        System.out.println("Introduzca el FCF de la empresa en el año fiscal actual: ");
        this.fcf = entrada.nextFloat();
        this.payout_fcf=Payout_FCF(payout_total, fcf);
        entrada.nextLine();
        System.out.println("Introduzca la deuda a largo plazo: ");
        float deuda_largo=entrada.nextFloat();
        entrada.nextLine();
        System.out.println("Introduzca la deuda a corto plazo: ");
        float deuda_corto=entrada.nextFloat();
        entrada.nextLine();
        System.out.println("Introduzca el dinero en efectivo (Cash & cash equivalents): ");
        this.cash=entrada.nextFloat();
        entrada.nextLine();
        float capital=calculo_capitalizacion(cotizacion, acciones);
        this.deuda=deuda_neta(deuda_largo, deuda_corto);
        float enterprise=enterprise_value(capital, cash, deuda);
        this.ev_fcf=EV_FCF(enterprise, fcf);
        System.out.println("Introduzca el beneficio neto: ");
        float ebit=entrada.nextFloat();
        entrada.nextLine();
        System.out.println("Introduzca la depreciación: ");
        float depreciacion=entrada.nextFloat();
        entrada.nextLine();
        System.out.println("Introduzca la amortización: ");
        float amortizacion=entrada.nextFloat();
        entrada.nextLine();
        this.ebitda=EBITDA(ebit, depreciacion, amortizacion);
        this.ev_ebitda=EV_EBITDA(enterprise, ebitda);
    }

    public void actualizarDatos(){

        this.cotizacion=Finance.encontrarPrecioApi(ticker);

        float capital=calculo_capitalizacion(cotizacion, acciones);
        float enterprise=enterprise_value(capital, cash, deuda);
        this.ev_fcf=EV_FCF(enterprise, fcf);
        this.ev_ebitda=EV_EBITDA(enterprise, ebitda);
    }

    public void introducirParametrosAvanzados(){
        Scanner entrada = new Scanner(System.in);
        System.out.println("Introduzca el beneficio neto(net income): ");
        float ben_neto=entrada.nextFloat();
        entrada.nextLine();
        this.per=PER(cotizacion, acciones, ben_neto);
        System.out.println("Introduzca el patrimono(equity): ");
        float equity=entrada.nextFloat();
        entrada.nextLine();
        this.p_b=P_B(cotizacion, acciones, equity);
        System.out.println("Introduzca el total de ventas(net revenue): ");
        float net_revenues=entrada.nextFloat();
        entrada.nextLine();
        this.p_s=P_S(cotizacion, acciones, net_revenues);
    }



    private static void mensajeDividendo(){
        System.out.println("¿Es es dividendo trimestral, semestral o anual?");
        System.out.println("Indique el tipo de dividendo que es:");
        System.out.println("1.Dividendo trimestral");
        System.out.println("2.Dividendo semestral");
        System.out.println("3.Dividendo anual");
    }

    private static int tipoDividendo(){
        mensajeDividendo();
        Scanner entrada = new Scanner(System.in);
        int opcion = entrada.nextInt();
        entrada.nextLine();
        return opcion;
    }

    public float getAcciones() {
        return acciones;
    }

    public float getCash() {
        return cash;
    }

    public float getDeuda() {
        return deuda;
    }

    public float getFcf() {
        return fcf;
    }

    public float getEbitda() {
        return ebitda;
    }

    private static float calculo_dividendo(int opcion, float dividendo){
        if(opcion==1){
            dividendo*=4;
        }
        else if(opcion==2){
            dividendo*=2;
        }
        return dividendo;
    }

    private static float calculo_payout(float num_acciones, float dividendo){
        return num_acciones*dividendo;
    }

    private static float calculo_capitalizacion(BigDecimal cotizacion, float acciones){
        //BigDecimal cotizacion = new BigDecimal(2.36359);
        float rounded = cotizacion.setScale(2, RoundingMode.DOWN).floatValue();
        return rounded*acciones;
    }

    private static float deuda_neta(float deudalargo, float deudacorto){
        return deudacorto + deudalargo;
    }

    private static float enterprise_value(float calculo_capitalizacion, float cash, float deuda_neta){
        return calculo_capitalizacion+deuda_neta-cash;
    }

    private static float Payout_FCF(float payout, float fcf){
        return (payout/fcf)*100;
    }

    public String toString(){
        return "Empresa: "+nombre+
                "\n     Sector: "+sector+
                "\n     Subsector: "+subsector+
                "\n     Ticker: "+ticker+
                "\n     PayOut/FCF: "+payout_fcf+"%"+
                "\n     EV/FCF: "+ev_fcf+
                "\n     EV/EBITDA: "+ev_ebitda+
                "\n     PER: "+per+
                "\n     P/B: "+p_b+
                "\n     P/S: "+p_s+
                "\n     Puntuación: "+calificacion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Empresa empresa = (Empresa) o;
        return ticker.equals(empresa.ticker);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ticker);
    }

    private static float EV_FCF(float enterprise_value, float fcf){
        return enterprise_value/fcf;
    }

    private static float EV_EBITDA(float ev, float ebitda){
        return ev/ebitda;
    }

    private static float EBITDA(float income, float deprecia, float amort){
        return income+deprecia+amort;
    }

    private static float PER(BigDecimal cotizacion, float acciones, float ben_neto){
        float rounded = cotizacion.setScale(2, RoundingMode.DOWN).floatValue();
        return rounded/(ben_neto/acciones);
    }

    private static float P_B(BigDecimal cotizacion, float acciones, float equity){
        float rounded = cotizacion.setScale(2, RoundingMode.DOWN).floatValue();
        return rounded/(equity/acciones);
    }

    private static float P_S(BigDecimal cotizacion, float acciones, float net_revenues){
        float rounded = cotizacion.setScale(2, RoundingMode.DOWN).floatValue();
        return rounded/(net_revenues/acciones);
    }

    public String getNombre() {
        return nombre;
    }

    public String getSector() {
        return sector;
    }

    public String getSubsector() {
        return subsector;
    }

    public float getCalificacion() {
        return calificacion;
    }

    public String getTicker() {
        return ticker;
    }

    public float getPayout_fcf() {
        return payout_fcf;
    }

    public float getEv_fcf() {
        return ev_fcf;
    }

    public float getEv_ebitda() {
        return ev_ebitda;
    }

    public float getP_b() {
        return p_b;
    }

    public float getP_s() {
        return p_s;
    }

    public float getPer() {
        return per;
    }

    public void aniadirPuntuacion(){
        this.calificacion= (int) (sumaEV_FCF(this.ev_fcf)*1.66+sumaEV_EBITDA(this.ev_ebitda)*1.66);
    }

    public void aniadirPuntuacionAvanzada(){
        this.calificacion= (int) (sumaEV_FCF(this.ev_fcf)+sumaEV_EBITDA(this.ev_ebitda)+sumaPER(this.per)+sumaPB(this.p_b)+sumaPS(this.p_s));
    }

    public static float sumaEV_FCF(float ev_fcf){
        float nota=0;
        int i=5;
        int barometro=24;
        do{
            nota += 0.5;
            barometro= barometro-2;
            i--;
        }while(i>0 && ev_fcf<barometro);
        return nota;
    }

    public static float sumaEV_EBITDA(float ev_ebitda){
        float nota=0;
        int i=5;
        int barometro=18;
        do{
            nota+=0.5;
            barometro-=2;
            i--;
        }while(i>0 && ev_ebitda<barometro);
        return nota;
    }

    public static float sumaPER(float per){
        float nota=0;
        int i=3;
        int barometro=20;
        do{
            nota+=0.25;
            barometro-=3;
            i--;
        }while(i>0 && per<barometro);
        return nota;
    }

    public static float sumaPB(float p_b){
        float nota=0;
        int i=5;
        float barometro= (float) 5.9;
        do{
            nota+=0.25;
            barometro-=0.5;
            i--;
        }while(i>0 && p_b<barometro);
        return nota;
    }

    public static float sumaPS(float p_s){
        float nota=0;
        int i=3;
        float barometro= (float) 3.7;
        do{
            nota+=0.25;
            barometro-=0.3;
            i--;
        }while(i>0 && p_s<barometro);
        return (float) (nota*1.5);
    }

    private int elegir(String[] elementos){
        boolean comprobar=false;
        Scanner entrada = new Scanner(System.in);
        int num=0;
        while(!comprobar) {
            for (int i = 0; i < elementos.length; i++) {
                System.out.println(i + 1 + ". " + elementos[i]);
            }
            num = entrada.nextInt();
            entrada.nextLine();
            comprobar=num<= elementos.length&&num>0;
            if(!comprobar){
                System.out.println("Tienes que introducir un índice válido: ");
            }
        }
        return num-1;
    }

    private String elegirSubSector(int indice){
        if(indice==0){
            return subsectores1[elegir(subsectores1)];
        }
        else if(indice==1){
            return subsectores2[elegir(subsectores2)];
        }
        else if(indice==2){
            return subsectores3[elegir(subsectores3)];
        }
        return "";
    }


}
