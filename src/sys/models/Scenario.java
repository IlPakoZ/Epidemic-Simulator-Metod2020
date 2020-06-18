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
}
