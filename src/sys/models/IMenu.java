package sys.models;

import sys.State;

public interface IMenu {

    /**
     * Restituisce un valore intero che viene utilizzato
     * da un'istanza di Simulation per interpretare.
     */
    int show();

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

}