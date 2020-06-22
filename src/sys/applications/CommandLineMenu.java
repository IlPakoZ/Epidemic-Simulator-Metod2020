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
import java.lang.reflect.Method;
import java.util.function.IntPredicate;

public class CommandLineMenu implements IMenu {
    private Scanner input = new Scanner(System.in);
    private DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.ITALIAN);
    

    public void clear() {
        for(int i=0; i<50; i++) System.out.println();
    }

    public void ScreenSH() {
        clear();
        printPersonalizedTitle("Menù Principale");
        System.out.println("1) Inizia la simulazione");
        System.out.println("2) Menu' parametri principali...");
        System.out.println("3) Menu' parametri opzionali...");
        System.out.println("3) Menu' scenari...");
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

           /*
           case "SEN":    //Non dovrebbe servire.
           ScreenSEN();
           break; */
       }
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
    public int show() {
        ScreenSH();
        int action = getInput(Integer.class);
        while(action < 0 || action > 3) {
            ScreenSH();
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

    @Override
    public void firstInput(Config config) {
        // Popolazione iniziale
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
        
        //TODO: DA QUI IN POI RENDERE OPZIONALI
        configurationScreen("FI", config);
        boolean result = askConfirmation("Settare anche i valori ozpionali?");
       
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
                break;
            
                case(2):
                // Durata della malattia
                inputUntilValid(config::setDiseaseDuration, Integer.class, "Numero non valido. Reinserirlo (<= " + Config.DISEASE_DURATION_UPPER_BOUND + "): ", "Inserire la durata della malattia in giorni ("+ Config.DISEASE_DURATION_LOWER_BOUND+" < x < "+ Config.DISEASE_DURATION_UPPER_BOUND +"): ", "SET", config);
                // cambiando la durata della malattia controllo se il numero delle risorse iniziali
                // sia ancora buono
                if(!(config.setInitialResources(config.getInitialResources()))){
                    inputUntilValid(config::setInitialResources, Integer.class, "Numero non valido. Reinserirlo ("+ Config.RESOURCES_LOWER_BOUND + " < x < " +(config.getPopulationNumber()*config.getDiseaseDuration())+ "): ", "Inserire il numero delle risorse iniziali (< " +(config.getPopulationNumber()*config.getDiseaseDuration())+ "): ", "SET", config);
                }
                break;

                case(3):
                // Risorse iniziali
                inputUntilValid(config::setInitialResources, Integer.class, "Numero non valido. Reinserirlo ("+ Config.RESOURCES_LOWER_BOUND + " < x < " +(config.getPopulationNumber()*config.getDiseaseDuration())+ "): ", "Inserire il numero delle risorse iniziali (< " +(config.getPopulationNumber()*config.getDiseaseDuration())+ "): ", "SET", config);
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
                inputUntilValid(config::setInfectivity, Integer.class, "Percentuale non valida. Reinserirla (0 <= x < 100): ", "Inserire la percentuale di infettivita' (0 <= x < 100): ", "FI", config);
                break;

                case(6):
                // Sintomaticità
                inputUntilValid(config::setSintomaticity, Integer.class, "Percentuale non valida. Reinserirla (0 <= x < 100): ", "Inserire la percentuale di sintomaticita' (0 <= x < 100): ", "FI", config);
                break;

                case(7):
                // Letalità
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

    /*
    public static void main(String[] args) {
        CommandLineMenu c = new CommandLineMenu();
        Config config = new Config();
        c.firstInput(config);
        c.settings(config);
       
    }*/
}
