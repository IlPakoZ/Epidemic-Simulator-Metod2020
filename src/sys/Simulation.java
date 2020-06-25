package sys;

import assets.Person;

import sys.Core.*;
import sys.applications.scenarios.*;
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
    @Ready
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
        currentState.swabPersons = new Queue<>();
    }

    /**
     * Mostra il menù principale.
     * Serve all'utente per interfacciarsi alla simulazione
     * e decidere cosa fare.
     */
    @Ready
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
                case(4):
                    state = menu.settings_op(getConfigs());
                    //Mostra il menù opzionale
                case 5:
                    menu.selectScenario(this);
                    state = 0;
                    //Mostra il menù di scelta dello scenario
                case 6:

                    break;
                default:
                    throw new UnsupportedOperationException();
            }
        }while(state != 2);
        currentState.startingPopulation = Rng.generatePopulation(currentState);
        currentState.startingPopulation[getConfigs().getPopulationNumber()-1].setAsInfected();
        currentState.resources = getConfigs().getInitialResources();
        start();
    }

    /**
     * Avvia la simulazione.
     */
    @Ready
    private void start() throws InterruptedException {
        currentState.status = SimulationStatus.PLAYING;
        boolean going = true;
        int frame = 0;
        Instant startTime = Instant.now();
        while(going){
            loop();
            if (frame % getConfigs().getFrameADay() == 0){
                if (frame == 0){
                    for (ArrayList<Integer> arr: currentState.total){
                        arr.add(0);
                    }
                    for (ArrayList<Integer> arr: currentState.daily){
                        arr.add(0);
                    }
                }else{
                    boolean result = nextDay();
                    if (!result) going = false;

                    Instant endTime = Instant.now();
                    int duration = Duration.between(startTime, endTime).getNano()/1000000;
                    if (duration < getConfigs().getDayDuration()) Thread.sleep(getConfigs().getDayDuration()-duration);
                    startTime = Instant.now();
                }
            }
            if (currentState.unoPatientFound) currentScenario.frameAction();
            frame++;
        }
        end();
    }

    /**
     * Contiene il loop di istruzioni che devono essere
     * eseguite in ripetizione. Restituisce true se il loop
     * deve continuare la sua esecuzione, altrimenti restituisce
     * false.
     *
     */
    @Ready
    private void loop() {
        currentState.space = new PeopleIndexList[currentState.configs.getSize()[0]+1][currentState.configs.getSize()[1]+1];

        for (int i=currentState.greenIncubation+1; i<=currentState.incubationYellow; i++){
            currentState.startingPopulation[i].nextPosition();
        }

        for (int i=currentState.incubationYellow+1;i<=currentState.redBlue;i++){
            int[] position = currentState.startingPopulation[i].nextPosition();
            if (currentState.space[position[0]][position[1]] == null){
                currentState.space[position[0]][position[1]] = new PeopleIndexList();
            }
            currentState.space[position[0]][position[1]].addElement(i);
        }

        for (int i = currentState.greenIncubation; i > -1; i--) {
            Person person = currentState.startingPopulation[i];
            int[] position = person.nextPosition();
            if (currentState.space[position[0]][position[1]] != null) {
                for (Integer contatto : currentState.space[position[0]][position[1]]) {
                    contact(currentState.startingPopulation[contatto], person);
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
    @Ready
    private boolean nextDay(){
        currentState.currentlyStationary = currentState.getDeathsNumber();

        for (int i = currentState.blueBlack; i > -1 ; i--)             //TODO: Controllare che gli indici siano giusti (voglio refreshare tutti tranne i verdi non incubati, blu e neri)
        {
            currentState.startingPopulation[i].refresh();
        }

        if (currentState.redBlue-currentState.yellowRed!=0) currentState.unoPatientFound = true;
        if (currentState.unoPatientFound) {
            if (!currentScenario.isConsumed()){
                currentScenario.setConsumed();
                currentScenario.oneTimeAction();
            }
            currentScenario.dailyAction();
        }
        currentState.total.get(0).add(getConfigs().getPopulationNumber()-currentState.greenIncubation-1);       //Tutti gli infetti (quelli in incubazione sono compresi)
        currentState.total.get(1).add(currentState.getSymptomaticNumber());                                     //Tutti i malati gravi
        currentState.total.get(2).add(currentState.getDeathsNumber());                                          //Tutti i morti
        currentState.total.get(3).add(currentState.getTotalSwabsNumber());

        currentState.daily.get(0).add(currentState.total.get(0).get(currentState.total.get(0).size()-1) - currentState.total.get(0).get(currentState.total.get(0).size()-2));   //Gli infetti giornalieri
        currentState.daily.get(1).add(currentState.getSymptomaticNumber()-currentState.total.get(1).get(currentState.total.get(1).size()-1));                                   //I malati gravi giornalieri
        currentState.daily.get(2).add(currentState.getDeathsNumber()-currentState.total.get(2).get(currentState.total.get(2).size()-1));                                        //I morti giornalieri
        currentState.daily.get(3).add(currentState.getTotalSwabsNumber()-currentState.total.get(3).get(currentState.total.get(3).size()-1));

        boolean result = currentState.subtractResources(currentState.getSymptomaticNumber()*getConfigs().getSwabsCost()*3 + currentState.currentlyStationary*Config.DAILY_COST_IF_STATIONARY);
        currentState.currentlyStationary = 0;
        if (!result) currentState.status = SimulationStatus.NO_MORE_RESOURCES;
        menu.feedback(currentState);

        currentState.currentDay+=1;
        if (currentState.getSymptomaticNumber()+currentState.getAsymptomaticNumber()+currentState.getIncubationNumber()==0) { //Sono tutti guariti.
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
    @Ready
    public void loadConfigs(Config config){
        getConfigs().copy(config);
    }

    /**
     * Restituisce le configurazioni della simulazione corrente.
     *
     * @return      configurazioni attuali
     */
    @Ready
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
     * Carica lo scenario passato come parametro.
     *
     * @param scenario  nuovo scenario
     */
    @Ready
    public void loadScenario(Scenario scenario) { this.currentScenario = scenario; }

    /**
     * Controlla se l'ID dello scenario passato come parametro
     * è attivo nella simulazione corrente.
     *
     * @param ID        ID dello scenario da controllare.
     * @return          true se attivo, false altrimenti.
     */
    @Ready
    public boolean isScenarioEnabled(Integer ID){
        if (currentScenario.getInfos().getID() == ID){
            return true;
        }
        else if (currentScenario instanceof CustomScenario) {
            return ((CustomScenario) currentScenario).containsScenario(ID);
        }
        return false;
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
    @Ready
    private void contact(Person p1, Person p2){
        p1.contact = true;
        p2.contact = true;
        if(!p2.isInfected()){
            if(Rng.generateFortune(currentState.configs.getInfectivity(), currentState.isPoorCountry() ? 5 : 0)) {
                p2.setAsInfected();
            }
        }
        if (currentState.unoPatientFound) {
            currentState.contacts.putIfAbsent(p1, new HashSet<>());
            currentState.contacts.get(p1).add(p2);
        }
    }

    /**
     * Termina la simulazione ed esegue le operazioni finali.
     * Mostra a schermo i risultati. Della visualizzazione dei
     * risultati e delle statistiche finali si occupa la classe
     * che implementa IMenu.
     */
    @Ready
    private void end() {menu.finalFeedback(currentState);}

    // ------------------------- DEBUGGING ---------------------------

    @Debug
    public void debug() {
        Config config = getConfigs();

        config.forcePopulationNumber(80000);
        config.forceHealthParameters(10, 0);
        config.forceHealthParameters(20, 1);
        config.forceHealthParameters(5, 2);
        config.forceSwabsCost(4);
        config.forceSize(1000,1000);
        config.forceInitialResources(3000000);
        config.forceDiseaseDuration(40);
        //config.forceAge(50, 1);
        //config.forceAge(110, 0);
        config.incubationToYellowDeadline = (int)(config.getDiseaseDuration()*Config.INCUBATION_TO_YELLOW_DEADLINE);
        config.yellowToRedDeadline = (int)(config.getDiseaseDuration()*Config.YELLOW_TO_RED_DEADLINE);

        //((CustomScenario)currentScenario).addScenario(new StopRandomPeopleScenario(this, 50000, 50, 50));
        //((CustomScenario)currentScenario).addScenario(new PeopleMetGetTestedScenario(this, 90));
        //((CustomScenario)currentScenario).addScenario(new RandomSwabsScenario(this, 5000));

        //currentScenario = new StopRandomPeopleScenario(this, 20000, 40, 40);
        //currentScenario = new RandomSwabsScenario(this, 5000);
        currentState.startingPopulation = Rng.generatePopulation(currentState);
        currentState.resources = config.getInitialResources();

        //System.out.println(currentState.getCurrentAgeAverage(0, currentState.getCurrentAgeAverage(0,configs.populationNumber)));
    }

    @Debug
    public void debugRun() throws InterruptedException {
        start();
    }

    @Debug
    public void debugDisableInfections() {
        getConfigs().forceHealthParameters(0, 0);
    }

}
