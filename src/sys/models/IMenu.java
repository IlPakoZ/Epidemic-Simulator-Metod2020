package sys.models;

import sys.Simulation;
import sys.State;
import sys.Config;

public interface IMenu {

    /**
     * Restituisce un valore intero che viene utilizzato
     * da un'istanza di Simulation per essere interpretato
     * dal metodo run come comando.
     * ES: restituire 0 chiude il menù e fa partire la simulazione.
     *
     * @return       restituisce un valore che verrà interpretato dal
     *               metodo run di Simulation per decidere che operazione
     *               eseguire
     */
    int show();

    /**
     * Apre la sezione del menù che si occupa di gestire il primo
     * inserimento dei parametri di input. Controllare la correttezza
     * degli input spetta a questa classe.
     *
     * @param config     configurazione corrente della simulazione
     */
    void firstInput(Config config);

    /**
     * Apre il menù delle opzioni. Permette di modificare singolarmente
     * i parametri di input inseriti. Controllare la correttezza
     * degli input spetta a questa classe. Serve anche per inserire
     * i parametri opzionali.
     *
     * @param config configurazione corrente della simulazione
     * @return       restituisce un valore che verrà interpretato dal
     *               metodo run di Simulation per decidere che operazione
     *               eseguire
     */
    int settings(Config config);

    /**
     * Apre il menù opzionale
     * 
     * @param config configurazione corrente della simulazione
     * @return       restituisce un valore che verrà interpretato dal
     *               metodo run di Simulation per decidere che operazione
     *               eseguire
     */
    int settings_op(Config config);

    /**
     * Ogni giorno che passa, mostra un feedback all'utente
     * dei cambiamenti che ci sono stati dal giorno precedente
     * o altri tipi di feedback.
     *
     * @param state     stato corrente della simulazione
     */
    void feedback(State state);

    /**
     * Questo metodo viene richiamato da Simulation alla fine
     * della simulazione. Viene utilizzato per mostrare il
     * sommario della simulazione e altre informazioni statistiche.
     *
     * @param state     stato corrente della simulazione
     */
    void finalFeedback(State state);

    /**
     * Questo metodo viene richiamato da Simulation per aprire
     * il menù della scelta dello scenario. In questo menù, verrà
     * restituita un oggetto di Scenario che implementa IScenario in base
     * al valore inserito dall'utente (quindi in base allo scenario che
     * vorrà scegliere l'utente).
     * Questo metodo conterrà anche una breve descrizione dello scenario
     * che si vuole selezionare.
     *
     * @return un oggetto di una classe che implementa IScenario in base
     *         alla scelta dell'utente.
     */
    Scenario selectScenario(Simulation simulation);

}