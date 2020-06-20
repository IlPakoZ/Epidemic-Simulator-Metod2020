package sys.models;

import sys.Simulation;
import sys.State;

public abstract class Scenario {

    private Simulation currentSimulation;

    public Scenario(Simulation currentSimulation){
        this.currentSimulation = currentSimulation;
    }

    public abstract void oneTimeAction();
    public abstract void dailyAction();
    public abstract void frameAction();

    /**
     * Restituisce l'ID univoco che identifica uno scenario.
     * @return  ID dello scenario.
     */
    public abstract int getID();

    /**
     * Restituisce una breve descrizione dello scenario.
     * @return  breve descrizione dello scenario.
     */
    public abstract String getInfos();

    /**
     * Restituisce il nome dello scenario.
     * @return  nome dello scenario.
     */
    public abstract String getName();


    public Simulation getCurrentSimulation(){ return currentSimulation; }
}
