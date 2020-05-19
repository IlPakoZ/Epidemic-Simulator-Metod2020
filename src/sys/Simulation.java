package sys;

import assets.Person;
import assets.SimulationStatus;

import sys.Core.*;
import sys.models.IMenu;
import sys.models.IScenario;

public class Simulation {

    private State currentState;
    private IMenu menu;

    /**
     * Costruttore della simulazione.
     */
    Simulation(IMenu menu){
        initialize(menu);
    }
    Pasquale puzza
    /**
     * Inizializza la simulazione. Metodo di appoggio.
     */
    @ToRevise
    private void initialize(IMenu menu){
        this.menu = menu;
    }

    /**
     * Mostra il menù principale.
     * Serve all'utente per interfacciarsi alla simulazione
     * e decidere cosa fare.
     */
    @ToRevise
    void run() throws InterruptedException {
        menu.show();
        int state = 1;
        while(state != 0) {
            switch (menu.show()) {
                case 0:
                    state = 0;
                    start();
                    break;
                case 1:
                    state = 1;
                    break;
                case -1:
                    throw new UnsupportedOperationException();
            }
        }
    }

    /**
     * Avvia la simulazione.
     */
    @NotImplemented
    private void start() throws InterruptedException {
        currentState.STATUS = SimulationStatus.PLAYING;
        boolean going = true;
        while(going){
            long startTime = System.nanoTime();
            going = loop();
            long endTime = System.nanoTime();
            Thread.sleep(currentState.configs.dayDuration);
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
    @NotImplemented
    private boolean loop() {
        nextDay();
        menu.feedback(currentState);
        return true;
    }

    /**
     * Passa al giorno della simulazione successivo,
     * aggiorna i valori della simulazione. Contiene il
     * payload da eseguire ad ogni cambiamento di giorno.
     * Calcola anche il valore vd (vedere specifiche progetto).
     */
    @NotImplemented
    private void nextDay(){ }

    /**
     * Permette di caricare nella simulazione uno scenario
     * personalizzato.
     */
    @NotImplemented
    void loadScenario(IScenario scenario){ }

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
    @NotImplemented
    private void contact(Person p1, Person p2){ }

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
    private void doSwab(Person p1){ }

    /**
     * Termina la simulazione ed esegue le operazioni finali.
     * Mostra a schermo i risultati. Della visualizzazione dei
     * risultati e delle statistiche finali si occupa la classe
     * che implementa IMenu.
     */
    @NotImplemented
    private void end() {menu.finalFeedback(currentState);}

}
