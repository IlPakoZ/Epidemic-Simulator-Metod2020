package sys;

import assets.ColorStatus;
import assets.Person;

import javafx.util.Pair;
import sys.Core.*;
import sys.applications.scenarios.CustomScenario;
import sys.applications.scenarios.DefaultScenario;
import sys.models.IMenu;
import sys.models.Scenario;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Simulation {

    private State currentState;
    private IMenu menu;
    private Scenario currentScenario = new DefaultScenario(this);

    /**
     * Costruttore della simulazione.
     */
    Simulation(IMenu menu){ initialize(menu); }

    /**
     * Inizializza la simulazione. Metodo di appoggio.
     */
    @ToRevise
    private void initialize(IMenu menu){
        this.menu = menu;
        currentState = new State();
        currentState.configs = new Config();
        currentState.total = new ArrayList<>();
        currentState.total.add(new ArrayList<>());
        currentState.total.add(new ArrayList<>());
        currentState.total.add(new ArrayList<>());
        currentState.total.add(new ArrayList<>());
        currentState.daily = new ArrayList<>();
        currentState.daily.add(new ArrayList<>());
        currentState.daily.add(new ArrayList<>());
        currentState.daily.add(new ArrayList<>());
        currentState.daily.add(new ArrayList<>());
        currentState.contacts = new HashMap<>();
        currentState.swabs = new HashSet<>();

    }

    /**
     * Mostra il menù principale.
     * Serve all'utente per interfacciarsi alla simulazione
     * e decidere cosa fare.
     */
    @ToRevise
    void run() throws InterruptedException {
        menu.firstInput(getConfigs());
        int state = 0;
        do {
            switch (state){
                case 0:
                    state = menu.show();
                    //Mostra il menù principale / torna al menù principale
                    break;
                case 1:
                    System.exit(0);

                case 3:
                    state = menu.settings(getConfigs());
                    //Mostra il menù delle opzioni
                    break;
                case 4:
                    currentScenario = menu.selectScenario(this);
                    state = 1;
                    //Mostra il menù di scelta dello scenario
                case 5:

                    break;
                default:
                    throw new UnsupportedOperationException();
            }
        }while(state != 2);
        currentState.startingPopulation = Rng.generatePopulation(currentState);
        currentState.startingPopulation[getConfigs().populationNumber-1].setAsInfected();
        currentState.resources = getConfigs().initialResources;
        currentScenario.oneTimeAction();
        start();
    }

    /**
     * Avvia la simulazione.
     */
    @ToRevise
    private void start() throws InterruptedException {
        currentState.status = SimulationStatus.PLAYING;
        boolean going = true;
        int frame = 0;
        Instant startTime = Instant.now();
        while(going){
            loop();
            if (frame % getConfigs().frameADay == 0){
                if (frame == 0){
                    for (ArrayList<Integer> arr: currentState.total){
                        arr.add(0);
                    }
                    for (ArrayList<Integer> arr: currentState.daily){
                        arr.add(0);
                    }
                    menu.feedback(currentState);
                }else{
                    boolean result = nextDay();
                    if (!result) going = false;

                    Instant endTime = Instant.now();
                    int duration = Duration.between(startTime, endTime).getNano()/1000000;
                    if (duration < getConfigs().dayDuration) Thread.sleep(getConfigs().dayDuration-duration);
                    startTime = Instant.now();
                }
            }
            if (currentState.unoPatientFound) currentScenario.frameAction();
            frame++;
            if (currentState.currentDay==150) going = false;
        }
        end();
    }

    /**
     * Contiene il loop di istruzioni che devono essere
     * eseguite in ripetizione. Restituisce true se il loop
     * deve continuare la sua esecuzione, altrimenti restituisce
     * false.
     *
     * @return      booleano che indica se il loop deve essere ancora eseguito
     */
    @ToRevise
    private void loop() {

        currentState.space = new PersonList[currentState.configs.size[0]][currentState.configs.size[0]];
        for (int i=currentState.incubationYellow+1;i<=currentState.redBlue;i++){
            int[] position = currentState.startingPopulation[i].nextPosition();
            if (currentState.space[position[0]][position[1]] == null){
                currentState.space[position[0]][position[1]] = new PersonList();
            }
            currentState.space[position[0]][position[1]].addElement(currentState.startingPopulation[i]);
        }

        if (currentState.redBlue - currentState.incubationYellow != 0) {
            for (int i = currentState.greenIncubation; i > -1; i--) {
                Person person = currentState.startingPopulation[i];
                int[] position = person.nextPosition();
                if (currentState.space[position[0]][position[1]] != null) {
                    for (Person contatto : currentState.space[position[0]][position[1]]) {
                        contact(contatto, person);
                    }
                }
            }
        }
    }
    // NB: I blu sono invisibili, quindi come se fossero inesistenti
    // infatti, su di loro le collisioni non hanno effetto e non sono registrate.


    /**
     * Passa al giorno successivo della simulazione e
     * aggiorna i valori della simulazione. Contiene il
     * payload da eseguire ad ogni cambiamento di giorno.
     * Calcola anche il valore vd (vedere specifiche progetto).
     * Vengono considerati nel numero di infetti tutte le
     * persone tranne quelle verdi (anche quelle in incubazione
     * sono considerate infette).
     */
    @ToRevise
    private boolean nextDay(){
        for (int i = currentState.blueBlack; i > -1 ; i--)             //TODO: Controllare che gli indici siano giusti (voglio refreshare tutti tranne i verdi non incubati, blu e neri)
        {
            currentState.startingPopulation[i].refresh();
        }
        if (currentState.redBlue-currentState.yellowRed!=0) currentState.unoPatientFound = true;

        currentState.total.get(0).add(getConfigs().populationNumber-currentState.greenIncubation-1);    //Tutti gli infetti (quelli in incubazione sono compresi)
        currentState.total.get(1).add(currentState.getSymptomaticNumber());                                     //Tutti i malati gravi
        currentState.total.get(2).add(currentState.getDeathsNumber());                                          //Tutti i morti
        currentState.total.get(3).add(currentState.getTotalSwabsNumber());

        currentState.daily.get(0).add(currentState.total.get(0).get(currentState.total.get(0).size()-1) - currentState.total.get(0).get(currentState.total.get(0).size()-2));   //Gli infetti giornalieri
        currentState.daily.get(1).add(currentState.getSymptomaticNumber()-currentState.total.get(1).get(currentState.total.get(1).size()-1));                                   //I malati gravi giornalieri
        currentState.daily.get(2).add(currentState.getDeathsNumber()-currentState.total.get(2).get(currentState.total.get(2).size()-1));                                        //I morti giornalieri
        currentState.daily.get(3).add(currentState.getTotalSwabsNumber()-currentState.total.get(3).get(currentState.total.get(3).size()-1));

        boolean result = currentState.subtractResources(currentState.getSymptomaticNumber()*getConfigs().swabsCost*3 + currentState.currentlyStationary*Config.DAILY_COST_IF_STATIONARY);
        if (!result) currentState.status = SimulationStatus.NO_MORE_RESOURCES;
        menu.feedback(currentState);
        currentState.currentlyStationary = currentState.getDeathsNumber();
        if (currentState.unoPatientFound) currentScenario.dailyAction();
        currentState.currentDay+=1;
        if (currentState.greenIncubation == currentState.redBlue + 1) { //Sono tutti guariti.
            currentState.status = SimulationStatus.ERADICATED_DISEASE;
            return false;
        }
        if (currentState.blueBlack == 0) { //Sono tutti morti.
            currentState.status = SimulationStatus.NONE_SURVIVED;
            return false;
        }
        return result;
    }

    /**
     * Sostituisci le configurazioni correnti della simulazione
     * con le configurazioni passate in input.
     *
     * @param config    istanza di Config, contiene le configurazioni da cariare nella simulazione
     */
    @ToRevise
    public void loadConfigs(Config config){
        getConfigs().copy(config);
    }

    /**
     * Restituisce le configurazioni della simulazione corrente.
     *
     * @return      configurazioni attuali
     */
    @ToRevise
    public Config getConfigs(){
        return currentState.configs;
    }

    /**
     * Restituisce lo stato della simulazione corrente.
     *
     * @return      stato attuale
     */
    @Ready
    public State getCurrentState() {return currentState;}

    /**
     * Restituisce lo scenario attualmente in uso.
     *
     * @return      scenario attuale
     */
    @Ready
    public Scenario getCurrentScenario() {return currentScenario;}

    /**
     * Controlla se lo scenario passato come parametro
     * è attivo nella simulazione corrente.
     *
     * @param scenario  scenario da controllare.
     * @return          true se attivo, false altrimenti.
     */
    @Ready
    public boolean isScenarioEnabled(Scenario scenario){
        if (currentScenario instanceof DefaultScenario){
            return scenario == null;
        }
        else if (currentScenario instanceof CustomScenario) {
            return ((CustomScenario) currentScenario).containsScenario(scenario.getInfos().getID());
        }
        return false;
    }

    /**
     * Fa il tampone alle persone scelte per quel determinato giorno e, se una di queste
     * risulta positiva, aggiunge alla coda le persone con cui è entrata in contatto.
     *
     * @param percent   percentuale di fare il tampone ad una persona.
     *
     */
    @ToRevise
    public void swabQueue(double percent){
        int oldSize = currentState.swabPersons.getSize();
        for (int i = 0; i < oldSize; i++) {
            Person x = currentState.swabPersons.dequeue();
            if (Rng.generateFortune(percent, 1)) {
                if (doSwab(x)) {
                    for (Person person : currentState.contacts.get(x)) {
                        if (!currentState.swabs.contains(person))
                            currentState.swabPersons.enqueue(person);
                    }
                }
            }
        }
    }

    /**
     * Questo metodo prende in input due parametri: una prima persona
     * (quella sana) e una seconda persona (quella contagiata). Questo metodo
     * si occupa innanzitutto di richiamare i metodi della classe Rng per
     * calcolare se la persona sana deve risultare contagiata. Se il risultato
     * è positivo e se il paziente uno è stato trovato, bisogna memorizzare il
     * contatto nel dizionario che conterrà gli incontri di una certa persona.
     *
     * @param p1    persona sana che è venuta in contatto con un infetto
     * @param p2    persona infetta.
     */
    @ToRevise
    private void contact(Person p1, Person p2){
        p1.contact = true;
        p2.contact = true;
        if(!p2.isInfected()){
            if(Rng.generateFortune(currentState.configs.infectivity, currentState.isPoorCountry() ? 5 : 0)) {
                p2.setAsInfected();
            }
        }
        currentState.contacts.putIfAbsent(p1, new ArrayList<>());
        currentState.contacts.get(p1).add(p2);
    }

    /**
     * Toglie denaro dalle casse dello Stato per effettuare un tampone
     * sulla persona. Effettuato il tampone, se la persona è nella
     * lista dei contatti, si può decidere di eseguire un tampone
     * anche ai contatti (contact tracing). Il tampone ha una certa
     * probabilità di fallire. Il metodo "generateFortune" in Rng
     * calcola le probabilità di riuscita del tampone.
     * Se il tampone è posiitvo, la persona viene fermata.
     * Il tampone verrà usato o meno in base allo scenario che si sceglie.
     *
     * @param p1    persona a cui sottoporre il tampone.
     * @return      true se la persona è positiva al tampone, false altrimenti.
     */
     @ToRevise
     public boolean doSwab(Person p1){
         currentState.totalSwabsNumber++;
         boolean result = false;
         currentState.subtractResources(currentState.configs.swabsCost);
         if (p1.color == ColorStatus.YELLOW) {
             result = true;
         }
         if (!result) return false;
         if (!Rng.generateFortune(Config.SWAB_SUCCESS_RATE,1)){
             result = false;
         }
         if (result) {
             currentState.swabs.add(p1);
             if (!currentState.swabs.contains(p1)) p1.setStationary(currentState.configs.diseaseDuration - currentState.configs.incubationToYellowDeadline);
         }
         return result;
     }

    /**
     * Termina la simulazione ed esegue le operazioni finali.
     * Mostra a schermo i risultati. Della visualizzazione dei
     * risultati e delle statistiche finali si occupa la classe
     * che implementa IMenu.
     */
    @ToRevise
    private void end() {menu.finalFeedback(currentState);}

    // ------------------------- DEBUGGING ---------------------------

    @Debug
    public void debug() {
        Config config = getConfigs();

        config.populationNumber = 100000;
        config.infectivity = 10;
        config.letality = 20;
        config.sintomaticity = 20;
        config.swabsCost = 3;
        config.size = new int[]{500,500};
        config.initialResources = 100000;
        config.diseaseDuration = 50;
        config.ageAverage = 50;
        config.maxAge = 110;
        config.incubationToYellowDeadline = (int)(config.diseaseDuration*Config.INCUBATION_TO_YELLOW_DEADLINE);
        config.yellowToRedDeadline = (int)(config.diseaseDuration*Config.YELLOW_TO_RED_DEADLINE);

        currentState.startingPopulation = Rng.generatePopulation(currentState);
        currentState.resources = config.initialResources;
        //System.out.println(currentState.getCurrentAgeAverage(0, currentState.getCurrentAgeAverage(0,configs.populationNumber)));
    }

    @Debug
    public void debugRun() throws InterruptedException {
        start();
    }

    @Debug
    public void debugDisableInfections() {
        getConfigs().infectivity = 0;
    }

}
