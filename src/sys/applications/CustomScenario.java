package sys.applications;

import sys.Simulation;
import sys.models.Scenario;

import java.util.HashSet;

public class CustomScenario extends Scenario {

    private HashSet<Scenario> scenariosCombination = new HashSet<>();

    public CustomScenario(Simulation currentSimulation) {
        super(currentSimulation);
    }

    public void addScenario(Scenario newScenario){
        scenariosCombination.add(newScenario);
    }

    @Override
    public void oneTimeAction() {
        for (Scenario scenario: scenariosCombination){
            scenario.oneTimeAction();
        }
    }

    @Override
    public void dailyAction() {
        for (Scenario scenario: scenariosCombination){
            scenario.dailyAction();
        }
    }

    @Override
    public void frameAction() {
        for (Scenario scenario: scenariosCombination){
            scenario.frameAction();
        }
    }

}
