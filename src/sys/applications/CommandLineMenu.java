package sys.applications;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;
/*
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.DefaultXYDataset;
*/
import sys.Config;
import sys.Core.NotImplemented;
import sys.Core.Ready;
import sys.Core.ToRevise;
import sys.Simulation;
import sys.State;
import sys.applications.scenarios.CustomScenario;
import sys.applications.scenarios.DefaultScenario;
import sys.applications.scenarios.PeopleGetStoppedOnceScenario;
import sys.applications.scenarios.PeopleMetGetsTestedScenario;
import sys.applications.scenarios.RandomSwabsScenario;
import sys.applications.scenarios.ScenarioInfos;
import sys.applications.scenarios.StopRandomPeopleScenario;
import sys.models.IMenu;
import sys.models.Scenario;


public class CommandLineMenu implements IMenu {
    private Scanner input = new Scanner(System.in);
    private DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.ITALIAN);

    /**
     * Crea dei grafici che mostrano la quantità di persone infette,
     * sintomatiche, morte e la quantità di tamponi. In base al valore
     * value passato come parametro, si può decidere se stampare i dati
     * che rappresentano i valori totali o le differenze tra i giorni prima
     * e quelli successivi (essenzialmente le derivate).
     *
     * @param currentState  stato corrente della simulazione
     * @param value         indice per la scelta del grafico da stampare
     */
    public void createDataset(State currentState, int value) {
        ArrayList<ArrayList<Integer>> timeRange;
        File file;
        if (value == 0) {// total
            timeRange = currentState.total;
            file = new File(Config.OUTPUT_TOTAL_FILE);
        } else {//daily
            timeRange = currentState.daily;
            file = new File(Config.OUTPUT_DAILY_FILE);
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
        for(int i=0; i<50; i++) System.out.println();
    }

    public void ScreenSH() {
        clear();
        printPersonalizedTitle("Menù Principale");
        System.out.println("1) Inizia la simulazione");
        System.out.println("2) Menu' parametri principali...");
        System.out.println("3) Menu' parametri opzionali...");
        System.out.println("4) Menu' scenari...");
        System.out.println("\n0) Esci");

    }

    public void ScreenFI() {
        clear();
        printPersonalizedTitle("Configurazione Iniziale");
    }

    public void ScreenSET(Config config) {
        clear();
        printPersonalizedTitle("Menù Parametri");
        System.out.println("Per modificare i parametri inziali digita: ");
        System.out.println("1) Popolazione inziale:\t\t\t" + format(config.getPopulationNumber()) + " valore attuale;");
        System.out.println("2) Durata malattia:\t\t\t\t" + format(config.getDiseaseDuration()) + " valore attuale;");
        System.out.println("3) Risorse inziali:\t\t\t\t" + format(config.getInitialResources()) + " valore attuale;");
        System.out.println("4) Costo del tampone:\t\t\t" + format(config.getSwabsCost()) + " valore attuale;");
        System.out.println("5) Infettivita':\t\t\t\t" + config.getInfectivity() + " valore attuale;");
        System.out.println("6) Sintomaticita':\t\t\t\t" + config.getSintomaticity() + " valore attuale;");
        System.out.println("7) Letalita':\t\t\t\t\t" + config.getLetality() + " valore attuale;");
        System.out.println("8) Spazio della simulazione:\t(" + config.getSize()[0] +"," + config.getSize()[1]+ ") valore attuale;");
        System.out.println("9) Per settare i paramteri opzionali ...");
        System.out.println("\n0) Torna al menù principale...");
    }

    public void ScreenSET_OP(Config config) {
        clear();
        printPersonalizedTitle("Menù Parametri opzionali");
        System.out.println("Per modificare i parametri opzionali digita: ");
        System.out.println("1) Eta' massima:\t\t\t\t" + format(config.getMaxAge()) + " valore attuale;");
        System.out.println("2) Eta' media:\t\t\t\t\t" + format(config.getAgeAverage()) + " valore attuale;");
        System.out.println("3) Velocità:\t\t\t\t\t" + config.getVelocity() + " valore attuale;");
        System.out.println("4) Frame per ogni giorno:\t\t" + format(config.getFrameADay()) + " valore attuale;");
        System.out.println("5) Per settare i parametri principali ...");
        System.out.println("\n0) Torna al menù principale...");
    }

    
    public void ScreenSEN(Simulation currentSimulation) {
        clear();
        printPersonalizedTitle("Selezione Scenario");
        System.out.println("1) Scenario di default: " + getEnabledStatus(currentSimulation, DefaultScenario.SCENARIO_INFOS.getID()));
        System.out.println("2) Scenario personalizzato...");
        System.out.println("\n0) Torna indietro...");
    }
 
    @Ready
    private void configurationScreen(String key,Config config) {
       switch(key) {
           case "SET_OP":
           ScreenSET_OP(config);
           break;

           case "SET":
           ScreenSET(config);
           break;

           case "FI":
           config = null;
           ScreenFI();
           break;
       }
    }  

    private String format(int value){
       return formatter.format(value);
    }

    @Ready
    private void printPersonalizedTitle (String title){
       System.out.println("######### " + title + " #########\n");
    }


    @Override
    public int show() {
        ScreenSH();
        System.out.print("\nInserisci un valore: ");
        int action = getInput(Integer.class);
        while(action < 0 || action > 4) {
            ScreenSH();
            System.out.print("Inserisci un valore: ");
            action = getInput(Integer.class);
        }
        return action+1;
    }

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
    }

    private <E extends Number> E inputUntilValid(E lowerBound, E upperBound, Class<E> type, String infos){
        String formattedLowerBound;
        String formattedUpperBound;
        if (lowerBound instanceof Integer) {
            formattedLowerBound = format(lowerBound.intValue());
            formattedUpperBound = format(upperBound.intValue());
        } else {
            formattedLowerBound = lowerBound.toString();
            formattedUpperBound = upperBound.toString();
        }

        System.out.print(infos + " ("+formattedLowerBound+"<x<=" + formattedUpperBound + "): ");
        E value = getInput(type);
        while(value.doubleValue()<=lowerBound.doubleValue() || value.doubleValue()>upperBound.doubleValue()) {
            System.out.print("\nNumero non valido. Reinserirlo ("+ formattedLowerBound +"<x=<"+formattedUpperBound+ "): ");
            value = getInput(type);
        }
        return value;
    }

    @Override
    public void firstInput(Config config) {
        // Popolazione iniziale
        inputUntilValid(config::setPopulationNumber, Integer.class, "Numero non valido. Reinserirlo (<= "+format(Config.POPULATION_NUMBER_UPPER_BOUND)+"): ", "Inserire il numero della popolazione iniziale (<= "+ format(Config.POPULATION_NUMBER_UPPER_BOUND) +"): ", "FI", config);

        // Durata della malattia
        inputUntilValid(config::setDiseaseDuration, Integer.class, "Numero non valido. Reinserirlo ("+ format(Config.DISEASE_DURATION_LOWER_BOUND) +" < x < "+ format(Config.DISEASE_DURATION_UPPER_BOUND) +"): ", "Inserire la durata della malattia in giorni ("+ format(Config.DISEASE_DURATION_LOWER_BOUND) +" < x < "+ format(Config.DISEASE_DURATION_UPPER_BOUND) +"): ", "FI", config);

        // Risorse iniziali
        inputUntilValid(config::setInitialResources, Integer.class, "Numero non valido. Reinserirlo ("+ format(Config.RESOURCES_LOWER_BOUND) + " < x < " +format(config.getPopulationNumber()*config.getDiseaseDuration())+ "): ", "Inserire il numero delle risorse iniziali (< " +format(config.getPopulationNumber()*config.getDiseaseDuration())+ "): ", "FI", config);

        // Costo del tampone
        inputUntilValid(config::setSwabsCost, Integer.class, "Numero troppo piccolo. Reinserirlo (> " + (config.getInitialResources()/(config.getPopulationNumber()*10)) +"): ", "Inserire il costo del tampone (> "+ (config.getInitialResources()/(config.getPopulationNumber()*10)) +"): ", "FI", config);

        // Infettività
        inputUntilValid(config::setInfectivity, Integer.class, "Percentuale non valida. Reinserirla (0 <= x < 100): ", "Inserire la percentuale di infettivita' (0 <= x < 100): ", "FI", config);

        // Sintomaticità
        inputUntilValid(config::setSintomaticity, Integer.class, "Percentuale non valida. Reinserirla (0 <= x < 100): ", "Inserire la percentuale di sintomaticita' (0 <= x < 100): ", "FI", config);

        // Letalità
        inputUntilValid(config::setLetality, Integer.class, "Percentuale non valida. Reinserirla (0 <= x < 100): ", "Inserire la percentuale di letalita' (0 <= x < 100): ", "FI", config);

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

        configurationScreen("FI", config);
        boolean result = askConfirmation("Settare anche i valori opzionali?");
       
        if (result) {
            // Età massima
            inputUntilValid(config::setMaxAge, Integer.class, "Eta' non valida. Reinserirla "+ Config.MAX_AGE_LOWER_BOUND +"< x <"+ Config.MAX_AGE_UPPER_BOUND +": ", "Inserire l'eta' massima (" +Config.MAX_AGE_LOWER_BOUND+ "< x < " + Config.MAX_AGE_UPPER_BOUND +"): ", "FI", config);
            // Età media
            inputUntilValid(config::setAgeAverage, Integer.class, "Eta' non valida. Reinserirla ("+ Config.AGE_AVERAGE_LOWER_BOUND +"< x <"+ Config.AGE_AVERAGE_UPPER_BOUND +"): ", "Inserire l'eta' media (" +Config.AGE_AVERAGE_LOWER_BOUND+ "< x < " + Config.AGE_AVERAGE_UPPER_BOUND +"): ","FI", config);
        }

    }

    @Override
    public int settings(Config config) {
        int action = -1;

        while(action!=0 && action!=9) {
            configurationScreen("SET", config);
            action = getInput(Integer.class);
            switch(action) {
                case(1):
                // Popolazione iniziale
                inputUntilValid(config::setPopulationNumber, Integer.class, "Numero non valido. Reinserirlo (<= "+format(Config.POPULATION_NUMBER_UPPER_BOUND)+"): ", "Inserire il numero della popolazione iniziale (<= "+ format(Config.POPULATION_NUMBER_UPPER_BOUND) +"): ", "SET", config);
                // settando un nuovo valore per la popolazione iniziale controllo se 
                // il valore delle risorse inziali sia ancora buono
                if(!(config.setInitialResources(config.getInitialResources()))){
                    inputUntilValid(config::setInitialResources, Integer.class, "Numero non valido. Reinserirlo ("+ format(Config.RESOURCES_LOWER_BOUND) + " < x < " +format(config.getPopulationNumber()*config.getDiseaseDuration())+ "): ", "Inserire il numero delle risorse iniziali (< " +format(config.getPopulationNumber()*config.getDiseaseDuration())+ "): ", "SET", config);
                }
                // controllo se il valore del costo del tampone sia ancora buono
                if(!(config.setSwabsCost(config.getSwabsCost()))){
                    inputUntilValid(config::setSwabsCost, Integer.class, "Numero troppo piccolo. Reinserirlo (> " + format(config.getInitialResources()/(config.getPopulationNumber()*10)) +"): ", "Inserire il costo del tampone (> "+ format(config.getInitialResources()/(config.getPopulationNumber()*10)) +"): ", "SET", config);
                }
                break;
            
                case(2):
                // Durata della malattia
                inputUntilValid(config::setDiseaseDuration, Integer.class, "Numero non valido. Reinserirlo (<= " + Config.DISEASE_DURATION_UPPER_BOUND + "): ", "Inserire la durata della malattia in giorni ("+ Config.DISEASE_DURATION_LOWER_BOUND+" < x < "+ Config.DISEASE_DURATION_UPPER_BOUND +"): ", "SET", config);
                // cambiando la durata della malattia controllo se il numero delle risorse iniziali
                // sia ancora buono
                if(!(config.setInitialResources(config.getInitialResources()))){
                    inputUntilValid(config::setInitialResources, Integer.class, "Numero non valido. Reinserirlo ("+ format(Config.RESOURCES_LOWER_BOUND) + " < x < " +format(config.getPopulationNumber()*config.getDiseaseDuration())+ "): ", "Inserire il numero delle risorse iniziali (< " +format(config.getPopulationNumber()*config.getDiseaseDuration())+ "): ", "SET", config);
                }
                break;

                case(3):
                // Risorse iniziali
                inputUntilValid(config::setInitialResources, Integer.class, "Numero non valido. Reinserirlo ("+ format(Config.RESOURCES_LOWER_BOUND) + " < x < " + format(config.getPopulationNumber()*config.getDiseaseDuration())+ "): ", "Inserire il numero delle risorse iniziali (< " + format(config.getPopulationNumber()*config.getDiseaseDuration())+ "): ", "SET", config);
                // cambiando il numero delle risorse iniziali controllo se il costo del tampone sia ancora buono
                if(!(config.setSwabsCost(config.getSwabsCost()))){
                    inputUntilValid(config::setSwabsCost, Integer.class, "Numero troppo piccolo. Reinserirlo (> " + (config.getInitialResources()/(config.getPopulationNumber()*10)) +"): ", "Inserire il costo del tampone (> "+ (config.getInitialResources()/(config.getPopulationNumber()*10)) +"): ", "SET", config);
                }
                break;

                case(4):
                // Costo del tampone
                inputUntilValid(config::setSwabsCost, Integer.class, "Numero troppo piccolo. Reinserirlo (> " + (config.getInitialResources()/(config.getPopulationNumber()*10)) +"): ", "Inserire il costo del tampone (> "+ (config.getInitialResources()/(config.getPopulationNumber()*10)) +"): ", "SET", config);
                break;

                case(5):
                // Infettività
                inputUntilValid(config::setInfectivity, Integer.class, "Percentuale non valida. Reinserirla (0 <= x < 100): ", "Inserire la percentuale di infettivita' (0 <= x < 100): ", "SET", config);
                break;

                case(6):
                // Sintomaticità
                inputUntilValid(config::setSintomaticity, Integer.class, "Percentuale non valida. Reinserirla (0 <= x < 100): ", "Inserire la percentuale di sintomaticita' (0 <= x < 100): ", "SET", config);
                break;

                case(7):
                // Letalità
                inputUntilValid(config::setLetality, Integer.class, "Percentuale non valida. Reinserirla (0 <= x < 100): ", "Inserire la percentuale di letalita' (0 <= x < 100): ", "SET", config);
                break;

                case(8):
                configurationScreen("SET", config);
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
 
        while(action!=0 && action!=5) {
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
                inputUntilValid(config::setVelocity, Integer.class, "Numero non valido. Reinserirlo("+ Config.VELOCITY_LOWER_BOUND +"<= x <"+ config.VELOCITY_UPPER_BOUND.getAsInt() +")", "Inserire la velocità("+ Config.VELOCITY_LOWER_BOUND +"< x <"+ config.VELOCITY_UPPER_BOUND.getAsInt() +"): ", "SET_OP", config);
                break;

                case(4):
                // Frame per ogni giorno
                inputUntilValid(config::setFrameADay, Integer.class, "Numero non valido. Reinserirlo("+ Config.FRAME_A_DAY_LOWER_BOUND +"< x <"+ config.FRAME_A_DAY_UPPER_BOUND +")", "Inserire il numero di frame per ogni giorno("+ Config.FRAME_A_DAY_LOWER_BOUND +"< x <"+ config.FRAME_A_DAY_UPPER_BOUND +"): ", "SET_OP", config);
                break;

                case(5): break;
                case(0): break;
            }
        }
        if(action==5) return 3;
        return action;   
    }

    @Override
    public void feedback(State state){
        printPersonalizedTitle("GIORNO " + state.currentDay);
        System.out.println("Paziente zero "+ (!state.unoPatientFound?"NON ANCORA ":"") +"trovato!");
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
        //createDataset(state, 0);
        //createDataset(state,1 );
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
    private void customScenarioMenu(Simulation currentSimulation){
       printPersonalizedTitle("Custom Scenario");
       System.out.println("1) Scenario 'Tampone alle persone incontrate da chi risulta positivo': \t\t" + getEnabledStatus(currentSimulation, PeopleMetGetsTestedScenario.SCENARIO_INFOS.getID()));
       System.out.println("2) Scenario 'Persone fermate a caso all'inizio': \t\t\t\t\t\t\t" + getEnabledStatus(currentSimulation, PeopleGetStoppedOnceScenario.SCENARIO_INFOS.getID()));
       System.out.println("3) Scenario 'Persone fermate a caso periodicamente': \t\t\t\t\t\t" + getEnabledStatus(currentSimulation, StopRandomPeopleScenario.SCENARIO_INFOS.getID()));
       System.out.println("4) Scenario 'Tamponi a persone a caso': \t\t\t\t\t\t\t\t\t" + getEnabledStatus(currentSimulation, RandomSwabsScenario.SCENARIO_INFOS.getID()));
       System.out.println("\n0) Torna indietro...");
    }

    @ToRevise
    private String getEnabledStatus(Simulation currentSimulation, Integer ID){
        final String positiveFeedback = "abilitato.";
        final String negativeFeedback = "disabilitato.";
        return currentSimulation.isScenarioEnabled(ID) ? positiveFeedback : negativeFeedback;
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
        System.out.println();
        printPersonalizedTitle("Hai scelto " + infos.getName());
        System.out.println(infos.getInfos());
        System.out.println();
    }

    /**
     * Questo metodo era stato inizialmente pensato per consentire di abilitare
     * o disabilitare gli scenari. Per una serie di motivi, questo metodo viene
     * utilizzato soltanto per consentire l'abilitazione o la disabilitazione
     * dello scenario di default. Nonostante ciò, abbiamo preferito lasciarlo
     * nel codice nella sua completezza come reliquia dell'evoluzione di questa classe.
     *
     * @param simulation    la simulazione attiva
     * @param scenario      lo scenario attivo
     * @return              lo scenario attivato
     */
    @ToRevise
    private Scenario enableDisable(Simulation simulation, Scenario scenario){
        boolean response;

        if (simulation.isScenarioEnabled(scenario.getInfos().getID())) {
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
        ScreenSEN(simulation);
        return scenario;
    }

    @ToRevise
    @Override
    public void selectScenario(Simulation simulation) {
        ScreenSEN(simulation);
        DefaultScenario defaultScenario = new DefaultScenario(simulation);

        int action = -1;
        while(action != 0){
            System.out.print("\nInserisci un valore: ");
            action = getInput(Integer.class);

            switch (action){
                case 1:
                    clear();
                    printScenarioIntro(DefaultScenario.SCENARIO_INFOS);
                    simulation.loadScenario(enableDisable(simulation, defaultScenario));
                    break;
                case 2:
                    makeCustomScenario(simulation);
                    clear();
                    ScreenSEN(simulation);
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Valore non valido!");
            }
        }
    }



    @ToRevise
    private void makeCustomScenario(Simulation simulation){
        CustomScenario customScenario;
        if (simulation.getCurrentScenario() instanceof CustomScenario) customScenario = (CustomScenario) simulation.getCurrentScenario();
        else customScenario = new CustomScenario(simulation);


        Integer percentage;
        Integer duration;
        String confirmMessage = "Vuoi confermare le modifiche?";
        int action = -1;

        while(action != 0){
            clear();
            customScenarioMenu(simulation);
            System.out.print("Digita il numero dello scenario da attivare: ");
            action = getInput(Integer.class);

            switch(action) {
                case 1:
                    printScenarioIntro(PeopleMetGetsTestedScenario.SCENARIO_INFOS);
                    percentage = inputUntilValid(0,100, Integer.class, "Inserisci la possibilità che una persona incontrata da un positivo venga testata");
                    if (askConfirmation(confirmMessage)) {
                        customScenario.addScenario(new PeopleMetGetsTestedScenario(simulation, percentage));
                        simulation.loadScenario(customScenario);
                    }
                    break;
                case 2:
                    printScenarioIntro(PeopleGetStoppedOnceScenario.SCENARIO_INFOS);
                    percentage = inputUntilValid(0,  simulation.getConfigs().getPopulationNumber(), Integer.class, "Inserisci le persone da fermare");
                    duration = inputUntilValid(PeopleGetStoppedOnceScenario.DURATION_LOWER_BOUND,  PeopleGetStoppedOnceScenario.DURATION_UPPER_BOUND, Integer.class, "Inserisci per quanti giorni queste persone dovranno essere fermate");
                    if (askConfirmation(confirmMessage)) {
                        customScenario.addScenario(new PeopleGetStoppedOnceScenario(simulation, percentage, duration));
                        simulation.loadScenario(customScenario);
                    }
                    break;
                case 3:
                    printScenarioIntro(StopRandomPeopleScenario.SCENARIO_INFOS);
                    percentage = inputUntilValid(0, simulation.getConfigs().getPopulationNumber(), Integer.class, "Inserisci le persone da fermare");
                    duration = inputUntilValid(StopRandomPeopleScenario.DURATION_LOWER_BOUND, StopRandomPeopleScenario.DURATION_UPPER_BOUND, Integer.class, "Inserisci per quanti giorni queste persone dovranno essere fermate");
                    Integer ratio = inputUntilValid(StopRandomPeopleScenario.RATIO_LOWER_BOUND, StopRandomPeopleScenario.RATIO_UPPER_BOUND, Integer.class, "Inserisci ogni quanti giorni verranno fermate persone casualmente");
                    if (askConfirmation(confirmMessage)) {
                        customScenario.addScenario(new StopRandomPeopleScenario(simulation, percentage, duration, ratio));
                        simulation.loadScenario(customScenario);
                    }
                    break;
                case 4:
                    printScenarioIntro(RandomSwabsScenario.SCENARIO_INFOS);
                    Integer swabs = inputUntilValid(0, simulation.getConfigs().getPopulationNumber(), Integer.class, "Inserisci quanti tamponi effettuare al giorno");
                    if (askConfirmation(confirmMessage)) {
                        customScenario.addScenario(new RandomSwabsScenario(simulation, swabs));
                        simulation.loadScenario(customScenario);
                    }
                    break;
                case 0:
                    action = 0;
                    break;
                default:
                    System.out.println("Valore non valido!");
                    break;
            }
        }
    }

    
    public static void main(String[] args) {
        CommandLineMenu menu = new CommandLineMenu();
        Config config = new Config();
        Simulation S = new Simulation(menu);
        try {
            S.run();
        }catch(InterruptedException e) {}
        
    }
}
