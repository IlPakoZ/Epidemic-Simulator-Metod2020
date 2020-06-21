package sys;

import assets.Person;

import javafx.util.Pair;
import sys.Core.*;
import sys.applications.*;
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
        currentState.daily = new ArrayList<>();
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
        int state = 1;
        do {
            switch (state) {
                case 1:
                    state = menu.show();
                    //Mostra il menù principale / torna al menù principale
                    break;
                case 2:
                    state = menu.settings(getConfigs());
                    //Mostra il menù delle opzioni
                    break;
                case 3:
                    currentScenario = menu.selectScenario(this);
                    state = 1;
                    //Mostra il menù di scelta dello scenario
                case 4:

                    break;
                default:
                    throw new UnsupportedOperationException();
            }
        }while(state != 0);
        currentState.startingPopulation = Rng.generatePopulation(currentState);
        currentState.startingPopulation[currentState.configs.populationNumber-1].setAsInfected();
        currentState.resources = currentState.configs.initialResources;
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
            going = loop();
            if (frame % currentState.configs.frameADay == 0){
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
                    if (!result) {
                        going = false;
                        currentState.status = SimulationStatus.NO_MORE_RESOURCES;
                    }
                    Instant endTime = Instant.now();
                    int duration = Duration.between(startTime, endTime).getNano()/1000000;
                    if (duration < currentState.configs.dayDuration) Thread.sleep(currentState.configs.dayDuration-duration);
                    startTime = Instant.now();
                }
            }
            currentScenario.frameAction();
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
     * @return      booleano che indica se il loop è ancora in esecuzione
     */
    @ToRevise
    private boolean loop() {

        currentState.space = new PersonList[currentState.configs.size.getKey()][currentState.configs.size.getValue()];
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

        //Mettere una condizione di uscita (se sono tutti guariti)
        return true;
    }
    // NB: I blu sono invisibili, quindi come se fossero inesistenti
    // infatti, su di loro le collisioni non hanno effetto e non sono registrate.


    /**
     * Passa al giorno della simulazione successivo,
     * aggiorna i valori della simulazione. Contiene il
     * payload da eseguire ad ogni cambiamento di giorno.
     * Calcola anche il valore vd (vedere specifiche progetto).
     * Vengono considerati nel numero di infetti tutte le
     * persone tranne quelle verdi (anche quelle in incubazione
     * sono considerate infette).
     */
    @ToRevise
    private boolean nextDay(){
        for (int i = currentState.redBlue; i > -1 ; i--)             //TODO: Controllare che gli indici siano giusti (voglio refreshare tutti tranne i verdi non incubati, blu e neri)
        {
            currentState.startingPopulation[i].refresh();
        }
        if (currentState.redBlue-currentState.yellowRed!=0) currentState.unoPatientFound = true;

        currentState.total.get(0).add(currentState.configs.populationNumber-currentState.greenIncubation-1);    //Tutti gli infetti (quelli in incubazione sono compresi)
        currentState.total.get(1).add(currentState.getSymptomaticNumber());                                     //Tutti i malati gravi
        currentState.total.get(2).add(currentState.getDeathsNumber());                                          //Tutti i morti

        currentState.daily.get(0).add(currentState.total.get(0).get(currentState.total.get(0).size()-1) - currentState.total.get(0).get(currentState.total.get(0).size()-2));   //Gli infetti giornalieri
        currentState.daily.get(1).add(currentState.getSymptomaticNumber()-currentState.total.get(1).get(currentState.total.get(1).size()-1));                                   //I malati gravi giornalieri
        currentState.daily.get(2).add(currentState.getDeathsNumber()-currentState.total.get(2).get(currentState.total.get(2).size()-1));                                        //I morti giornalieri

        boolean result = currentState.subtractResources(currentState.getSymptomaticNumber()*currentState.configs.swabsCost*3 + currentState.currentlyStationary*Config.DAILY_COST_IF_STATIONARY);
        menu.feedback(currentState);
        currentState.currentlyStationary = currentState.getDeathsNumber();
        currentScenario.dailyAction();
        currentState.currentDay+=1;
        return result;
    }

    /**
     * Sostituisci le configurazioni correnti della simulazione
     * con le configurazioni passate in input.
     * @param config    istanza di Config, contiene le configurazioni da cariare nella simulazione.
     */
    @ToRevise
    public void loadConfigs(Config config){
        currentState.configs.copy(config);
    }

    /**
     * Restituisce le configurazioni della simulazione corrente.
     * @return      configurazioni attuali.
     */
    @ToRevise
    public Config getConfigs(){
        return currentState.configs;
    }

    /**
     * Restituisce lo stato della simulazione corrente.
     * @return      stato attuale.
     */
    @Ready
    public State getCurrentState() {return currentState;}

    /**
     * Restituisce lo scenario attualmente in uso.
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
            return ((CustomScenario) currentScenario).containsScenario(scenario.getID());
        }
        return false;
    }

    /**
     * Fa il tampone alle persone scelte per quel determinato giorno e, se una di queste
     * risulta positivo, aggiunge alla coda le persone con cui è entrata in contatto.
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
     * contatto nel dizionario che conterrà i contatti di una certa persona.
     *
     * @param p1    persona sana che è venuta in contatto con un infetto
     * @param p2    persona infetta.
     */
    @ToRevise
    private void contact(Person p1, Person p2){
        p1.contact = true;
        p2.contact = true;
        if(!p2.isInfected()){
            if(Rng.generateFortune(currentState.configs.infectivity, currentState.isPoorCountry() ? p2.getInfectivityModifier()*5 : p2.getInfectivityModifier())) {
                p2.setAsInfected();
            }
        }
        currentState.contacts.putIfAbsent(p1, new ArrayList<>());
        currentState.contacts.get(p1).add(p2);
    }

    /**
     * Toglie denaro dalle casse dello Stato per effettuare un tampone
     * sulla persona. Effettuato il tempone, se la persona è nella
     * lista dei contatti, si può decidere di eseguire un tampone
     * anche ai contatti (contact tracing). Il tampone ha una certa
     * probabilità di fallire. Usare il metodo "generateFortune" in Rng
     * per calcolare le probabilità di riuscita.
     * Il tampone verrà usato o meno in base allo scenario che si sceglie.
     * @param p1    persona a cui sottoporre il tampone.
     */
     @ToRevise
     public boolean doSwab(Person p1){
         boolean result = false;
         currentState.subtractResources(currentState.configs.swabsCost);
         switch (p1.color) {
             case YELLOW:
             case RED:
                 result = true;
             default:
                 break;
         }
         if (!result) return false;
         if (!Rng.generateFortune(Config.SWAB_SUCCESS_RATE,1)){
             result = false;
         }
         if (result) {
             currentState.swabs.add(p1);
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
        currentState.configs.populationNumber = 100000;
        currentState.configs.infectivity = 10;
        currentState.configs.letality = 20;
        currentState.configs.sintomaticity = 20;
        currentState.configs.swabsCost = 3;
        currentState.configs.size = new Pair<>(500,500);
        currentState.configs.initialResources = 100000;
        currentState.configs.diseaseDuration = 50;
        currentState.configs.ageAverage = 50;
        currentState.configs.maxAge = 110;
        currentState.configs.incubationToYellowDeadline = (int)(currentState.configs.diseaseDuration*Config.INCUBATION_TO_YELLOW_DEADLINE);
        currentState.configs.yellowToRedDeadline = (int)(currentState.configs.diseaseDuration*Config.YELLOW_TO_RED_DEADLINE);

        currentState.startingPopulation = Rng.generatePopulation(currentState);
        currentState.resources = currentState.configs.initialResources;
        //System.out.println(currentState.getCurrentAgeAverage(0, currentState.getCurrentAgeAverage(0,currentState.configs.populationNumber)));
    }

    @Debug
    public void debugRun() throws InterruptedException {
        start();
    }

    @Debug
    public void debugDisableInfections() {
        currentState.configs.infectivity = 0;
    }

}
