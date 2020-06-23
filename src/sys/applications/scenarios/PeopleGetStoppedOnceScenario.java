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
    private double percentToStop;
    private int duration;
    public static final Integer DURATION_LOWER_BOUND = 0;
    public static final Integer DURATION_UPPER_BOUND = 200;

    public static final ScenarioInfos SCENARIO_INFOS = new ScenarioInfos(4);

    static {
        SCENARIO_INFOS.setInfos("In questo scenario, vengono fermate una sola volta un numero preso in input di persone per un numero di giorni presi in input.\n" +
                "\nParametri:\n" +
                "\t1) percentuale delle persone da testare;" +
                "\t2) per quanto tempo queste persone dovranno essere fermate;");
        SCENARIO_INFOS.setName("People Get Stopped Once Scenario");
    }

    public PeopleGetStoppedOnceScenario(Simulation currentSimulation, double percent, int duration) {
        super(currentSimulation);
        this.duration = duration;
        currentState = currentSimulation.getCurrentState();
        percentToStop = percent;
    }

    /**
     * Ferma un numero preso in input di persone, scelte casualmente tra la
     * popolazione, per un numero di giorni preso in input e con un intervallo
     * di tempo preso in input.
     */
    @Override
    public void oneTimeAction() {
        int limit = (int)percentToStop*currentState.configs.getPopulationNumber()/100;
        for (int i = 0; i < limit; i++) {
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
