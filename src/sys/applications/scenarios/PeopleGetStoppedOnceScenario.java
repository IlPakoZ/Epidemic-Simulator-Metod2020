package sys.applications.scenarios;

import assets.MovementStatus;
import assets.Person;
import sys.Rng;
import sys.Simulation;
import sys.Core.*;
import sys.State;
import sys.applications.Functional;
import sys.models.Scenario;

import javax.swing.plaf.IconUIResource;
import java.util.Random;

public class PeopleGetStoppedOnceScenario extends Scenario{

    private State currentState;
    private double peopleToStop;
    private int duration;
    public static final Integer DURATION_LOWER_BOUND = 0;
    public static final Integer DURATION_UPPER_BOUND = 200;

    public static final ScenarioInfos SCENARIO_INFOS = new ScenarioInfos(4);

    static {
        SCENARIO_INFOS.setInfos("In questo scenario, vengono fermate una sola volta un numero preso in input di persone per un numero di giorni presi in input.\n" +
                "\nParametri:\n" +
                "\t1) numero delle persone da testare;" +
                "\t2) per quanto tempo queste persone dovranno essere fermate;");
        SCENARIO_INFOS.setName("People Get Stopped Once Scenario");
    }

    public PeopleGetStoppedOnceScenario(Simulation currentSimulation, int people, int duration) {
        super(currentSimulation);
        this.duration = duration;
        currentState = currentSimulation.getCurrentState();
        peopleToStop = people;
    }

    /**
     * Ferma un numero preso in input di persone, scelte casualmente tra la
     * popolazione, per un numero di giorni preso in input e con un intervallo
     * di tempo preso in input.
     */
    @Override
    @Ready
    public void oneTimeAction() {
        if (peopleToStop>currentState.configs.getPopulationNumber()) peopleToStop = currentState.configs.getPopulationNumber();
        int[] indexes = Rng.getPersonShuffledIndex(currentState);
        int indexPerson = 0;
        for (int i=0; i<peopleToStop; i++) {
            if (indexPerson == currentState.configs.getPopulationNumber()) break;
            Person x = currentState.startingPopulation[indexes[indexPerson]];
            if (x.getMovement() == MovementStatus.MOVING) {
                x.setStationary(duration);
            }
            else i--;
            indexPerson++;
        }
    }


    //Non fa nulla
    @Override
    @Ready
    public void dailyAction() { }

    //Non fa nulla
    @Override
    @Ready
    public void frameAction() { }

    /**
     * Restituisce una breve descrizione dello scenario e dei parametri di cui necessita per essere eseguito.
     * @return  breve descrizione dello scenario.
     */
    @Override
    @Ready
    public ScenarioInfos getInfos() {
        return SCENARIO_INFOS;
    }
}
