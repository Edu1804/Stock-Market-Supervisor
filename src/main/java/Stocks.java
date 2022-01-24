import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;
import java.util.Scanner;

public class Stocks {

    //Main attributes that are not expected to change during the execution of the code
    private String nombre;
    private String sector;
    private String subsector;
    private String ticker;

    //Attributes for the basic analysis
    private float payout_fcf;
    private float ev_fcf;
    private float ev_ebitda;

    //Atrributes for the advanced analysis
    private float p_b;
    private float p_s;
    private float per;

    //Atrribute for the calification based in others (atrributes)
    private float calificacion;

    //Atrribute necessary to storage the price (it changes during the code execution many times)
    private BigDecimal cotizacion;

    //Other atrributes that have been created in order to support the others
    private float acciones;
    private float cash;
    private float deuda;
    private float fcf;
    private float ebitda;

    //Other atrributes for the advanced analysis that have been created in order to support the others
    private float ben_neto;
    private float equity;
    private float net_revenues;

    //Arrays for the atrributes 'Sector' & 'Subsector'
    static String[] sectores=new String[]{"Defensivo","Sensitivo","Ciclico"};
    static String[] subsectores1=new String[]{"Consumo Defensivo","Salud","Utilities"};
    static String[] subsectores2=new String[]{"Industrial","Servicios de Telecomunicacion","Tecnologia","Energia"};
    static String[] subsectores3=new String[]{"Consumo Ciclico","Servicios Financieros","Real Estate","Materiales Basicos"};

    //Empty basic constructor
    public Stocks(){
        this.nombre="";
        this.sector="";
        this.subsector="";
        this.ticker="";
    }

    //constructor for the Ticker
    public Stocks(String ticker){
        this();
        this.ticker=ticker;
    }

    //Full constructor
    public Stocks(String ticker, String nombre, String sector, String subsector, float payout_fcf, float ev_fcf, float ev_ebitda, float p_b, float p_s, float per, float puntuacion, float acciones, float cash, float deuda, float fcf, float ebitda, float ben_neto, float equity, float net_revenues) {
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
        this.ben_neto=ben_neto;
        this.equity=equity;
        this.net_revenues=net_revenues;
    }

