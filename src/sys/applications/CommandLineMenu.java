package sys.applications;
import javafx.util.Pair;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.DefaultXYDataset;
import sys.Config;
import sys.Core.*;
import sys.Simulation;
import sys.State;
import sys.applications.scenarios.CustomScenario;
import sys.applications.scenarios.DefaultScenario;
import sys.applications.scenarios.PeopleMetGetsTestedScenario;
import sys.applications.scenarios.ScenarioInfos;
import sys.models.IMenu;
import sys.models.Scenario;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;
import java.lang.Math;
<<<<<<< HEAD
=======
import java.lang.reflect.Method;
>>>>>>> 27e8fe60c8cf90b30adc541618970d104bdc3e3a
import java.util.function.IntPredicate;

public class CommandLineMenu implements IMenu {
    private Scanner input = new Scanner(System.in);
    private DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.ITALIAN);
<<<<<<< HEAD
    public void createDataset(State currentState, int value) {
        ArrayList<ArrayList<Integer>> timeRange;
        File file;
        if (value == 0) {// total
            timeRange = currentState.total;
            file = new File(currentState.configs.outputTotalFile);
        } else {//daily
            timeRange = currentState.daily;
            file = new File(currentState.configs.outputDailyFile);
        }
        double[] x1 = new double[timeRange.get(0).size()];
        double[] y1 = new double[timeRange.get(0).size()];
        double[] y2 = new double[timeRange.get(0).size()];
        double[] y3 = new double[timeRange.get(0).size()];
        double[] y4 = new double[timeRange.get(0).size()];

        for (int i=0; i<y1.length; i++) {
            y1[i] = timeRange.get(0).get(i);
            y2[i] = timeRange.get(1).get(i);
            y3[i] = timeRange.get(2).get(i);
            y4[i] = timeRange.get(3).get(i);
            x1[i] = i;
        }

        DefaultXYDataset dataset = new DefaultXYDataset();
        dataset.addSeries("Total infected", new double[][]{x1,y1});
        dataset.addSeries("Total symptomatics", new double[][]{x1,y2});
        dataset.addSeries("Total deaths", new double[][]{x1,y3});
        dataset.addSeries("Total swabs", new double[][]{x1,y4});

        JFreeChart chart = ChartFactory.createXYLineChart("People", "Day passed", "Number of people", dataset);

        XYPlot plot = chart.getXYPlot();

        NumberAxis nAxis = (NumberAxis) plot.getDomainAxis();
        NumberTickUnit unit = new NumberTickUnit(10);
        nAxis.setTickUnit(unit);

        try {
            ChartUtils.saveChartAsPNG(file, chart, 1000, 700);
        }catch (IOException ignored) {}

    }
=======
    

>>>>>>> 27e8fe60c8cf90b30adc541618970d104bdc3e3a
    public void clear() {
        for(int i=0; i<50; i++) System.out.println();
    }

    public void ScreenSH() {
        clear();
        printPersonalizedTitle("Menù Principale");
        System.out.println("1) Inizia la simulazione");
<<<<<<< HEAD
        System.out.println("2) Menu' opzioni...");
=======
        System.out.println("2) Menu' parametri principali...");
        System.out.println("3) Menu' parametri opzionali...");
>>>>>>> 27e8fe60c8cf90b30adc541618970d104bdc3e3a
        System.out.println("3) Menu' scenari...");
        System.out.println("\n0) Esci");

    }

    public void ScreenFI() {
<<<<<<< HEAD
=======
        clear();
>>>>>>> 27e8fe60c8cf90b30adc541618970d104bdc3e3a
        printPersonalizedTitle("Configurazione Iniziale");
    }

    public void ScreenSET(Config config) {
<<<<<<< HEAD
        printPersonalizedTitle("Menù Parametri");
        System.out.println("Per modificare i parametri inziali digita: ");
        System.out.println("1) Popolazione inziale:\t\t\t" + format(config.populationNumber) + " valore attuale;");
        System.out.println("2) Durata malattia:\t\t\t\t" + format(config.diseaseDuration) + " valore attuale;");
        System.out.println("3) Risorse inziali:\t\t\t\t" + format(config.initialResources) + " valore attuale;");
        System.out.println("4) Costo del tampone:\t\t\t" + format(config.swabsCost) + " valore attuale;");
        System.out.println("5) Infettivita':\t\t\t\t" + format(config.infectivity) + " valore attuale;");
        System.out.println("6) Sintomaticita':\t\t\t\t" + format(config.sintomaticity) + " valore attuale;");
        System.out.println("7) Letalita':\t\t\t\t\t" + format(config.letality) + " valore attuale;");
        System.out.println("8) Eta' massima:\t\t\t\t" + format(config.maxAge) + " valore attuale;");
        System.out.println("9) Eta' media:\t\t\t\t\t" + format(config.ageAverage) + " valore attuale;");
        // System.out.println("10) Spazio della simulazione:\t(" + config.size.toString().replace("="," , ")+ ") valore attuale;");
        System.out.println("\n0) Torna indietro...");
    }

    
   /*public void ScreenSEN() {
=======
        clear();
        printPersonalizedTitle("Menù Parametri");
        System.out.println("Per modificare i parametri inziali digita: ");
        System.out.println("1) Popolazione inziale:\t\t\t" + format(config.getPopulationNumber()) + " valore attuale;");
        System.out.println("2) Durata malattia:\t\t\t\t" + format(config.getDiseaseDuration()) + " valore attuale;");
        System.out.println("3) Risorse inziali:\t\t\t\t" + format(config.getInitialResources()) + " valore attuale;");
        System.out.println("4) Costo del tampone:\t\t\t" + format(config.getSwabsCost()) + " valore attuale;");
        System.out.println("5) Infettivita':\t\t\t\t" + format(config.getInfectivity()) + " valore attuale;");
        System.out.println("6) Sintomaticita':\t\t\t\t" + format(config.getSintomaticity()) + " valore attuale;");
        System.out.println("7) Letalita':\t\t\t\t\t" + format(config.getLetality()) + " valore attuale;");
        // System.out.println("8) Spazio della simulazione:\t(" + config.size.toString().replace("="," , ")+ ") valore attuale;");
        System.out.println("9) Per settare i paramteri opzionali;");
        System.out.println("\n0) Torna al menù principale...");
    }

    public void ScreenSET_OP(Config config) {
        printPersonalizedTitle("Menù Parametri opzionali");
        System.out.println("Per modificare i parametri opzionali digita: ");
        System.out.println("1) Eta' massima:\t\t\t\t" + format(config.getMaxAge()) + " valore attuale;");
        System.out.println("2) Eta' media:\t\t\t\t\t" + format(config.getAgeAverage()) + " valore attuale;");
        System.out.println("3) Velocità:\t\t\t\t\t" + config.getVelocity() + " valore attuale;");
        System.out.println("4) Frame per ogni giorno:\t\t\t\t\t" + format(config.getFrameADay()) + " valore attuale;");
        System.out.println("5) Distanza sociale:\t\t\t\t\t" + format(config.getSocialDistance()) + " valore attuale;");
        System.out.println("9) Per settare i parametri principali;");
        System.out.println("\n0) Torna al menù principale...");
    }

    
    /*public void ScreenSEN() {
>>>>>>> 27e8fe60c8cf90b30adc541618970d104bdc3e3a
        System.out.println("MENU' SCENARIO");
        System.out.println("#");
        System.out.println("#");
        System.out.println("Scegli una strategia per contrastare il virus:");
        System.out.println("0 - Strategia di default");
        System.out.println("1 - Tamponi casuali sulle persone ogni giorno");
        System.out.println("2 - Tamponi sui contatti delle persone risultate positive al tampone");
        System.out.println("3 - In modo casuale, vengono fermate persone per un tempo limitato");
        System.out.println("4 - Tutta la popolazione, o una parte, usa le mascherine (maggior consumo di risorse)");
    }*/
