package sys.models;

import sys.Simulation;
import sys.applications.scenarios.ScenarioInfos;

public abstract class Scenario {

    private boolean consumed = false;
    private Simulation currentSimulation;

    public Scenario(Simulation currentSimulation){
        this.currentSimulation = currentSimulation;
    }

    public void oneTimeAction(){}
    public abstract void dailyAction();
    public abstract void frameAction();
    public abstract ScenarioInfos getInfos();

    public boolean isConsumed(){
        return consumed;
    }

    public void setConsumed(){
        consumed = true;
    }
}
