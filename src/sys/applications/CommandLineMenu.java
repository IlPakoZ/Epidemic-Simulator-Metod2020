package sys.applications;
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
import sys.models.IMenu;
import sys.models.Scenario;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.lang.Math;

public class CommandLineMenu implements IMenu {
    private Scanner input = new Scanner(System.in);

    public void createDataset(State currentState){
        double[] x1 = new double[currentState.totalInfected.size()];
        double[] y1 = new double[currentState.totalInfected.size()];

        for (int i=0; i<y1.length; i++) {
            y1[i] = currentState.totalInfected.get(i);
            x1[i] = i;
        }

        DefaultXYDataset dataset = new DefaultXYDataset();
        dataset.addSeries("Total infected", new double[][]{x1,y1});
        JFreeChart chart = ChartFactory.createXYLineChart("Infection rate", "Day passed", "Number of people", dataset);

        XYPlot plot = chart.getXYPlot();

        NumberAxis naxis = (NumberAxis) plot.getDomainAxis();
        NumberTickUnit unit = new NumberTickUnit(5);
        naxis.setTickUnit(unit);

        try {
            ChartUtils.saveChartAsPNG(new File("prova.png"), chart, 1000, 700);
        }catch (IOException ignored) {}
    }

    public void clear() {
        for(int i=0; i<50; i++) System.out.println();
    }

    public void ScreenSH() {  
        System.out.println("MENU' PRINCIPALE");
        System.out.println("#");
        System.out.println("#"); 
    }

    public void ScreenFI() {  
        System.out.println("CONFIGURAZIONE INIZIALE");
        System.out.println("#");
        System.out.println("#");   
    }

    public void ScreenSET() {  
        System.out.println("MENU' OPZIONI");
        System.out.println("#");
        System.out.println("#");
        System.out.println("Per settare i paramtetri inziali digita:");
        System.out.println("1 - Popolazione inziale");
        System.out.println("2 - Durata malattia");
        System.out.println("3 - Risorse inziali");
        System.out.println("4 - Costo del tampone");
        System.out.println("5 - Infettivita'");
        System.out.println("6 - Sintomaticita'");
        System.out.println("7 - Letalita'");
        System.out.println("8 - Eta' massima");
        System.out.println("9 - Eta' media");
        System.out.println("10 - Larghezza e Altezza dello spazio grafico della simulazione");
        System.out.println("0 - Per uscire");
    }

    
    public void ScreenSEN() {
        System.out.println("MENU' SCENARIO");
        System.out.println("#");
        System.out.println("#");
        System.out.println("Scegli una strategia per contrastare il virus:");
        System.out.println("0 - Strategia di default");
        System.out.println("1 - Tamponi casuali sulle persone ogni giorno");
        System.out.println("2 - Tamponi sui contatti delle persone risultate positive al tampone");
        System.out.println("3 - In modo casuale, vengono fermate persone per un tempo limitato");
        System.out.println("4 - Tutta la popolazione, o una parte, usa le mascherine (maggior consumo di risorse)");
    }
    

    @Override
    public int show() { 
        Scanner Input = new Scanner(System.in);

        clear();
        ScreenSH();
        System.out.println("Per far partire la simulazione digitare 0. Per configurare le impostazioni inziali digitare 2:");
        int action = getInput(Integer.class);
        while(action != 0 && action != 2) {
            clear();
            ScreenSH();
            System.out.println("Parametro non valido. Reinserirlo:");
            action = getInput(Integer.class);
        }
        return action; 
    }