<<<<<<< HEAD

   /* NUOVO METODO. Serve per richiamare le operazioni di schermo che riguardano
   la configurazione dei dati. Poichè l'argomento config serve soltanto per il menù SET,
   nel caso venga chiamato FI, config viene scartato. Comunque, dato che setting e firstInput
   sono metodi in cui vengono configurati i paramteri in Config (ricevendo entrambi lo stesso oggetto), 
   questo è un loro metodo di appoggio per quanto riguarda la parte "visiva" dell'interfaccia */
   @Ready
   private void configurationScreen(String key,Config config) {
       clear();
       switch(key) {
=======
 
    @Ready
    private void configurationScreen(String key,Config config) {
       switch(key) {
           case "SET_OP":
           ScreenSET_OP(config);
           break;

>>>>>>> 27e8fe60c8cf90b30adc541618970d104bdc3e3a
           case "SET":
           ScreenSET(config);
           break;

           case "FI":
           config = null;
           ScreenFI();
           break;

           /*
           case "SEN":    //Non dovrebbe servire.
           ScreenSEN();
           break; */
       }
<<<<<<< HEAD
   }  

   private String format(int value){
       return formatter.format(value);
   }

   @Ready
   private void printPersonalizedTitle (String title){
       System.out.println("######### " + title + " #########\n");
   }

    @Override /* METODO MODFICATO
    Dato che show non è un menù di configurazione, non è incluso in configurationScreen() */
=======
    }  

    private String format(int value){
       return formatter.format(value);
    }

    @Ready
    private void printPersonalizedTitle (String title){
       System.out.println("######### " + title + " #########\n");
    }

    public interface Functional<T>{
       public boolean test(T parameter);
    } 

    @Override
>>>>>>> 27e8fe60c8cf90b30adc541618970d104bdc3e3a
    public int show() {
        ScreenSH();
        int action = getInput(Integer.class);
        while(action < 0 || action > 3) {
            ScreenSH();
            action = getInput(Integer.class);
        }
        return action+1;
    }

<<<<<<< HEAD
    @Ready  /* METODO MODIFICATO
    Importante: il metodo prende in input una variabile config che serve per poter
    richiamare il metodo configurationScreen(). Viene usato appunto in firstInput e settings */
    private int inputUntilValid(int condition1, int condition2, String errorMessage, String request, String keyOS,Config config){
        configurationScreen(keyOS, config);
        System.out.print(request);
        int x = getInput(Integer.class);
        while(!(condition1 < x && x < condition2)) {
            configurationScreen(keyOS, config);  
            System.out.print(errorMessage);
            x = getInput(Integer.class);
        }
        return x;
=======
    @Ready 
    private <E> void inputUntilValid(Functional<E> method, Class<E> type, String errorMessage, String request, String keyOS,Config config){
        configurationScreen(keyOS, config);
        System.out.print(request);
        boolean verify = method.test(getInput(type));
        while(!(verify)) {
            configurationScreen(keyOS, config);  
            System.out.print(errorMessage);
            verify = method.test(getInput(type));
        }
>>>>>>> 27e8fe60c8cf90b30adc541618970d104bdc3e3a
    }

    @Override
    public void firstInput(Config config) {
        // Popolazione iniziale
<<<<<<< HEAD
        config.populationNumber = inputUntilValid(0, Config.POPULATION_NUMBER_UPPER_BOUND, "Numero non valido. Reinserirlo (<= "+Config.POPULATION_NUMBER_UPPER_BOUND+"): ", "Inserire il numero della popolazione iniziale (<= "+ Config.POPULATION_NUMBER_UPPER_BOUND +"): ", "FI", config);

        // Durata della malattia
        config.diseaseDuration = inputUntilValid(Config.DISEASE_DURATION_LOWER_BOUND, Config.DISEASE_DURATION_UPPER_BOUND, "Numero non valido. Reinserirlo (<= " + Config.DISEASE_DURATION_UPPER_BOUND + "): ", "Inserire la durata della malattia in giorni ("+ Config.DISEASE_DURATION_LOWER_BOUND+" < x < "+ Config.DISEASE_DURATION_UPPER_BOUND +"): ", "FI", config);

        // Risorse iniziali
        config.initialResources = inputUntilValid(Config.RESOURCES_LOWER_BOUND , (config.populationNumber*config.diseaseDuration), "Numero non valido. Reinserirlo ("+ Config.RESOURCES_LOWER_BOUND + " < x < " +(config.populationNumber*config.diseaseDuration)+ "): ", "Inserire il numero delle risorse iniziali (< " +(config.populationNumber*config.diseaseDuration)+ "): ", "FI", config);
        config.incubationToYellowDeadline = (int) (config.diseaseDuration * Config.INCUBATION_TO_YELLOW_DEADLINE);
        config.yellowToRedDeadline = (int) (config.diseaseDuration * Config.YELLOW_TO_RED_DEADLINE);

        // Costo del tampone
        config.swabsCost = inputUntilValid((config.initialResources/(config.populationNumber*10)), (int)Double.POSITIVE_INFINITY, "Numero troppo piccolo. Reinserirlo (> " + (config.initialResources/(config.populationNumber*10)) +"): ", "Inserire il costo del tampone (> "+ (config.initialResources/(config.populationNumber*10)) +"): ", "FI", config);

        // Infettività
        config.infectivity = inputUntilValid(0, 100, "Percentuale non valida. Reinserirla (0 <= x < 100): ", "Inserire la percentuale di infettivita' (0 <= x < 100): ", "FI", config);

        // Sintomaticità
        config.sintomaticity = inputUntilValid(0, 100, "Percentuale non valida. Reinserirla (0 <= x < 100): ", "Inserire la percentuale di sintomaticita' (0 <= x < 100): ", "FI", config);

        // Letalità
        config.letality = inputUntilValid(0, 100, "Percentuale non valida. Reinserirla (0 <= x < 100): ", "Inserire la percentuale di letalita' (0 <= x < 100): ", "FI", config);

        configurationScreen("FI", config);

=======
        inputUntilValid(config::setPopulationNumber, Integer.class, "Numero non valido. Reinserirlo (<= "+Config.POPULATION_NUMBER_UPPER_BOUND+"): ", "Inserire il numero della popolazione iniziale (<= "+ Config.POPULATION_NUMBER_UPPER_BOUND +"): ", "FI", config);

        // Durata della malattia
        inputUntilValid(config::setDiseaseDuration, Integer.class, "Numero non valido. Reinserirlo (<= " + Config.DISEASE_DURATION_UPPER_BOUND + "): ", "Inserire la durata della malattia in giorni ("+ Config.DISEASE_DURATION_LOWER_BOUND+" < x < "+ Config.DISEASE_DURATION_UPPER_BOUND +"): ", "FI", config);

        // Risorse iniziali
        inputUntilValid(config::setInitialResources, Integer.class, "Numero non valido. Reinserirlo ("+ Config.RESOURCES_LOWER_BOUND + " < x < " +(config.getPopulationNumber()*config.getDiseaseDuration())+ "): ", "Inserire il numero delle risorse iniziali (< " +(config.getPopulationNumber()*config.getDiseaseDuration())+ "): ", "FI", config);

        // Costo del tampone
        inputUntilValid(config::setSwabsCost, Integer.class, "Numero troppo piccolo. Reinserirlo (> " + (config.getInitialResources()/(config.getPopulationNumber()*10)) +"): ", "Inserire il costo del tampone (> "+ (config.getInitialResources()/(config.getPopulationNumber()*10)) +"): ", "FI", config);

        // Infettività
        inputUntilValid(config::setInfectivity, Integer.class, "Percentuale non valida. Reinserirla (0 <= x < 100): ", "Inserire la percentuale di infettivita' (0 <= x < 100): ", "FI", config);

        // Sintomaticità
        inputUntilValid(config::setSintomaticity, Integer.class, "Percentuale non valida. Reinserirla (0 <= x < 100): ", "Inserire la percentuale di sintomaticita' (0 <= x < 100): ", "FI", config);

        // Letalità
        inputUntilValid(config::setLetality, Integer.class, "Percentuale non valida. Reinserirla (0 <= x < 100): ", "Inserire la percentuale di letalita' (0 <= x < 100): ", "FI", config);

        configurationScreen("FI", config);
>>>>>>> 27e8fe60c8cf90b30adc541618970d104bdc3e3a
        System.out.println("\nInserire l'altezza e la larghezza dello spazio grafico dove avverrà la simulazione.");
        System.out.println("Più lo spazio è grande, meno spesso avverranno i contatti tra le persone!\n");
        System.out.print("Larghezza: (" + format(config.SizeLowerBound.apply(0)) + "<=x<=" + format(config.SizeUpperBound.apply(0)) + ", valore consigliato " + format(config.PreferredSizeBound.apply(0)) + "): ");
        while (!config.setSizeX(getInput(Integer.class))) {
            System.out.print("Parametri non validi, reinserirli: ");
        }
        System.out.print("Altezza: (" + format(config.SizeLowerBound.apply(1)) + "<=x<=" + format(config.SizeUpperBound.apply(1)) + ", valore consigliato " + format(config.PreferredSizeBound.apply(1)) + "): ");
        while (!config.setSizeY(getInput(Integer.class))) {
            System.out.print("Parametri non validi, reinserirli: ");
        }
<<<<<<< HEAD
        // config.size = new Pair<>(width, height);
=======
>>>>>>> 27e8fe60c8cf90b30adc541618970d104bdc3e3a
        
        //TODO: DA QUI IN POI RENDERE OPZIONALI
        configurationScreen("FI", config);
        boolean result = askConfirmation("Settare anche i valori ozpionali?");
<<<<<<< HEAD
        /*
        Integer option = getInput(Integer.class);
        while(option != 0 && option != 1) {
            configurationScreen("FI", config);
            System.out.print("Settare anche i valori ozpionali? (0/1) ");
            option = getInput(Integer.class);
        }
          */
        if (result) {
            // Età massima
            config.maxAge = inputUntilValid(Config.MAX_AGE_LOWER_BOUND, Config.MAX_AGE_UPPER_BOUND, "Eta' non valida. Reinserirla "+ Config.MAX_AGE_LOWER_BOUND +"< x <"+ Config.MAX_AGE_UPPER_BOUND +": ", "Inserire l'eta' massima (" +Config.MAX_AGE_LOWER_BOUND+ "< x < " + Config.MAX_AGE_UPPER_BOUND +"): ", "FI", config);
            // Età media
            config.ageAverage = inputUntilValid(Config.AGE_AVERAGE_LOWER_BOUND, Config.AGE_AVERAGE_UPPER_BOUND, "Eta' non valida. Reinserirla ("+ Config.AGE_AVERAGE_LOWER_BOUND +"< x <"+ Config.AGE_AVERAGE_UPPER_BOUND +"): ", "Inserire l'eta' media (" +Config.AGE_AVERAGE_LOWER_BOUND+ "< x < " + Config.AGE_AVERAGE_UPPER_BOUND +"): ","FI", config);
=======
       
        if (result) {
            // Età massima
            inputUntilValid(config::setMaxAge, Integer.class, "Eta' non valida. Reinserirla "+ Config.MAX_AGE_LOWER_BOUND +"< x <"+ Config.MAX_AGE_UPPER_BOUND +": ", "Inserire l'eta' massima (" +Config.MAX_AGE_LOWER_BOUND+ "< x < " + Config.MAX_AGE_UPPER_BOUND +"): ", "FI", config);
            // Età media
            inputUntilValid(config::setAgeAverage, Integer.class, "Eta' non valida. Reinserirla ("+ Config.AGE_AVERAGE_LOWER_BOUND +"< x <"+ Config.AGE_AVERAGE_UPPER_BOUND +"): ", "Inserire l'eta' media (" +Config.AGE_AVERAGE_LOWER_BOUND+ "< x < " + Config.AGE_AVERAGE_UPPER_BOUND +"): ","FI", config);
>>>>>>> 27e8fe60c8cf90b30adc541618970d104bdc3e3a
        }

    }

    @Override
    public int settings(Config config) {
        int action = -1;

<<<<<<< HEAD
        while(action!=0) {
=======
        while(action!=0 && action!=9) {
>>>>>>> 27e8fe60c8cf90b30adc541618970d104bdc3e3a
            configurationScreen("SET", config);
            action = getInput(Integer.class);
            switch(action) {
                case(1):
                // Popolazione iniziale
<<<<<<< HEAD
                config.populationNumber = inputUntilValid(0, Config.POPULATION_NUMBER_UPPER_BOUND, "Numero non valido. Reinserirlo (<= "+Config.POPULATION_NUMBER_UPPER_BOUND+"): ", "Inserire il numero della popolazione iniziale (<= "+ Config.POPULATION_NUMBER_UPPER_BOUND +"): ", "SET", config);
                // settando un nuovo valore per la popolazione iniziale controllo se 
                // il valore delle risorse inziali sia ancora buono
                if(config.initialResources > config.populationNumber * config.diseaseDuration) {
                    config.initialResources = inputUntilValid(Config.RESOURCES_LOWER_BOUND , (config.populationNumber*config.diseaseDuration), "Numero non valido. Reinserirlo ("+ Config.RESOURCES_LOWER_BOUND + " < x < " +(config.populationNumber*config.diseaseDuration)+ "): ", "Inserire il numero delle risorse iniziali (< " +(config.populationNumber*config.diseaseDuration)+ "): ", "SET", config);
                }
                // controllo se il valore del costo del tampone sia ancora buono
                if(config.swabsCost > config.initialResources/(config.populationNumber * 10)) {
                    config.swabsCost = inputUntilValid((config.initialResources/(config.populationNumber*10)), (int)Double.POSITIVE_INFINITY, "Numero troppo piccolo. Reinserirlo (> " + (config.initialResources/(config.populationNumber*10)) +"): ", "Inserire il costo del tampone (> "+ (config.initialResources/(config.populationNumber*10)) +"): ", "SET", config);
                }
                // e controllo se i valori di altezza e larghezza siano ancora validi
/* TOGLIERE IL COMMENTO
                if((config.size[0]*config.size[1] > config.populationNumber*10)||(config.size[0]*config.size[1] < config.populationNumber/10)) {
                    operationScreen("SET");
                    System.out.println("ATTENZIONE! lo spazio grafico non è più idoneo:");
                    System.out.println("\nInserire l'altezza e la larghezza dello spazio grafico dove avverrà la simulazione.");
                    System.out.println("Più lo spazio è grande, meno spesso avverranno i contatti tra le persone!\n");
                    System.out.print("Larghezza: (> 2, valore consigliato "+(int)Math.sqrt(config.populationNumber)*2+"): ");
                    int width = getInput(Integer.class);
                    System.out.print("Altezza: ("+ Math.max(2, (config.populationNumber/10)/width) + "< x <" +((config.populationNumber*10)/width)+ ", valore consigliato "+ (4*config.populationNumber/width)+"): ");
                    int height = getInput(Integer.class);

                    while(width*height > config.populationNumber*4 | width<2 | height <2) {
                        operationScreen("SET");
                        System.out.println("Parametri non validi. Reinserirli.");
                        System.out.print("Larghezza: (> 2, valore consigliato "+(int)Math.sqrt(config.populationNumber)*2+"): ");
                        width = getInput(Integer.class);
                        System.out.print("Altezza: ("+ Math.max(2, (config.populationNumber/10)/width) + "< x <" +((config.populationNumber*10)/width)+ ", valore consigliato "+ (4*config.populationNumber/width)+"): ");
                        height = getInput(Integer.class);
                    }
                    // config.size = new Pair<>(width, height);      
                }*/
=======
                inputUntilValid(config::setPopulationNumber, Integer.class, "Numero non valido. Reinserirlo (<= "+Config.POPULATION_NUMBER_UPPER_BOUND+"): ", "Inserire il numero della popolazione iniziale (<= "+ Config.POPULATION_NUMBER_UPPER_BOUND +"): ", "SET", config);
                // settando un nuovo valore per la popolazione iniziale controllo se 
                // il valore delle risorse inziali sia ancora buono
                if(!(config.setInitialResources(config.getInitialResources()))){
                    inputUntilValid(config::setInitialResources, Integer.class, "Numero non valido. Reinserirlo ("+ Config.RESOURCES_LOWER_BOUND + " < x < " +(config.getPopulationNumber()*config.getDiseaseDuration())+ "): ", "Inserire il numero delle risorse iniziali (< " +(config.getPopulationNumber()*config.getDiseaseDuration())+ "): ", "SET", config);
                }
                // controllo se il valore del costo del tampone sia ancora buono
                if(!(config.setSwabsCost(config.getSwabsCost()))){
                    inputUntilValid(config::setSwabsCost, Integer.class, "Numero troppo piccolo. Reinserirlo (> " + (config.getInitialResources()/(config.getPopulationNumber()*10)) +"): ", "Inserire il costo del tampone (> "+ (config.getInitialResources()/(config.getPopulationNumber()*10)) +"): ", "SET", config);
                }
>>>>>>> 27e8fe60c8cf90b30adc541618970d104bdc3e3a
                break;
            
                case(2):
                // Durata della malattia
<<<<<<< HEAD
                config.diseaseDuration = inputUntilValid(Config.DISEASE_DURATION_LOWER_BOUND, Config.DISEASE_DURATION_UPPER_BOUND, "Numero non valido. Reinserirlo (<= " + Config.DISEASE_DURATION_UPPER_BOUND + "): ", "Inserire la durata della malattia in giorni ("+ Config.DISEASE_DURATION_LOWER_BOUND+" < x < "+ Config.DISEASE_DURATION_UPPER_BOUND +"): ", "SET", config);
                config.incubationToYellowDeadline = (int) (config.diseaseDuration * Config.INCUBATION_TO_YELLOW_DEADLINE);
                config.yellowToRedDeadline = (int) (config.diseaseDuration * Config.YELLOW_TO_RED_DEADLINE);
                // cambiando la durata della malattia controllo se il numero delle risorse iniziali
                // sia ancora buono
                if(config.initialResources > config.populationNumber * config.diseaseDuration) {
                    config.initialResources = inputUntilValid(Config.RESOURCES_LOWER_BOUND , (config.populationNumber*config.diseaseDuration), "Numero non valido. Reinserirlo ("+ Config.RESOURCES_LOWER_BOUND + " < x < " +(config.populationNumber*config.diseaseDuration)+ "): ", "Inserire il numero delle risorse iniziali (< " +(config.populationNumber*config.diseaseDuration)+ "): ", "SET", config);
=======
                inputUntilValid(config::setDiseaseDuration, Integer.class, "Numero non valido. Reinserirlo (<= " + Config.DISEASE_DURATION_UPPER_BOUND + "): ", "Inserire la durata della malattia in giorni ("+ Config.DISEASE_DURATION_LOWER_BOUND+" < x < "+ Config.DISEASE_DURATION_UPPER_BOUND +"): ", "SET", config);
                // cambiando la durata della malattia controllo se il numero delle risorse iniziali
                // sia ancora buono
                if(!(config.setInitialResources(config.getInitialResources()))){
                    inputUntilValid(config::setInitialResources, Integer.class, "Numero non valido. Reinserirlo ("+ Config.RESOURCES_LOWER_BOUND + " < x < " +(config.getPopulationNumber()*config.getDiseaseDuration())+ "): ", "Inserire il numero delle risorse iniziali (< " +(config.getPopulationNumber()*config.getDiseaseDuration())+ "): ", "SET", config);
>>>>>>> 27e8fe60c8cf90b30adc541618970d104bdc3e3a
                }
                break;

                case(3):
                // Risorse iniziali
<<<<<<< HEAD
                config.initialResources = inputUntilValid(Config.RESOURCES_LOWER_BOUND , (config.populationNumber*config.diseaseDuration), "Numero non valido. Reinserirlo ("+ Config.RESOURCES_LOWER_BOUND + " < x < " +(config.populationNumber*config.diseaseDuration)+ "): ", "Inserire il numero delle risorse iniziali (< " +(config.populationNumber*config.diseaseDuration)+ "): ", "SET", config);
                // cambiando il numero delle risorse iniziali controllo se il costo del tampone sia ancora buono
                if(config.swabsCost > config.initialResources/(config.populationNumber * 10)) {
                    config.swabsCost = inputUntilValid((config.initialResources/(config.populationNumber*10)), (int)Double.POSITIVE_INFINITY, "Numero troppo piccolo. Reinserirlo (> " + (config.initialResources/(config.populationNumber*10)) +"): ", "Inserire il costo del tampone (> "+ (config.initialResources/(config.populationNumber*10)) +"): ", "SET", config);
=======
                inputUntilValid(config::setInitialResources, Integer.class, "Numero non valido. Reinserirlo ("+ Config.RESOURCES_LOWER_BOUND + " < x < " +(config.getPopulationNumber()*config.getDiseaseDuration())+ "): ", "Inserire il numero delle risorse iniziali (< " +(config.getPopulationNumber()*config.getDiseaseDuration())+ "): ", "SET", config);
                // cambiando il numero delle risorse iniziali controllo se il costo del tampone sia ancora buono
                if(!(config.setSwabsCost(config.getSwabsCost()))){
                    inputUntilValid(config::setSwabsCost, Integer.class, "Numero troppo piccolo. Reinserirlo (> " + (config.getInitialResources()/(config.getPopulationNumber()*10)) +"): ", "Inserire il costo del tampone (> "+ (config.getInitialResources()/(config.getPopulationNumber()*10)) +"): ", "SET", config);
>>>>>>> 27e8fe60c8cf90b30adc541618970d104bdc3e3a
                }
                break;

                case(4):
                // Costo del tampone
<<<<<<< HEAD
                config.swabsCost = inputUntilValid((config.initialResources/(config.populationNumber*10)), (int)Double.POSITIVE_INFINITY, "Numero troppo piccolo. Reinserirlo (> " + (config.initialResources/(config.populationNumber*10)) +"): ", "Inserire il costo del tampone (> "+ (config.initialResources/(config.populationNumber*10)) +"): ", "SET", config);
=======
                inputUntilValid(config::setSwabsCost, Integer.class, "Numero troppo piccolo. Reinserirlo (> " + (config.getInitialResources()/(config.getPopulationNumber()*10)) +"): ", "Inserire il costo del tampone (> "+ (config.getInitialResources()/(config.getPopulationNumber()*10)) +"): ", "SET", config);
>>>>>>> 27e8fe60c8cf90b30adc541618970d104bdc3e3a
                break;

                case(5):
                // Infettività
<<<<<<< HEAD
                config.infectivity = inputUntilValid(0, 100, "Percentuale non valida. Reinserirla (0 <= x < 100): ", "Inserire la percentuale di infettivita' (0 <= x < 100): ", "SET", config);
=======
                inputUntilValid(config::setInfectivity, Integer.class, "Percentuale non valida. Reinserirla (0 <= x < 100): ", "Inserire la percentuale di infettivita' (0 <= x < 100): ", "FI", config);
>>>>>>> 27e8fe60c8cf90b30adc541618970d104bdc3e3a
                break;

                case(6):
                // Sintomaticità
<<<<<<< HEAD
                config.sintomaticity = inputUntilValid(0, 100, "Percentuale non valida. Reinserirla (0 <= x < 100): ", "Inserire la percentuale di sintomaticita' (0 <= x < 100): ", "SET", config);
=======
                inputUntilValid(config::setSintomaticity, Integer.class, "Percentuale non valida. Reinserirla (0 <= x < 100): ", "Inserire la percentuale di sintomaticita' (0 <= x < 100): ", "FI", config);
>>>>>>> 27e8fe60c8cf90b30adc541618970d104bdc3e3a
                break;

                case(7):
                // Letalità
<<<<<<< HEAD
                config.letality = inputUntilValid(0, 100, "Percentuale non valida. Reinserirla (0 <= x < 100): ", "Inserire la percentuale di letalita' (0 <= x < 100): ", "SET", config);
                break;

                case(8):
                // Età massima
                config.maxAge = inputUntilValid(50, 110, "Eta' non valida. Reinserirla (50 < x < 110): ", "Inserire l'eta' massima (50 < x < 110): ", "SET", config);
                break;

                case(9):
                // Età media
                config.ageAverage = inputUntilValid(20, 80, "Eta' non valida. Reinserirla (20 < x < 80): ", "Inserire l'eta' massima (20 < x < 80): ", "SET", config);
                break;

                /*case(10):
                operationScreen("SET");
                System.out.println("ATTENZIONE! lo spazio grafico non è più idoneo:");
                System.out.println("\nInserire l'altezza e la larghezza dello spazio grafico dove avverrà la simulazione.");
                System.out.println("Più lo spazio è grande, meno spesso avverranno i contatti tra le persone!\n");
                System.out.print("Larghezza: (> 2, valore consigliato "+(int)Math.sqrt(config.populationNumber)*2+"): ");
                int width = getInput(Integer.class);
                System.out.print("Altezza: ("+ Math.max(2, (config.populationNumber/10)/width) + "< x <" +((config.populationNumber*10)/width)+ ", valore consigliato "+ (4*config.populationNumber/width)+"): ");
                int height = getInput(Integer.class);
                while(width*height > config.populationNumber*4 | width<2 | height <2) {
                    operationScreen("SET");
                    System.out.println("Parametri non validi. Reinserirli.");
                    System.out.print("Larghezza: (> 2, valore consigliato "+(int)Math.sqrt(config.populationNumber)*2+"): ");
                    width = getInput(Integer.class);
                    System.out.print("Altezza: ("+ Math.max(2, (config.populationNumber/10)/width) + "< x <" +((config.populationNumber*10)/width)+ ", valore consigliato "+ (4*config.populationNumber/width)+"): ");
                    height = getInput(Integer.class);
                }
                // config.size = new Pair<>(width, height); 
                break;
                */

                case(0):
                break;

                default:
                break;
            }
        }
        return 0;
=======
                inputUntilValid(config::setLetality, Integer.class, "Percentuale non valida. Reinserirla (0 <= x < 100): ", "Inserire la percentuale di letalita' (0 <= x < 100): ", "FI", config);
                break;

                /*case(8):
                configurationScreen("FI", config);
                System.out.println("\nInserire l'altezza e la larghezza dello spazio grafico dove avverrà la simulazione.");
                System.out.println("Più lo spazio è grande, meno spesso avverranno i contatti tra le persone!\n");
                System.out.print("Larghezza: (" + format(config.SizeLowerBound.apply(0)) + "<=x<=" + format(config.SizeUpperBound.apply(0)) + ", valore consigliato " + format(config.PreferredSizeBound.apply(0)) + "): ");
                while (!config.setSizeX(getInput(Integer.class))) {
                    System.out.print("Parametri non validi, reinserirli: ");
                }
                System.out.print("Altezza: (" + format(config.SizeLowerBound.apply(1)) + "<=x<=" + format(config.SizeUpperBound.apply(1)) + ", valore consigliato " + format(config.PreferredSizeBound.apply(1)) + "): ");
                while (!config.setSizeY(getInput(Integer.class))) {
                    System.out.print("Parametri non validi, reinserirli: ");
                }
                break;
                */
                
                case(9): break;
                case(0): break;

                default:
                break;
            }
        }
        if(action==9) return 4;
        return action;
    }

    @Override
    public int settings_op(Config config) {
        int action = -1;
 
        while(action!=0 && action!=9) {
            configurationScreen("SET_OP", config);
            action = getInput(Integer.class);
            switch(action){
                case(1):
                // Età massima
                inputUntilValid(config::setMaxAge, Integer.class, "Eta' non valida. Reinserirla "+ Config.MAX_AGE_LOWER_BOUND +"< x <"+ Config.MAX_AGE_UPPER_BOUND +": ", "Inserire l'eta' massima (" +Config.MAX_AGE_LOWER_BOUND+ "< x < " + Config.MAX_AGE_UPPER_BOUND +"): ", "SET_OP", config);
                break;

                case(2):
                // Età media
                inputUntilValid(config::setAgeAverage, Integer.class, "Eta' non valida. Reinserirla ("+ Config.AGE_AVERAGE_LOWER_BOUND +"< x <"+ Config.AGE_AVERAGE_UPPER_BOUND +"): ", "Inserire l'eta' media (" +Config.AGE_AVERAGE_LOWER_BOUND+ "< x < " + Config.AGE_AVERAGE_UPPER_BOUND +"): ","SET_OP", config);
                break;

                case(3):
                // Velocità
                inputUntilValid(config::setVelocity, Integer.class, "Numero non valido. Reinserirlo(> 0)", "Inserire la velocità(> 0): ", "SET_OP", config);
                break;

                case(4):
                // Frame per ogni giorno
                inputUntilValid(config::setFrameADay, Integer.class, "Numero non valido. Reinserirlo(> 0)", "Inserire il numero di frame per ogni giorno(> 0): ", "SET_OP", config);
                break;

                case(5):
                // Distanza sociale
                inputUntilValid(config::setSocialDistance, Integer.class, "Numero non valido. Reinserirlo(> 0)", "Inserire la distanza(> 0): ", "SET_OP", config);
                break;

                case(6): break;
                case(0): break;
            }
        }
        if(action==6) return 3;
        return action;   
>>>>>>> 27e8fe60c8cf90b30adc541618970d104bdc3e3a
    }

    @Override
    public void feedback(State state){
        printPersonalizedTitle("GIORNO " + state.currentDay);
        System.out.println("Popolazione sana: " + state.getHealthyNumber());
        System.out.println("Popolazione guarita: " + state.getCuredNumber());
        System.out.print("Popolazione infettata: " + state.total.get(0).get(state.total.get(0).size()-1));
        System.out.println(" (" + state.daily.get(0).get(state.daily.get(0).size()-1) +" in più rispetto a ieri)");
        System.out.println("Morti: " + state.getDeathsNumber());
        System.out.println("Risorse disponibili: " + state.resources);
        System.out.println("\n\n");
    }
<<<<<<< HEAD
=======

>>>>>>> 27e8fe60c8cf90b30adc541618970d104bdc3e3a
    @Override
    public void finalFeedback(State state) {
        printPersonalizedTitle("SIMULAZIONE TERMINATA");
        System.out.println("Resoconto finale\n");
        System.out.println("Giorni passati: " + state.currentDay);
        System.out.println("Popolazione sana: " + state.getHealthyNumber());
        System.out.println("Popolazione guarita: " + state.getCuredNumber());
        System.out.println("Popolazione infetta: " + state.total.get(0).get(state.total.get(0).size()-1));
        System.out.println("Morti: " + state.getDeathsNumber());
        System.out.println("Risorse rimaste: " + state.resources);
        System.out.println("Motivo termine simulazione: " + state.status);
<<<<<<< HEAD
        createDataset(state, 0);
        createDataset(state, 1);
=======
>>>>>>> 27e8fe60c8cf90b30adc541618970d104bdc3e3a
    }

    @Ready
    private void showScenarioSelectionMenu(Simulation currentSimulation){
        printPersonalizedTitle("Selezione Scenario");
        System.out.println("1) Scenario di default: " + getEnabledStatus(currentSimulation, null));
        System.out.println("2) Scenario personalizzato...\n");
        System.out.println("0) Torna indietro...");
    }

    @Ready
    private <E> E getInput(Class<E> eClass){
        E result = null;
        if (eClass == Integer.class){
            result = eClass.cast(input.nextInt());
        }else if (eClass == Double.class){
            result = eClass.cast(input.nextDouble());
        }else if (eClass == String.class){
            result = eClass.cast(input.next());
        }
        return result;
    }

    @NotImplemented
    private void customScenarioMenu(){
       printPersonalizedTitle("Custom Scenario");

    }

    @ToRevise
    private String getEnabledStatus(Simulation currentSimulation, Scenario scenario){
        final String positiveFeedback = "abilitato.";
        final String negativeFeedback = "disabilitato.";
        return currentSimulation.isScenarioEnabled(scenario) ? positiveFeedback : negativeFeedback;
    }

    @ToRevise
    private boolean askConfirmation(String customMessage){
        String positiveFeedback = "si";
        System.out.print(customMessage + " (si per accettare): ");
        String response = getInput(String.class);
        return response.equalsIgnoreCase(positiveFeedback);
    }

    @ToRevise
    private void printScenarioIntro(ScenarioInfos infos){
        //printPersonalizedTitle("Hai scelto " + type.getInfos().getName());
        //System.out.println(scenario.getInfos());
    }

    @ToRevise
    private Scenario enableDisable(Simulation simulation, Scenario scenario){
        boolean response;

        if (simulation.isScenarioEnabled(scenario)) {
            if (scenario instanceof DefaultScenario) {
                System.out.println("Lo scenario di default non può essere disabilitato.");
            } else {
                System.out.println("Lo scenario '" + scenario.getInfos().getName() + "' è abilitato.");
                response = askConfirmation("Sei sicuro di voler disabilitare '" + scenario.getInfos().getName() + "'?");
                if (response) {
                    System.out.println("Lo scenario '"+ scenario.getInfos().getName()+"' è stato disattivato.");
                    if (simulation.getCurrentScenario() instanceof CustomScenario) {
                        ((CustomScenario) simulation.getCurrentScenario()).removeScenario(scenario);
                        scenario = simulation.getCurrentScenario();
                    } else {
                        System.out.println("E' stato reimpostato lo scenario di default.");
                        scenario = new DefaultScenario(simulation);
                    }
                } else {
                    System.out.println("Hai annullato la modifica.");
                }
            }
        } else {
            if (scenario instanceof DefaultScenario) {
                response = askConfirmation("Sei sicuro di voler abilitare lo scenario di default?\n" +
                        "Abilitando questo scenario, verranno disabilitati tutti gli altri!");
                if (response) {
                    System.out.println("Lo scenario di default è stato abilitato.");
                } else {
                    System.out.println("Hai annullato la modifica.");
                }
            } else {
                response = askConfirmation("Sei sicuro di voler abilitare '" + scenario.getInfos().getName() + "'?");
                if (response) {
                    if (simulation.getCurrentScenario() instanceof CustomScenario) {
                        ((CustomScenario) simulation.getCurrentScenario()).addScenario(scenario);
                        scenario = simulation.getCurrentScenario();
                    }
                } else {
                    System.out.println("Hai annullato la modifica.");
                }
            }
        }

        System.out.print("Premi un tasto per continuare.");
        input.next();
        return scenario;
    }

    @ToRevise
    @Override
    public Scenario selectScenario(Simulation simulation) {   // BOZZA. DA MIGLIORARE
        showScenarioSelectionMenu(simulation);
        DefaultScenario defaultScenario = new DefaultScenario(simulation);
        Scenario result = simulation.getCurrentScenario();

        int action = -1;
        while(action != 0){
            System.out.print("\nInserisci un valore: ");
            action = getInput(Integer.class);

            switch (action){
                case 1:
                    printScenarioIntro(DefaultScenario.SCENARIO_INFOS);
                    result = enableDisable(simulation, defaultScenario);
                    break;
                case 2:
                    result = makeCustomScenario(simulation);
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Valore non valido!");
            }
        }
        return result;
    }



    @ToRevise
    private CustomScenario makeCustomScenario(Simulation simulation){
        CustomScenario customScenario = new CustomScenario(simulation);
        int action = -1;

        while(action != 0){
            clear();
            customScenarioMenu();
            System.out.print("Digita il numero dello scenario da attivare: ");
            action = getInput(Integer.class);

            switch(action) {
                case 1:
                    printScenarioIntro(PeopleMetGetsTestedScenario.SCENARIO_INFOS);
                    //PeopleMetGetsTestedScenario new_scen = new PeopleMetGetsTestedScenario();
                    break;
                case 2:

                    // Scenario2 new_scen = new Scenario2()
                    // return new_scen
                    break;
                case 3:
                    // Scenario2 new_scen = new Scenario2()
                    // return new_scen
                    break;
                case 0:
                    action = 0;
                    break;
                default:
                    System.out.println("Valore non valido!");
                    break;
            }
        }
        return customScenario;
    }

<<<<<<< HEAD
    /*public interface Functional<T>{
       public abstract boolean test(T parameter);
    } */

=======
    /*
>>>>>>> 27e8fe60c8cf90b30adc541618970d104bdc3e3a
    public static void main(String[] args) {
        CommandLineMenu c = new CommandLineMenu();
        Config config = new Config();
        c.firstInput(config);
        c.settings(config);
       
<<<<<<< HEAD
    }
=======
    }*/
>>>>>>> 27e8fe60c8cf90b30adc541618970d104bdc3e3a
}
