package sys.applications;

import sys.Simulation;
import sys.State;
import sys.Core.*;
import sys.models.IScenario;


public class DefaultScenario implements IScenario {

    //Non fa nulla
    @Ready
    @Override
    public void oneTimeAction(Simulation currentSimulation) { }

    //Non fa nulla
    @Ready
    @Override
    public void dailyAction(Simulation currentSimulation) { }

    //Non fa nulla
    @Ready
    @Override
    public void frameAction(Simulation currentSimulation) { }
}
