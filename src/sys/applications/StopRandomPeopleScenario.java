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

    //Non fa nulla
    @Override
    public void oneTimeAction() {

    }

    /**
     * Ferma un numero preso in input di persone, scelte casualmente tra la
     * popolazione, per un numero di giorni preso in input.
     */
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

    //Non fa nulla
    @Override
    public void frameAction() {

    }

    /**
     * Restituisce l'ID univoco che identifica uno scenario.
     * @return  ID dello scenario.
     */
    @Override
    public int getID() {
        return ID;
    }

    /**
     * Restituisce una breve descrizione dello scenario.
     * @return  breve descrizione dello scenario.
     */
    @Override
    public String getInfos() {
        return "In questo scenario, ogni giorno vengono fermate un numero preso in input di persone.\n";
    }

    /**
     * Restituisce il nome dello scenario.
     * @return  nome dello scenario.
     */
    @Override
    public String getName() {
        return "Stop Random People Scenario\n";
    }
}
