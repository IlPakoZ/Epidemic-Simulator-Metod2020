package sys.applications;

import assets.Person;
import sys.Simulation;
import sys.Core.*;
import sys.State;
import sys.models.Scenario;
import java.util.Random;

public class RandomSwabsScenario extends Scenario{

    private Simulation currentSimulation;
    private State currentState;
    private static final int ID = 1;
    private int swabsNumber;

    public RandomSwabsScenario(Simulation currentSimulation, int swabs) {
        super(currentSimulation);
        this.currentSimulation = currentSimulation;
        this.currentState = currentSimulation.getCurrentState();
        swabsNumber = swabs;
    }


    @Override
    public void oneTimeAction() {

    }

    @Override
    public void dailyAction() {
        Random r = new Random();
        for (int i = 0; i < swabsNumber; i++) {
            Person x = currentState.startingPopulation[r.nextInt(currentState.blueBlack+1)];
            if (!currentState.swabs.contains(x)) currentSimulation.doSwab(x);
        }
    }

    @Override
    public void frameAction() {

    }

    @Override
    public int getID() {
        return ID;
    }

    @Override
    public String getInfos() {
        return "In questo scenario, ogni giorno vengono testate un numero preso in input di persone, scelte casualmente.\n";
    }

    @Override
    public String getName() {
        return "Random Swabs Scenario\n";
    }
}
