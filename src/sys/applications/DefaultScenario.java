package sys.applications;

import sys.Simulation;
import sys.Core.*;
import sys.models.Scenario;


public class DefaultScenario extends Scenario {

    @Ready
    public DefaultScenario(Simulation currentSimulation){ super(currentSimulation); }

    // Non fa nulla
    @Ready
    @Override
    public void oneTimeAction() { }

    // Non fa nulla
    @Ready
    @Override
    public void dailyAction() { }

    // Non fa nulla
    @Ready
    @Override
    public void frameAction() { }

    /**
     * Restituisce l'ID univoco che identifica uno scenario.
     * @return  ID dello scenario.
     */
    @Override
    public int getID() {
        return -1;
    }

    /**
     * Restituisce una breve descrizione dello scenario e dei parametri di cui necessita per essere eseguito.
     * @return  breve descrizione dello scenario.
     */
    @Override
    public String getInfos() {
        return  "In questo scenario, non viene fatto nessun tampone.\n"+
                "Non viene utilizzata nessuna trategia particolare per\nil contenimento della malattia.";
    }

    /**
     * Restituisce il nome dello scenario.
     * @return  nome dello scenario.
     */
    @Override
    public String getName() {
        return "Scenario di default.\n";
    }

}
