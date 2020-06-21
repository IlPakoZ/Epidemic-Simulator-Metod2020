package sys.applications;
import javafx.application.Preloader;
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
import java.util.function.IntPredicate;

public class CommandLineMenu implements IMenu {
    private Scanner input = new Scanner(System.in);
    private DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.ITALIAN);

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

    public void clear() {
        //for(int i=0; i<50; i++) System.out.println();
    }

    public void ScreenSH() {
        printPersonalizedTitle("Menù Principale");
        System.out.println("1) Inizia la simulazione");
        System.out.println("2) Menu' opzioni...");
        System.out.println("3) Menu' scenari...");
        System.out.println("\n0) Esci");

    }

    public void ScreenFI() {
        printPersonalizedTitle("Configurazione Iniziale");
    }

    public void ScreenSET(Config config) {
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
        System.out.println("10) Spazio della simulazione:\t(" + config.size.toString().replace("="," , ")+ ") valore attuale;");
        System.out.println("\n0) Torna indietro...");
    }

    
   /*public void ScreenSEN() {
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

   private String format(int value){
       return formatter.format(value);
   }

    @Override
    public int show() {
        clear();
        ScreenSH();
        int action = 0;
        IntPredicate condition = (x -> x < 0 | x > 3);
        System.out.print("\nInserire un valore: ");
        do{
            if (condition.test(action)){
                System.out.print("Valore non valido. Reinserirlo: ");
            }
            action = getInput(Integer.class);
        }while(condition.test(action));

        return action+1;
    }

    @Ready
    private <E> void inputUntilValid(Functional<E> f, Class<E> type, String errorMessage){
        boolean redo = f.test(getInput(type));
        while(!redo) {
            System.out.print(errorMessage);
            redo = f.test(getInput(type));
        }
    }

    @Override
    public void firstInput(Config config) {
        System.out.println();
        ScreenFI();

        // Popolazione iniziale
        System.out.print("Inserire il numero della popolazione iniziale (<= "+ Config.POPULATION_NUMBER_UPPER_BOUND +"): ");
        inputUntilValid(config::setPopulationNumber, Integer.class, "Numero non valido. Reinserirlo (<= "+Config.POPULATION_NUMBER_UPPER_BOUND+"): ");

        // Durata della malattia
        System.out.print("Inserire la durata della malattia in giorni ("+ Config.DISEASE_DURATION_LOWER_BOUND+"< "+ Config.DISEASE_DURATION_UPPER_BOUND +"): ");
        inputUntilValid(config::setDiseaseDuration, Integer.class, "Numero non valido. Reinserirlo (<= " + Config.DISEASE_DURATION_UPPER_BOUND + "): ");

        // Risorse iniziali
        System.out.print("Inserire il numero delle risorse iniziali (<" +(config.populationNumber*config.diseaseDuration)+ "): ");
        inputUntilValid(config::setInitialResources, Integer.class, "Numero non valido. Reinserirlo ("+ Config.RESOURCES_LOWER_BOUND + "<x<" +(config.populationNumber*config.diseaseDuration)+ "): ");

        // Costo del tampone
        System.out.print("Inserire il costo del tampone (> "+ (config.initialResources/(config.populationNumber*10)) +"): ");
        inputUntilValid(config::setSwabsCost, Integer.class, "Numero troppo piccolo. Reinserirlo (> " + (config.initialResources/(config.populationNumber*10)) +"): ");

        // Infettività
        System.out.print("Inserire la percentuale di infettivita' (0<=x<100): ");
        inputUntilValid(config::setInfectivity, Integer.class, "Percentuale non valida. Reinserirla (0<=x<100): ");

        // Sintomaticità
        System.out.print("Inserire la percentuale di sintomaticita' (0<=x<100): ");
        inputUntilValid(config::setSintomaticity, Integer.class, "Percentuale non valida. Reinserirla (0<=x<100): ");

        // Letalità
        System.out.print("Inserire la percentuale di letalita' (0<=x<100): ");
        Integer letality = getInput(Integer.class);
        while(letality <= 0.0 || letality > 100.0) {
            System.out.print("Percentuale non valida. Reinserirla (0<=x<100): ");
            letality = getInput(Integer.class);
        }
        config.letality = letality;

        System.out.println("\nInserire l'altezza e la larghezza dello spazio grafico dove avverrà la simulazione.");
        System.out.println("Più lo spazio è grande, meno spesso avverranno i contatti tra le persone!\n");
        System.out.print("Larghezza: (>2, valore consigliato "+(int)Math.sqrt(config.populationNumber)*2+"): ");
        int width = getInput(Integer.class);
        System.out.print("Altezza: ("+ Math.max(2, (config.populationNumber/10)/width) + "<x<" +((config.populationNumber*10)/width)+ ", valore consigliato "+ (4*config.populationNumber/width)+"): ");
        int height = getInput(Integer.class);

        while(width*height > config.populationNumber*4 | width<2 | height <2) {
            System.out.println("Parametri non validi. Reinserirli.");
            System.out.print("Larghezza: (>2, valore consigliato "+(int)Math.sqrt(config.populationNumber)*2+"): ");
            width = getInput(Integer.class);

            System.out.print("Altezza: ("+ Math.max(2, (config.populationNumber/10)/width) + "<x<" +((config.populationNumber*10)/width)+ ", valore consigliato "+ (4*config.populationNumber/width)+"): ");
            height = getInput(Integer.class);
        }
        config.size = new Pair<>(width, height);
        System.out.println();
        //TODO: DA QUI IN POI RENDERE OPZIONALI
        /*
        // Età massima
        System.out.print("Inserire l'eta' massima (50<x<110): ");
        int maxAge = getInput(Integer.class);
        while(maxAge < 50 || maxAge > 110) {
            System.out.print("Eta' non valida. Reinserirla (50<x<110): ");
            maxAge = getInput(Integer.class);
        }
        config.maxAge = maxAge;
        // Età media
        clear();
        ScreenFI();
        System.out.print("Inserire l'eta' media (20<x<80): ");
        int ageAverage = getInput(Integer.class);
        while(ageAverage < 20 || ageAverage > 80) {
            System.out.print("Eta' non valida. Reinserirla (20<x<80): ");
            ageAverage = getInput(Integer.class);
        }
        config.ageAverage = ageAverage;
        */

        /*
        // Altezza e Larghezza
        clear();
        ScreenFI();
        System.out.println("Inserire l'altezza e la larghezza dello spazio grafico dove verrà visualizzata la simulazione");
        System.out.println("(Valore consigliato: "+(int)Math.sqrt(config.populationNumber*2)+")");
        System.out.println("Il prodotto devve essere compreso tra "+(config.populationNumber*10)+" e "+(config.populationNumber/10)+")");
        System.out.print("Larghezza: "); int width = Integer.parseInt(Input.nextLine());
        System.out.print("Altezza: "); int heigth = Integer.parseInt(Input.nextLine());
        while((width*heigth > config.populationNumber*10)||(width*heigth < config.populationNumber/10)) {
            clear();
            ScreenFI();
            System.out.println("Parametri non validi. Reinserirli:");
            System.out.println("(Valore consigliati: "+(int)Math.sqrt(config.populationNumber*2)+")");
            System.out.println("Il prodotto delle due grandezze deve essere compreso tra "+(config.populationNumber*10)+" e "+(config.populationNumber/10)+")");
            System.out.print("Larghezza: "); width = Integer.parseInt(Input.nextLine());
            System.out.print("Altezza: "); heigth = Integer.parseInt(Input.nextLine());

        }
        // config.size[0] = width;  config.size[1] = heigth;   TOGLIERE IL COMMENTO

        */
    }

    @Override
    public int settings(Config config) { 
        Scanner Input = new Scanner(System.in);

        clear();
        ScreenSET(config);
        int action = -1;

        while(action!=0) {
            clear();
            ScreenSET(config);
            action = Integer.parseInt(Input.nextLine());
            switch(action) {
                case(1):
                clear();
                ScreenSET(config);
                // Popolazione iniziale

                System.out.print("Inserire il numero della popolazione iniziale (max 100000):");
                int populationNumber = Integer.parseInt(Input.nextLine());
                boolean result = config.setPopulationNumber(populationNumber);
                while(!result) {
                    clear();
                    ScreenSET(config);
                    System.out.print("Numero troppo grande. Reinserirlo: ");
                    populationNumber = Integer.parseInt(Input.nextLine());
                    result = config.setPopulationNumber(populationNumber);
                }
                config.populationNumber = populationNumber;

                // settando un nuovo valore per la popolazione iniziale controllo se 
                // il valore delle risorse inziali sia ancora buono
                if(config.initialResources > config.populationNumber * config.diseaseDuration) {
                    clear();
                    ScreenSET(config);
                    System.out.println("ATTENZIONE! il numero delle risorse iniziali non è più valido:");
                    System.out.println("Inserire il numero delle risorse iniziali:");
                    System.out.println("(deve essere minore di "+(config.populationNumber*config.diseaseDuration)+")");
                    int initialResource = Integer.parseInt(Input.nextLine());
                    while(initialResource >= config.populationNumber * config.diseaseDuration) {
                        clear();
                        ScreenSET(config);
                        System.out.print("Numero troppo grande. Reinserirlo: ");
                        System.out.println("(deve essere minore di "+(config.populationNumber*config.diseaseDuration)+")");
                        initialResource = Integer.parseInt(Input.nextLine());  
                    }
                    config.initialResources = initialResource;
                }
                // controllo se il valore del costo del tampone sia ancora buono
                if(config.swabsCost > config.initialResources/(config.populationNumber * 10)) {
                    clear();
                    ScreenSET(config);
                    System.out.println("ATTENZIONE! il costo del tampone non è più valido:");
                    System.out.println("Inserire il costo del tampone:");
                    System.out.println("(deve essere maggiore di "+(config.initialResources/(config.populationNumber*10))+")");
                    int swabsCost = Integer.parseInt(Input.nextLine());
                    while(swabsCost <= config.initialResources/(config.populationNumber * 10)) {
                        clear();
                        ScreenSET(config);
                        System.out.println("Numero troppo piccolo. Reinserirlo:");
                        System.out.println("(deve essere maggiore di "+(config.initialResources/(config.populationNumber*10))+")");
                        swabsCost = Integer.parseInt(Input.nextLine());  
                    }
                    config.swabsCost = swabsCost;
                }
                // e controllo se i valori di altezza e larghezza siano ancora validi
/* TOGLIERE IL COMMENTO
                if((config.size[0]*config.size[1] > config.populationNumber*10)||(config.size[0]*config.size[1] < config.populationNumber/10)) {
                    clear();
                    ScreenSET();
                    System.out.println("ATTENZIONE! I valori di altezza e larghezza non sono piu' validi");
                    System.out.println("Inserire l'altezza e la larghezza dello spazio grafico dove verrà visualizzata la simulazione");
                    System.out.println("(Valore consigliati: "+(int)Math.sqrt(config.populationNumber*2)+")");
                    System.out.println("Il prodotto devve essere compreso tra "+(config.populationNumber*10)+" e "+(config.populationNumber/10)+")");
                    System.out.print("Larghezza: "); int width = Integer.parseInt(Input.nextLine());
                    System.out.print("Altezza: "); int heigth = Integer.parseInt(Input.nextLine());
                    while((width*heigth > config.populationNumber*10)||(width*heigth < config.populationNumber/10)) {
                        clear();
                        ScreenSET();
                        System.out.println("Parametri non validi. Reinserirli:");
                        System.out.println("(Valore consigliati: "+(int)Math.sqrt(config.populationNumber*2)+")");
                        System.out.println("Il prodotto delle due grandezze deve essere compreso tra "+(config.populationNumber*10)+" e "+(config.populationNumber/10)+")");
                        System.out.print("Larghezza: "); width = Integer.parseInt(Input.nextLine());
                        System.out.print("Altezza: "); heigth = Integer.parseInt(Input.nextLine());

                    }
                    // config.size[0] = width;  config.size[1] = heigth;      
                }*/
                break;
            

                case(2):
                // Durata della malattia
                clear();
                ScreenSET(config);
                System.out.println("Inserire la durata della malattia (max 90 giorni):");
                int diseaseDuration = Integer.parseInt(Input.nextLine());
                while(diseaseDuration > 90) {
                    clear();
                    ScreenSET(config);
                    System.out.println("Numero troppo grande. Reinserirlo:");
                    diseaseDuration = Integer.parseInt(Input.nextLine());  
                }
                config.diseaseDuration = diseaseDuration;
                config.incubationToYellowDeadline = (int) (diseaseDuration * Config.INCUBATION_TO_YELLOW_DEADLINE);
                config.yellowToRedDeadline = (int) (diseaseDuration * Config.YELLOW_TO_RED_DEADLINE);
                // cambiando la durata della malattia controllo se il numero delle risorse iniziali
                // sia ancora buono
                if(config.initialResources > config.populationNumber * config.diseaseDuration) {
                    clear();
                    ScreenSET(config);
                    System.out.println("ATTENZIONE! il numero delle risorse iniziali non è più valido:");
                    System.out.println("Inserire il numero delle risorse iniziali:");
                    System.out.println("(deve essere minore di "+(config.populationNumber*config.diseaseDuration)+")");
                    int initialResource = Integer.parseInt(Input.nextLine());
                    while(initialResource >= config.populationNumber * config.diseaseDuration) {
                        clear();
                        ScreenSET(config);
                        System.out.println("Numero troppo grande. Reinserirlo:");
                        System.out.println("(deve essere minore di "+(config.populationNumber*config.diseaseDuration)+")");
                        initialResource = Integer.parseInt(Input.nextLine());
                    }
                    config.initialResources = initialResource;
                }
                break;


                case(3):
                // Risorse iniziali
                clear();
                ScreenSET(config);
                System.out.println("Inserire il numero delle risorse iniziali:");
                System.out.println("(deve essere minore di "+(config.populationNumber*config.diseaseDuration)+")");
                int initialResource = Integer.parseInt(Input.nextLine());
                while(initialResource >= config.populationNumber * config.diseaseDuration) {
                    clear();
                    ScreenSET(config);
                    System.out.println("Numero troppo grande. Reinserirlo:");
                    System.out.println("(deve essere minore di "+(config.populationNumber*config.diseaseDuration)+")");
                    initialResource = Integer.parseInt(Input.nextLine());
                }
                config.initialResources = initialResource;
                // cambiando il numero delle risorse iniziali controllo se il costo del tampone sia ancora buono
                if(config.swabsCost > config.initialResources/(config.populationNumber * 10)) {
                    clear();
                    ScreenSET(config);
                    System.out.println("ATTENZIONE! il costo del tampone non è più valido:");
                    System.out.println("Inserire il costo del tampone:");
                    System.out.println("(deve essere maggiore di "+(config.initialResources/(config.populationNumber*10))+")");
                    int swabsCost = Integer.parseInt(Input.nextLine());
                    while(swabsCost <= config.initialResources/(config.populationNumber * 10)) {
                        clear();
                        ScreenSET(config);
                        System.out.println("Numero troppo piccolo. Reinserirlo:");
                        System.out.println("(deve essere maggiore di "+(config.initialResources/(config.populationNumber*10))+")");
                        swabsCost = Integer.parseInt(Input.nextLine());  
                    }
                    config.swabsCost = swabsCost;
                }
                break;


                case(4):
                // Costo del tampone
                clear();
                ScreenSET(config);
                System.out.println("Inserire il costo del tampone:");
                System.out.println("(deve essere maggiore di "+(config.initialResources/(config.populationNumber*10))+")");
                int swabsCost = Integer.parseInt(Input.nextLine());
                while(swabsCost <= config.initialResources/(config.populationNumber * 10)) {
                    clear();
                    ScreenSET(config);
                    System.out.println("Numero troppo piccolo. Reinserirlo:");
                    System.out.println("(deve essere maggiore di "+(config.initialResources/(config.populationNumber*10))+")");
                    swabsCost = Integer.parseInt(Input.nextLine());  
                }
                config.swabsCost = swabsCost;
                break;


                case(5):
                // Infettività
                clear();
                ScreenSET(config);
                System.out.println("Inserire la percentuale di infettivita' :");
                System.out.println("(deve essere maggiore di 0)");
                Integer infectivity = Integer.parseInt(Input.nextLine());
                while(infectivity <= 0 || infectivity > 100) {
                    clear();
                    ScreenSET(config);
                    System.out.println("Percentuale non valida. Reinserirla:");
                    System.out.println("(deve essere maggiore di 0)");
                    infectivity = Integer.parseInt(Input.nextLine());
                }
                config.infectivity= infectivity;
                break;


                case(6):
                // Sintomaticità
                clear();
                ScreenSET(config);
                System.out.println("Inserire la percentuale di sintomaticita' :");
                System.out.println("(deve essere maggiore di 0)");
                Integer sintomaticity = Integer.parseInt(Input.nextLine());
                while(sintomaticity <= 0 || sintomaticity > 100) {
                    clear();
                    ScreenSET(config);
                    System.out.println("Percentuale non valida. Reinserirla:");
                    System.out.println("(deve essere maggiore di 0)");
                    sintomaticity = Integer.parseInt(Input.nextLine());
                }
                config.sintomaticity = sintomaticity;
                break;


                case(7):
                // Letalità
                clear();
                ScreenSET(config);
                System.out.println("Inserire la percentuale di letalita' :");
                System.out.println("(deve essere maggiore di 0)");
                Integer letality = Integer.parseInt(Input.nextLine());
                while(letality <= 0 || letality > 100) {
                    clear();
                    ScreenSET(config);
                    System.out.println("Percentuale non valida. Reinserirla:");
                    System.out.println("(deve essere maggiore di 0)");
                    letality = Integer.parseInt(Input.nextLine());
                }
                config.letality = letality;
                break;


                case(8):
                // Età massima
                clear();
                ScreenSET(config);
                System.out.println("Inserire l'eta' massima (compresa tra 50 e 100): ");
                int maxAge = Integer.parseInt(Input.nextLine());
                while(maxAge < 50 || maxAge > 100) {
                    clear();
                    ScreenSET(config);
                    System.out.println("Eta' non valida. Reinserirla (compresa tra 50 e 100): ");
                    maxAge = Integer.parseInt(Input.nextLine());  
                }
                config.maxAge = maxAge;
                break;


                case(9):
                // Età minima
                clear();
                ScreenSET(config);
                System.out.println("Inserire l'eta' media (compresa tra 20 e 80) :");
                int ageAverage = Integer.parseInt(Input.nextLine());
                while(ageAverage < 20 || ageAverage > 80) {
                    clear();
                    ScreenSET(config);
                    System.out.println("Eta' non valida. Reinserirla (compresa tra 20 e 80):");
                    ageAverage = Integer.parseInt(Input.nextLine());  
                }
                config.ageAverage = ageAverage;
                break;


                /*case(10):
                clear(); //TOGLIERE IL COMMENTO
                ScreenSET();
                System.out.println("Inserire l'altezza e la larghezza dello spazio grafico dove verrà visualizzata la simulazione");
                System.out.println("(Valore consigliati: "+(int)Math.sqrt(config.populationNumber*2)+")");
                System.out.println("Il prodotto devve essere compreso tra "+(config.populationNumber*10)+" e "+(config.populationNumber/10)+")");
                System.out.print("Larghezza: "); int width = Integer.parseInt(Input.nextLine());
                System.out.print("Altezza: "); int heigth = Integer.parseInt(Input.nextLine());
                while((width*heigth > config.populationNumber*10)||(width*heigth < config.populationNumber/10)) {
                    clear();
                    ScreenSET();
                    System.out.println("Parametri non validi. Reinserirli:");
                    System.out.println("(Valore consigliati: "+(int)Math.sqrt(config.populationNumber*2)+")");
                    System.out.println("Il prodotto delle due grandezze deve essere compreso tra "+(config.populationNumber*10)+" e "+(config.populationNumber/10)+")");
                    System.out.print("Larghezza: "); width = Integer.parseInt(Input.nextLine());
                    System.out.print("Altezza: "); heigth = Integer.parseInt(Input.nextLine());
                }
                // config.size[0] = width;  config.size[1] = heigth;
                break;
                */


                case(0):
                break;
            }
        }
        return 1; 
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
        createTotalDataset(state);
    }

    @Ready
    private void showScenarioSelectionMenu(Simulation currentSimulation){
        printPersonalizedTitle("Selezione Scenario");
        System.out.println("1) Scenario di default: " + getEnabledStatus(currentSimulation, null));
        System.out.println("2) Scenario personalizzato...\n");
        System.out.println("0) Torna indietro...");
    }

    @Ready
    private void printPersonalizedTitle (String title){
        System.out.println("######### " + title + " #########\n");
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
    private <E extends Scenario> void printScenarioIntro(Class<E> type){
        printPersonalizedTitle("Hai scelto " + type.getInfos().getName());
        System.out.println(scenario.getInfos());
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
                    printScenarioIntro(DefaultScenario.class);
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
                    PeopleMetGetsTestedScenario new_scen = new PeopleMetGetsTestedScenario();
                    printScenarioIntro(PeopleMetGetsTestedScenario.class);
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

    public interface Functional<T>{
       public abstract boolean test(T parameter);
    }
}
