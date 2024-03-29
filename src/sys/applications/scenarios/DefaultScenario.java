package sys.applications.scenarios;

import sys.Simulation;
import sys.Core.*;
import sys.models.Scenario;


public class DefaultScenario extends Scenario {
    public static final ScenarioInfos SCENARIO_INFOS = new ScenarioInfos(-1);

    static {
        SCENARIO_INFOS.setInfos("In questo scenario, non viene fatto nessun tampone.\n"+
                "Non viene utilizzata nessuna strategia particolare per il contenimento della malattia.");
        SCENARIO_INFOS.setInfos("Scenario di default");
    }

    @Ready
    public DefaultScenario(Simulation currentSimulation){ super(currentSimulation); }

    // Non fa nulla
    @Ready
    public void oneTimeAction() { }

    // Non fa nulla
    @Ready
    public void dailyAction() { }

    // Non fa nulla
    @Ready
    public void frameAction() { }

    /**
     * Restituisce una breve descrizione dello scenario e dei parametri di cui necessita per essere eseguito.
     * @return  oggetto ScenariosInfos contenente le informazioni dello scenario.
     */
    @Ready
    public ScenarioInfos getInfos() {
        return SCENARIO_INFOS;
    }


}
