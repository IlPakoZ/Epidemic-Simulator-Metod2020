package sys.applications.scenarios;

import assets.MovementStatus;
import assets.Person;
import sys.Rng;
import sys.Simulation;
import sys.Core.*;
import sys.State;
import sys.models.Scenario;
import java.util.Random;

public class StopRandomPeopleScenario extends Scenario{

    private State currentState;
    private int peopleToStop;
    private int duration;
    private int ratio;
    private Integer firstTime;
    public static final ScenarioInfos SCENARIO_INFOS = new ScenarioInfos(3);

    static {
        SCENARIO_INFOS.setInfos("In questo scenario, ogni giorno vengono fermate un numero preso in input di persone per un numero di giorni presi in input.\n" +
                "Parametri:\n" +
                "\t- numero massimo di persone da testare giornalmente (il numero non sarà fisso sempre);" +
                "\t- per quanto tempo queste persone dovranno essere fermate;" +
                "\t- ogni quanti giorni verranno fermate persone casualmente.");
        SCENARIO_INFOS.setName("Stop Random People Scenario");
    }

    public StopRandomPeopleScenario(Simulation currentSimulation, int people, int duration, int ratio) {
        super(currentSimulation);
        this.duration = duration;
        this.ratio = ratio;
        currentState = currentSimulation.getCurrentState();
        peopleToStop = people;
    }

    //Non fa nulla
    @Override
    public void oneTimeAction() { }


    /**
     * Ferma un numero preso in input di persone, scelte casualmente tra la
     * popolazione, per un numero di giorni preso in input e con un intervallo
     * di tempo preso in input.
     */
    @Override
    public void dailyAction()  {
        if (firstTime == null) {
            firstTime = 0;
        } else{
            firstTime++;
        }
        if (firstTime % ratio == 0) {
            for (int i = 0; i < peopleToStop; i++) {
                Person x = currentState.startingPopulation[Rng.R.nextInt(currentState.blueBlack + 1)];
                if (x.getMovement() != MovementStatus.STATIONARY) {
                    try {
                        x.setStationary(duration);
                    } catch (Person.UnsafeMovementStatusChangeException ignored) {
                        ignored.printStackTrace();
                    }
                }
            }
        }

    }

    //Non fa nulla
    @Override
    public void frameAction() { }

    /**
     * Restituisce una breve descrizione dello scenario e dei parametri di cui necessita per essere eseguito.
     * @return  breve descrizione dello scenario.
     */
    @Override
    public ScenarioInfos getInfos() {
        return SCENARIO_INFOS;
    }

}
