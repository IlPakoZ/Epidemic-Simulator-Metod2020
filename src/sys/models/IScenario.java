package sys.models;

import sys.Simulation;
import sys.State;

public interface IScenario {

    void oneTimeAction(Simulation currentSimulation);
    void dailyAction(Simulation currentSimulation);
    void frameAction(Simulation currentSimulation);
}
