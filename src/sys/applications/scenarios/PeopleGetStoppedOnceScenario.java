package sys.applications.scenarios;

import assets.MovementStatus;
import assets.Person;
import sys.Rng;
import sys.Simulation;
import sys.Core.*;
import sys.State;
import sys.models.Scenario;
import java.util.Random;

public class PeopleGetStoppedOnceScenario extends Scenario{

    private State currentState;
    private int peopleToStop;
    private int duration;
    public static final ScenarioInfos SCENARIO_INFOS = new ScenarioInfos(4);

    static {
        SCENARIO_INFOS.setInfos("In questo scenario, vengono fermate una sola volta un numero preso in input di persone per un numero di giorni presi in input.\n" +
                "Parametri:\n" +
                "\t- numero massimo di persone da testare;" +
                "\t- per quanto tempo queste persone dovranno essere fermate;");
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
    public void oneTimeAction() {
        for (int i = 0; i < peopleToStop; i++) {
            Person x = currentState.startingPopulation[Rng.R.nextInt(currentState.blueBlack + 1)];
            if (x.getMovement() != MovementStatus.STATIONARY) {
                x.setStationary(duration);
            }
        }
    }


    //Non fa nulla
    @Override
    public void dailyAction() {

    }

    //Non fa nulla
    @Override
    public void frameAction() {

    }

    /**
     * Restituisce una breve descrizione dello scenario e dei parametri di cui necessita per essere eseguito.
     * @return  breve descrizione dello scenario.
     */
    @Override
    public ScenarioInfos getInfos() {
        return SCENARIO_INFOS;
    }
}
