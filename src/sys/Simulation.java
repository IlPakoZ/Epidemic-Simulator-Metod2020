package sys;

import assets.ColorStatus;
import assets.MovementStatus;
import assets.Person;

import javafx.util.Pair;
import sys.Core.*;
import sys.applications.*;
import sys.models.IMenu;
import sys.models.IScenario;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Simulation {

    private State currentState;
    private IMenu menu;
    private IScenario currentScenario = new DefaultScenario();

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
        currentState.totalInfected = new ArrayList<>();
        currentState.dailyInfected = new ArrayList<>();
        currentState.contacts = new HashMap<>();
        currentState.swabs = new HashSet<>();
    }

    @Ready
    public void debug() throws InterruptedException {
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
        currentState.startingPopulation = Rng.generatePopulation(currentState);
        currentState.startingPopulation[currentState.configs.populationNumber-1].setAsInfected();
        currentState.resources = currentState.configs.initialResources;
        start();
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
                    currentScenario = menu.selectScenario();
                    state = 1;
                    //Mostra il menù di scelta dello scenario
                default:
                    throw new UnsupportedOperationException();
            }
        }while(state != 0);
        currentState.startingPopulation = Rng.generatePopulation(currentState);
        currentState.startingPopulation[currentState.configs.populationNumber-1].setAsInfected();
        currentState.resources = currentState.configs.initialResources;
        currentScenario.oneTimeAction(this);
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
                    currentState.totalInfected.add(0);
                    currentState.dailyInfected.add(0);
                    menu.feedback(currentState);
                }else{
                    nextDay();
                    Instant endTime = Instant.now();
                    int duration = Duration.between(startTime, endTime).getNano()/1000000;
                    if (duration < currentState.configs.dayDuration) Thread.sleep(currentState.configs.dayDuration-duration);
                    startTime = Instant.now();
                }
            }
            currentScenario.frameAction(this);
            frame++;
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
        currentState.space = new HashMap<>();
        for (int i=currentState.incubationYellow+1;i<=currentState.redBlue;i++){
            Pair<Integer, Integer> position = currentState.startingPopulation[i].nextPosition();
            currentState.space.putIfAbsent(position, new ArrayList<>());
            currentState.space.get(position).add(currentState.startingPopulation[i]);
        }
        if (currentState.redBlue - currentState.incubationYellow != 0) {
            for (int i = currentState.greenIncubation; i > -1; i--) {
                Person person = currentState.startingPopulation[i];
                if (currentState.space.containsKey(person.nextPosition())) {
                    for (Person contatto : currentState.space.get(person.getPosition())) {
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
    private void nextDay(){
        //Potrei fare quella cosa degli indici anche qui?
        for (int i = currentState.redBlue; i > -1 ; i--)             //TODO: Controllare che gli indici siano giusti (voglio refreshare tutti tranne i verdi non incubati, blu e neri)
        {
            currentState.startingPopulation[i].refresh();
        }
        if (currentState.redBlue-currentState.yellowRed!=0) currentState.unoPatientFound = true;

        currentState.totalInfected.add(currentState.configs.populationNumber-currentState.greenIncubation-1);
        currentState.dailyInfected.add(currentState.totalInfected.get(currentState.totalInfected.size()-1) - currentState.totalInfected.get(currentState.totalInfected.size()-2));
        menu.feedback(currentState);
        currentScenario.dailyAction(this);
        currentState.currentDay+=1;                                 //Controllare se l'incremento del giorno è nella posizione giusta (dovrebbe esserlo)
    }

    /**
     * Sostituisci le configurazioni correnti della simulazione
     * con le configurazioni passate in input.
     * @param config    istanza di Config, contiene le configurazioni da cariare nella simulazione.
     */
    @ToRevise
    void loadConfigs(Config config){
        currentState.configs.copy(config);
    }

    /**
     * Restituisce le configurazioni della simulazione corrente.
     * @return      configurazioni attuali.
     */
    @ToRevise
    Config getConfigs(){
        return currentState.configs;
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
            if(Rng.generateFortune(currentState.configs.infectivity, p2.getInfectivityModifier())) {
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
     @NotImplemented
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


}
