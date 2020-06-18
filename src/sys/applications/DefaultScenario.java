package sys.applications;

import sys.Simulation;
import sys.Core.*;
import sys.models.Scenario;


public class DefaultScenario extends Scenario {

    @Ready
    public DefaultScenario(Simulation currentSimulation){
        super(currentSimulation);
    }

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
}
