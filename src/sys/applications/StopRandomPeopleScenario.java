package sys.applications;

import assets.MovementStatus;
import assets.Person;
import sys.Simulation;
import sys.Core.*;
import sys.State;
import sys.models.Scenario;
import java.util.Random;

public class StopRandomPeopleScenario extends Scenario{

    private State currentState;
    private int peopleToStop;
    private int duration;
    private static final int ID = 3;

    public StopRandomPeopleScenario(Simulation currentSimulation, int people, int duration) {
        super(currentSimulation);
        this.duration = duration;
        currentState = currentSimulation.getCurrentState();
        peopleToStop = people;
    }

    @Override
    public void oneTimeAction() {

    }

    @Override
    public void dailyAction()  {
        Random r = new Random();
        for (int i = 0; i < peopleToStop; i++) {
            Person x = currentState.startingPopulation[r.nextInt(currentState.blueBlack+1)];
            if (x.getMovement() != MovementStatus.STATIONARY) {
                try {
                    x.setStationary(duration);
                }catch (Person.UnsafeMovementStatusChangeException ignored) { ignored.printStackTrace(); }
            }
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
        return "Stop Random People Scenario\n";
    }
}