    @Override
    public void firstInput(Config config) {

        clear();
        ScreenFI();
        // Popolazione iniziale
        System.out.println("Inserire il numero della popolazione iniziale (max 100000):");
        int populationNumber = getInput(Integer.class);
        while(populationNumber > 100000) {
            clear();
            ScreenFI();
            System.out.println("Numero troppo grande. Reinserirlo:");
            populationNumber = getInput(Integer.class);
        }
        config.populationNumber = populationNumber;
        // Durata della malattia
        clear();
        ScreenFI();
        System.out.println("Inserire la durata della malattia (max 90 giorni):");
        int diseaseDuration = getInput(Integer.class);
        while(diseaseDuration > 90) {
            clear();
            ScreenFI();
            System.out.println("Numero troppo grande. Reinserirlo:");
            diseaseDuration = getInput(Integer.class);
        }
        config.diseaseDuration = diseaseDuration;
        config.incubationToYellowDeadline = (int) (diseaseDuration * Config.INCUBATION_TO_YELLOW_DEADLINE);
        config.yellowToRedDeadline = (int) (diseaseDuration * Config.YELLOW_TO_RED_DEADLINE);
        // Risorse iniziali
        clear();
        ScreenFI();
        System.out.println("Inserire il numero delle risorse iniziali:");
        System.out.println("(deve essere minore di "+(config.populationNumber*config.diseaseDuration)+")");
        int initialResource = getInput(Integer.class);
        while(initialResource >= config.populationNumber * config.diseaseDuration) {
            clear();
            ScreenFI();
            System.out.println("Numero troppo grande. Reinserirlo:");
            System.out.println("(deve essere minore di "+(config.populationNumber*config.diseaseDuration)+")");
            initialResource = getInput(Integer.class);
        }
        config.initialResources = initialResource;
        // Costo del tampone
        clear();
        ScreenFI();
        System.out.println("Inserire il costo del tampone:");
        System.out.println("(deve essere maggiore di "+(config.initialResources/(config.populationNumber*10))+")");
        int swabsCost = getInput(Integer.class);
        while(swabsCost <= config.initialResources/(config.populationNumber * 10)) {
            clear();
            ScreenFI();
            System.out.println("Numero troppo piccolo. Reinserirlo:");
            System.out.println("(deve essere maggiore di "+(config.initialResources/(config.populationNumber*10))+")");
            swabsCost = getInput(Integer.class);
        }
        config.swabsCost = swabsCost;
        // Infettività
        clear();
        ScreenFI();
        System.out.println("Inserire la percentuale di infettivita' :");
        System.out.println("(deve essere maggiore di 0)");
        Integer infectivity = getInput(Integer.class);
        while(infectivity <= 0.0 || infectivity > 100.0) {
            clear();
            ScreenFI();
            System.out.println("Percentuale non valida. Reinserirla:");
            System.out.println("(deve essere maggiore di 0)");
            infectivity = getInput(Integer.class);
        }
        //config.infectivity= infectivity;
        // Sintomaticità
        clear();
        ScreenFI();
        System.out.println("Inserire la percentuale di sintomaticita' :");
        System.out.println("(deve essere maggiore di 0)");
        Integer sintomaticity = getInput(Integer.class);
        while(sintomaticity <= 0.0 || sintomaticity > 100.0) {
            clear();
            ScreenFI();
            System.out.println("Percentuale non valida. Reinserirla:");
            System.out.println("(deve essere maggiore di 0)");
            sintomaticity = getInput(Integer.class);
        }
        //config.sintomaticity = sintomaticity;
        // Letalità
        clear();
        ScreenFI();
        System.out.println("Inserire la percentuale di letalita' :");
        System.out.println("(deve essere maggiore di 0)");
        Integer letality = getInput(Integer.class);
        while(letality <= 0.0 || letality > 100.0) {
            clear();
            ScreenFI();
            System.out.println("Percentuale non valida. Reinserirla:");
            System.out.println("(deve essere maggiore di 0)");
            letality = getInput(Integer.class);
        }
        config.letality = letality;
        // Età massima
        clear();
        ScreenFI();
        System.out.println("Inserire l'eta' massima (compresa tra 50 e 100) :");
        int maxAge = getInput(Integer.class);
        while(maxAge < 50 || maxAge > 100) {
            clear();
            ScreenFI();
            System.out.println("Eta' non valida. Reinserirla (compresa tra 50 e 100):");
            maxAge = getInput(Integer.class);
        }
        config.maxAge = maxAge;
        // Età media
        clear();
        ScreenFI();
        System.out.println("Inserire l'eta' media (compresa tra 20 e 80) :");
        int ageAverage = getInput(Integer.class);
        while(ageAverage < 20 || ageAverage > 80) {
            clear();
            ScreenFI();
            System.out.println("Eta' non valida. Reinserirla (compresa tra 20 e 80):");
            ageAverage = getInput(Integer.class);
        }
        config.ageAverage = ageAverage;
        /*
        // Altezza e Larghezza
        clear();
        ScreenFI();
        System.out.println("Inserire l'altezza e la larghezza dello spazio grafico dove verrà visualizzata la simulazione");
        System.out.println("(Valore consigliati: "+(int)Math.sqrt(config.populationNumber*2)+")");
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
        ScreenSET();
        int action = -1;

        while(action!=0) {
            clear();
            ScreenSET();
            action = Integer.parseInt(Input.nextLine());
            switch(action) {
                case(1):
                clear();
                ScreenSET();
                // Popolazione iniziale

                System.out.print("Inserire il numero della popolazione iniziale (max 100000):");
                int populationNumber = Integer.parseInt(Input.nextLine());
                boolean result = config.setPopulationNumber(populationNumber);
                while(!result) {
                    clear();
                    ScreenSET();
                    System.out.print("Numero troppo grande. Reinserirlo: ");
                    populationNumber = Integer.parseInt(Input.nextLine());
                    result = config.setPopulationNumber(populationNumber);
                }
                config.populationNumber = populationNumber;

                // settando un nuovo valore per la popolazione iniziale controllo se 
                // il valore delle risorse inziali sia ancora buono
                if(config.initialResources > config.populationNumber * config.diseaseDuration) {
                    clear();
                    ScreenSET();
                    System.out.println("ATTENZIONE! il numero delle risorse iniziali non è più valido:");
                    System.out.println("Inserire il numero delle risorse iniziali:");
                    System.out.println("(deve essere minore di "+(config.populationNumber*config.diseaseDuration)+")");
                    int initialResource = Integer.parseInt(Input.nextLine());
                    while(initialResource >= config.populationNumber * config.diseaseDuration) {
                        clear();
                        ScreenSET();
                        System.out.print("Numero troppo grande. Reinserirlo: ");
                        System.out.println("(deve essere minore di "+(config.populationNumber*config.diseaseDuration)+")");
                        initialResource = Integer.parseInt(Input.nextLine());  
                    }
                    config.initialResources = initialResource;
                }
                // controllo se il valore del costo del tampone sia ancora buono
                if(config.swabsCost > config.initialResources/(config.populationNumber * 10)) {
                    clear();
                    ScreenSET();
                    System.out.println("ATTENZIONE! il costo del tampone non è più valido:");
                    System.out.println("Inserire il costo del tampone:");
                    System.out.println("(deve essere maggiore di "+(config.initialResources/(config.populationNumber*10))+")");
                    int swabsCost = Integer.parseInt(Input.nextLine());
                    while(swabsCost <= config.initialResources/(config.populationNumber * 10)) {
                        clear();
                        ScreenSET();
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
                ScreenSET();
                System.out.println("Inserire la durata della malattia (max 90 giorni):");
                int diseaseDuration = Integer.parseInt(Input.nextLine());
                while(diseaseDuration > 90) {
                    clear();
                    ScreenSET();
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
                    ScreenSET();
                    System.out.println("ATTENZIONE! il numero delle risorse iniziali non è più valido:");
                    System.out.println("Inserire il numero delle risorse iniziali:");
                    System.out.println("(deve essere minore di "+(config.populationNumber*config.diseaseDuration)+")");
                    int initialResource = Integer.parseInt(Input.nextLine());
                    while(initialResource >= config.populationNumber * config.diseaseDuration) {
                        clear();
                        ScreenSET();
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
                ScreenSET();
                System.out.println("Inserire il numero delle risorse iniziali:");
                System.out.println("(deve essere minore di "+(config.populationNumber*config.diseaseDuration)+")");
                int initialResource = Integer.parseInt(Input.nextLine());
                while(initialResource >= config.populationNumber * config.diseaseDuration) {
                    clear();
                    ScreenSET();
                    System.out.println("Numero troppo grande. Reinserirlo:");
                    System.out.println("(deve essere minore di "+(config.populationNumber*config.diseaseDuration)+")");
                    initialResource = Integer.parseInt(Input.nextLine());  
                }
                config.initialResources = initialResource;
                // cambiando il numero delle risorse iniziali controllo se il costo del tampone sia ancora buono
                if(config.swabsCost > config.initialResources/(config.populationNumber * 10)) {
                    clear();
                    ScreenSET();
                    System.out.println("ATTENZIONE! il costo del tampone non è più valido:");
                    System.out.println("Inserire il costo del tampone:");
                    System.out.println("(deve essere maggiore di "+(config.initialResources/(config.populationNumber*10))+")");
                    int swabsCost = Integer.parseInt(Input.nextLine());
                    while(swabsCost <= config.initialResources/(config.populationNumber * 10)) {
                        clear();
                        ScreenSET();
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
                ScreenSET();
                System.out.println("Inserire il costo del tampone:");
                System.out.println("(deve essere maggiore di "+(config.initialResources/(config.populationNumber*10))+")");
                int swabsCost = Integer.parseInt(Input.nextLine());
                while(swabsCost <= config.initialResources/(config.populationNumber * 10)) {
                    clear();
                    ScreenSET();
                    System.out.println("Numero troppo piccolo. Reinserirlo:");
                    System.out.println("(deve essere maggiore di "+(config.initialResources/(config.populationNumber*10))+")");
                    swabsCost = Integer.parseInt(Input.nextLine());  
                }
                config.swabsCost = swabsCost;
                break;


                case(5):
                // Infettività
                clear();
                ScreenSET();
                System.out.println("Inserire la percentuale di infettivita' :");
                System.out.println("(deve essere maggiore di 0)");
                Integer infectivity = Integer.parseInt(Input.nextLine());
                while(infectivity <= 0 || infectivity > 100) {
                    clear();
                    ScreenSET();
                    System.out.println("Percentuale non valida. Reinserirla:");
                    System.out.println("(deve essere maggiore di 0)");
                    infectivity = Integer.parseInt(Input.nextLine());
                }
                config.infectivity= infectivity;
                break;


                case(6):
                // Sintomaticità
                clear();
                ScreenSET();
                System.out.println("Inserire la percentuale di sintomaticita' :");
                System.out.println("(deve essere maggiore di 0)");
                Integer sintomaticity = Integer.parseInt(Input.nextLine());
                while(sintomaticity <= 0 || sintomaticity > 100) {
                    clear();
                    ScreenSET();
                    System.out.println("Percentuale non valida. Reinserirla:");
                    System.out.println("(deve essere maggiore di 0)");
                    sintomaticity = Integer.parseInt(Input.nextLine());
                }
                config.sintomaticity = sintomaticity;
                break;


                case(7):
                // Letalità
                clear();
                ScreenSET();
                System.out.println("Inserire la percentuale di letalita' :");
                System.out.println("(deve essere maggiore di 0)");
                Integer letality = Integer.parseInt(Input.nextLine());
                while(letality <= 0 || letality > 100) {
                    clear();
                    ScreenSET();
                    System.out.println("Percentuale non valida. Reinserirla:");
                    System.out.println("(deve essere maggiore di 0)");
                    letality = Integer.parseInt(Input.nextLine());
                }
                config.letality = letality;
                break;


                case(8):
                // Età massima
                clear();
                ScreenSET();
                System.out.println("Inserire l'eta' massima (compresa tra 50 e 100): ");
                int maxAge = Integer.parseInt(Input.nextLine());
                while(maxAge < 50 || maxAge > 100) {
                    clear();
                    ScreenSET();
                    System.out.println("Eta' non valida. Reinserirla (compresa tra 50 e 100): ");
                    maxAge = Integer.parseInt(Input.nextLine());  
                }
                config.maxAge = maxAge;
                break;


                case(9):
                // Età minima
                clear();
                ScreenSET();
                System.out.println("Inserire l'eta' media (compresa tra 20 e 80) :");
                int ageAverage = Integer.parseInt(Input.nextLine());
                while(ageAverage < 20 || ageAverage > 80) {
                    clear();
                    ScreenSET();
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
        System.out.println("GIORNO " + state.currentDay);

        if(state.unoPatientFound) System.out.println("Paziente zero trovato");
        else System.out.println("Paziente zero non ancora trovato");

        System.out.println("Popolazione sana " + state.getHealthyNumber());
        System.out.println("Popolazione guarita " + state.getCuredNumber());


        System.out.print("Popolazione infettata " + state.totalInfected.get(state.totalInfected.size()-1));
        System.out.println(" (" + state.dailyInfected.get(state.dailyInfected.size()-1) +" in più rispetto a ieri)");


        System.out.println("Morti " + state.getDeathsNumber());
        System.out.println("Risorse disponibili " + state.resources);
        System.out.println("\n\n");
    }

    @Override
    public void finalFeedback(State state) {
        System.out.println("SIMULAZIONE TERMINATA. Resoconto finale:");
        System.out.println("GIORNI PASSATI " + state.currentDay);
        System.out.println("Popolazione sana " + state.getHealthyNumber());
        System.out.println("Popolazione guarita " + state.getCuredNumber());
        System.out.println("Popolazione infettata " + state.totalInfected.get(state.totalInfected.size()-1));
        System.out.println("Morti " + state.getDeathsNumber());  
        System.out.println("Risorse rimaste " + state.resources);
        createDataset(state);
    }

    @Ready
    private void showScenarioSelectionMenu(Simulation currentSimulation){
        printPersonalizedTitle("Selezione Scenario");
        System.out.println("1) Scenario di default: " + getEnabledStatus(currentSimulation, null));
        System.out.println("2) Scenario personalizzato...\n");
        System.out.println("0) Torna indietro");
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
    private Scenario enableDisable(Simulation simulation, Scenario scenario){
        printPersonalizedTitle("Hai scelto " + scenario.getName());
        System.out.println(scenario.getInfos());
        boolean response;

        if (simulation.isScenarioEnabled(scenario)) {
            if (scenario instanceof DefaultScenario) {
                System.out.println("Lo scenario di default non può essere disabilitato.");
            } else {
                System.out.println("Lo scenario '" + scenario.getName() + "' è abilitato.");
                response = askConfirmation("Sei sicuro di voler disabilitare '" + scenario.getName() + "'?");
                if (response) {
                    System.out.println("Lo scenario '"+ scenario.getName()+"' è stato disattivato.");
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
                response = askConfirmation("Sei sicuro di voler abilitare '" + scenario.getName() + "'?");
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
    public CustomScenario makeCustomScenario(Simulation simulation){
        CustomScenario customScenario = new CustomScenario(simulation);
        int action = -1;

        while(action != 0){
            clear();
            ScreenSEN();
            customScenarioMenu();
            System.out.print("Digita il numero dello scenario da attivare: ");
            action = getInput(Integer.class);

            switch(action) {
                case 1:
                    // Scenario1 new_scen = new Scenario1()
                    // return new_scen
                    break;
                case 2:

                    // Scenario2 new_scen = new Scenario2()
                    // return new_scen
                    break;
                case 3:
                    // Scenario2 new_scen = new Scenario2()
                    // return new_scen
                    break;
                case 4:
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
}