    //Equals method
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Stocks stocks = (Stocks) o;
        return ticker.equals(stocks.ticker);
    }

    //Many getters and setters that are used in many classes
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

    public float getAcciones() {
        return acciones;
    }

    public float getCash() {
        return cash;
    }

    public float getDeuda() {
        return deuda;
    }

    public float getFcf() { return fcf; }

    public float getEbitda() {
        return ebitda;
    }

    //String toString method
    @Override
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

    //Basic method of hashCode
    @Override
    public int hashCode() {
        return Objects.hash(ticker);
    }

    //Main function that introduce all the parameters of a stock
    public void introduceParameters(){
        Scanner entrada = new Scanner(System.in);
        System.out.println("Digite las caracteristicas de la empresa:");
        System.out.println("Introduzca el nombre de la empresa: ");
        this.nombre = entrada.nextLine();
        System.out.println("Elige la opción del sector al que pertenece: ");
        int indice= choose(sectores);
        this.sector=sectores[indice];
        System.out.println("Elige la opción del subsector al que pertenece: ");
        this.subsector= chooseSubSector(indice);
        System.out.println("Introduzca el ticker de la empresa: ");
        this.ticker=entrada.nextLine();
        this.cotizacion=Finance.findStockValues(ticker);
        this.acciones= numberStocks();
        float dividendo= dividendValue();
        dividendo= calculateDividend(typeDividendMenu(), dividendo);
        float payout_total= payout(acciones, dividendo);
        this.fcf = valueFCF();
        this.payout_fcf=Payout_FCF(payout_total, fcf);
        entrada.nextLine();
        float deuda_largo= valueLongTermDebt();
        float deuda_corto= valueShortTermDebt();
        this.cash= valueCash();
        float capital= capitalization(cotizacion, acciones);
        this.deuda= netDebt(deuda_largo, deuda_corto);
        float enterprise=enterprise_value(capital, cash, deuda);
        this.ev_fcf=EV_FCF(enterprise, fcf);
        float ebit= valueEBIT();
        float depreciacion= valueDepreciation();
        float amortizacion= valueAmortization();
        this.ebitda=EBITDA(ebit, depreciacion, amortizacion);
        this.ev_ebitda=EV_EBITDA(enterprise, ebitda);
    }

    //Function necessary if you are doing an advanced analysis
    public void introduceParametersAdvanced(){
        this.ben_neto= valueEBIT();
        this.per=PER(cotizacion, acciones, ben_neto);
        this.equity= valueEquity();
        this.p_b=P_B(cotizacion, acciones, equity);
        this.net_revenues= valueRevenues();
        this.p_s=P_S(cotizacion, acciones, net_revenues);
    }

    //This functions are necessaries for the 'Sector' && 'Subsector'
    private int choose(String[] elementos){
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

    private String chooseSubSector(int indice){
        if(indice==0){
            return subsectores1[choose(subsectores1)];
        }
        else if(indice==1){
            return subsectores2[choose(subsectores2)];
        }
        else if(indice==2){
            return subsectores3[choose(subsectores3)];
        }
        return "";
    }

    //It calculates the amount of dividend per year based on the dividend period
    private static float calculateDividend(int opcion, float dividendo){
        if(opcion==1){
            dividendo*=4;
        }
        else if(opcion==2){
            dividendo*=2;
        }
        return dividendo;
    }

    //Auxiliary menu for the dividend
    private static void messageDividendMenu(){
        System.out.println("¿Es el dividendo trimestral, semestral o anual?");
        System.out.println("Indique el tipo de dividendo que es:");
        System.out.println("1.Dividendo trimestral");
        System.out.println("2.Dividendo semestral");
        System.out.println("3.Dividendo anual");
    }

    //Auxiliary function for 'calculateDividend'
    private static int typeDividendMenu(){
        messageDividendMenu();
        Scanner entrada = new Scanner(System.in);
        int opcion = entrada.nextInt();
        entrada.nextLine();
        return opcion;
    }

    //It calculates the PayOut of a stock
    private static float payout(float num_acciones, float dividendo){
        return num_acciones*dividendo;
    }

    //It calculates the PayOut/FCF of a stock
    private static float Payout_FCF(float payout, float fcf){
        return (payout/fcf)*100;
    }

    //It calculates the capitalization of a stock
    private static float capitalization(BigDecimal cotizacion, float acciones){
        //BigDecimal cotizacion = new BigDecimal(2.36359);
        float rounded = cotizacion.setScale(2, RoundingMode.DOWN).floatValue();
        return rounded*acciones;
    }

    //It calculates the net debt of a stock
    private static float netDebt(float deudalargo, float deudacorto){
        return deudacorto + deudalargo;
    }

    //It calculates the Enterprise Value of a stock
    private static float enterprise_value(float calculo_capitalizacion, float cash, float deuda_neta){
        return calculo_capitalizacion+deuda_neta-cash;
    }

    //It calculates the EV/FCF of a stock
    private static float EV_FCF(float enterprise_value, float fcf){
        return enterprise_value/fcf;
    }

    //It calculates the EBITDA of a stock
    private static float EBITDA(float income, float deprecia, float amort){
        return income+deprecia+amort;
    }

    //It calculates the EV/EBITDA of a stock
    private static float EV_EBITDA(float ev, float ebitda){
        return ev/ebitda;
    }

    //It calculates the PER of a stock
    private static float PER(BigDecimal cotizacion, float acciones, float ben_neto){
        float rounded = cotizacion.setScale(2, RoundingMode.DOWN).floatValue();
        return rounded/(ben_neto/acciones);
    }

    //It calculates the P/B ratio of a stock
    private static float P_B(BigDecimal cotizacion, float acciones, float equity){
        float rounded = cotizacion.setScale(2, RoundingMode.DOWN).floatValue();
        return rounded/(equity/acciones);
    }

    //It calculates the P/S ratio of a stock
    private static float P_S(BigDecimal cotizacion, float acciones, float net_revenues){
        float rounded = cotizacion.setScale(2, RoundingMode.DOWN).floatValue();
        return rounded/(net_revenues/acciones);
    }

    //It calculates the punctuation of a basic analysis
    public void addPunctuation(){
        this.calificacion= (float) (addingEV_FCF(this.ev_fcf)*1.66+ addingEV_EBITDA(this.ev_ebitda)*1.66);
    }

    //It calculates the punctuation of an advanced analysis
    public void addPunctuationAdvanced(){
        this.calificacion= (float) (addingEV_FCF(this.ev_fcf)+ addingEV_EBITDA(this.ev_ebitda)+ addingPER(this.per)+ addingPB(this.p_b)+ addingPS(this.p_s));
    }

    //Auxiliary method for the punctuation
    public static float addingEV_FCF(float ev_fcf){
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

    //Auxiliary method for the punctuation
    public static float addingEV_EBITDA(float ev_ebitda){
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

    //Auxiliary method for the punctuation
    public static float addingPER(float per){
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

    //Auxiliary method for the punctuation
    public static float addingPB(float p_b){
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

    //Auxiliary method for the punctuation
    public static float addingPS(float p_s){
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

    //It updates all the data based in the Stock Price
    public void updateData(){
        this.cotizacion=Finance.findStockValues(ticker);
        float capital= capitalization(cotizacion, acciones);
        float enterprise=enterprise_value(capital, cash, deuda);
        this.ev_fcf=EV_FCF(enterprise, fcf);
        this.ev_ebitda=EV_EBITDA(enterprise, ebitda);
        if(this.per==0){
            this.addPunctuation();
        }else{
            this.addPunctuationAdvanced();
            this.per=PER(cotizacion, acciones, ben_neto);
            this.p_b=P_B(cotizacion, acciones, equity);
            this.p_s=P_S(cotizacion, acciones, net_revenues);

        }
    }

    //It checks that a float value is correct
    public boolean checkPositiveValues(float valor){
        //We assume that the value is positive at the beginning
        boolean positive=true;
        if(valor<0){
            positive=false;
        }
        return positive;
    }

    //It asks the user for the number of shareholders of a company
    public float numberStocks(){
        Scanner entrada = new Scanner(System.in);
        System.out.println("Introduzca el numero de acciones diluidas de la empresa: ");
        float acciones=0;
        acciones=entrada.nextFloat();
        entrada.nextLine();
        if(!checkPositiveValues(acciones)){
            System.out.println("Error, el número de acciones debe de ser positivo");
            acciones= numberStocks();
        }
        return acciones;
    }

    //It asks the user for the amount of the dividend each time that the company pays it
    public float dividendValue(){
        Scanner entrada = new Scanner(System.in);
        System.out.println("Indique de cuanto es el dividendo cada vez que te pagan: ");
        float dividendo=0;
        dividendo=entrada.nextFloat();
        entrada.nextLine();
        if(!checkPositiveValues(dividendo)){
            System.out.println("Error, el dividendo debe de ser positivo");
            dividendo= dividendValue();
        }
        return dividendo;
    }

    //It asks the user for the free cash flow of the company
    public float valueFCF(){
        Scanner entrada = new Scanner(System.in);
        System.out.println("Introduzca el FCF de la empresa en el año fiscal actual: ");
        float FCF=0;
        FCF=entrada.nextFloat();
        entrada.nextLine();
        if(!checkPositiveValues(FCF)){
            System.out.println("Error, el flujo de caja libre debe de ser positivo");
            FCF= dividendValue();
        }
        return FCF;
    }

    //It asks the user for the long term debt of the company
    public float valueLongTermDebt(){
        Scanner entrada = new Scanner(System.in);
        System.out.println("Introduzca la deuda a largo plazo: ");
        float valor=0;
        valor=entrada.nextFloat();
        entrada.nextLine();
        if(!checkPositiveValues(valor)){
            System.out.println("Error, la deuda a largo plazo debe de ser positiva");
            valor= dividendValue();
        }
        return valor;
    }

    //It asks the user for the short term debt of the company
    public float valueShortTermDebt(){
        Scanner entrada = new Scanner(System.in);
        System.out.println("Introduzca la deuda a corto plazo: ");
        float valor=0;
        valor=entrada.nextFloat();
        entrada.nextLine();
        if(!checkPositiveValues(valor)){
            System.out.println("Error, la deuda a corto plazo debe de ser positiva");
            valor= dividendValue();
        }
        return valor;
    }

    //It asks the user for the available cash of the company
    public float valueCash(){
        Scanner entrada = new Scanner(System.in);
        System.out.println("Introduzca el dinero en efectivo (Cash & cash equivalents): ");
        float valor=0;
        valor=entrada.nextFloat();
        entrada.nextLine();
        if(!checkPositiveValues(valor)){
            System.out.println("Error, el dinero en efectivo debe de ser positivo");
            valor= dividendValue();
        }
        return valor;
    }

    //It asks the user for the net income of the company
    public float valueEBIT(){
        Scanner entrada = new Scanner(System.in);
        System.out.println("Introduzca el beneficio neto: ");
        float valor=0;
        valor=entrada.nextFloat();
        entrada.nextLine();
        if(!checkPositiveValues(valor)){
            System.out.println("Error, el beneficio neto debe de ser positivo");
            valor= dividendValue();
        }
        return valor;
    }

    //It asks the user for the depreciation of the company
    public float valueDepreciation(){
        Scanner entrada = new Scanner(System.in);
        System.out.println("Introduzca la depreciación: ");
        float valor=0;
        valor=entrada.nextFloat();
        entrada.nextLine();
        if(!checkPositiveValues(valor)){
            System.out.println("Error, la depreciación debe de ser positiva");
            valor= dividendValue();
        }
        return valor;
    }

    //It asks the user for the amortization of the company
    public float valueAmortization(){
        Scanner entrada = new Scanner(System.in);
        System.out.println("Introduzca la amortización: ");
        float valor=0;
        valor=entrada.nextFloat();
        entrada.nextLine();
        if(!checkPositiveValues(valor)){
            System.out.println("Error, la amortización debe de ser positiva");
            valor= dividendValue();
        }
        return valor;
    }

    //It asks the user for the net revenues of the company
    public float valueRevenues(){
        Scanner entrada = new Scanner(System.in);
        System.out.println("Introduzca las ventas (net revenues): ");
        float valor=0;
        valor=entrada.nextFloat();
        entrada.nextLine();
        if(!checkPositiveValues(valor)){
            System.out.println("Error, las ventas deben de ser positivas");
            valor= dividendValue();
        }
        return valor;
    }

    //It asks the user for the equity of the company
    public float valueEquity(){
        Scanner entrada = new Scanner(System.in);
        System.out.println("Introduzca el patrimonio (equity): ");
        float valor=0;
        valor=entrada.nextFloat();
        entrada.nextLine();
        if(!checkPositiveValues(valor)){
            System.out.println("Error, el patrimonio debe de ser positivo");
            valor= dividendValue();
        }
        return valor;
    }

}
