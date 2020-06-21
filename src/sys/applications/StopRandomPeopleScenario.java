package sys.applications;

import assets.MovementStatus;
import assets.Person;
import sys.Simulation;
import sys.Core.*;
import sys.State;
import sys.models.Scenario;
import java.util.Random;

public class StopRandomPeopleScenario extends Scenario{

    private Simulation currentSimulation;
    private State currentState;
    private int peopleToStop;
    private static final int ID = 3;

    public StopRandomPeopleScenario(Simulation currentSimulation, int people) {
        super(currentSimulation);
        currentState = currentSimulation.getCurrentState();
        peopleToStop = people;
    }

    @Override
    public void oneTimeAction() {

    }

    @Override
    public void dailyAction() {
        Random r = new Random();
        for (int i = 0; i < peopleToStop; i++) {
            Person x = currentState.startingPopulation[r.nextInt(currentState.blueBlack+1)];
            x.movement = MovementStatus.STATIONARY;
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
        return "In questo scenario, ogni giorno vengono fermate un numero preso in input di persone.\n";
    }

    @Override
    public String getName() {
        return "StopRandomPeopleScenario\n";
    }
}
