package sys;

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

    /**
     * Inizializza la simulazione.
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
        while(loop()){
            long startTime = System.nanoTime();
            long endTime = System.nanoTime();

            Thread.sleep(currentState.configs.dayDuration);
        }
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
    @NotImplemented
    void loadConfigs(Config config){
        currentState.configs.copy(config);
    }

    /**
     * Restituisce le configurazioni della simulazione corrente.
     * @return      configurazioni attuali.
     */
    @NotImplemented
    Config getConfigs(){
        return currentState.configs;
    }


}
