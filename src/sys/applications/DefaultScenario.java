package sys.applications;

import sys.Simulation;
import sys.Core.*;
import sys.models.Scenario;


public class DefaultScenario extends Scenario {

    @Ready
    public DefaultScenario(Simulation currentSimulation){ super(currentSimulation); }

    //Non fa nulla
    @Ready
    @Override
    public void oneTimeAction() { }

    //Non fa nulla
    @Ready
    @Override
    public void dailyAction() { }

    //Non fa nulla
    @Ready
    @Override
    public void frameAction() { }

    @Override
    public int getID() {
        return -1;
    }

    @Override
    public String getInfos() {
        return  "In questo scenario, non viene fatto nessun tampone.\n"+
                "Non viene utilizzata nessuna trategia particolare per\nil contenimento della malattia.";
    }

    @Override
    public String getName() {
        return "Scenario di default.\n";
    }

}
