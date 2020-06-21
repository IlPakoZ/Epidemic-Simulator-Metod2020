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

    //Non fa nulla
    @Override
    public void oneTimeAction() {

    }

    /**
     * Fa il tampone ad un numero preso in input di persone, scelte casualmente tra la popolazione.
     */
    @Override
    public void dailyAction() {
        Random r = new Random();
        for (int i = 0; i < swabsNumber; i++) {
            Person x = currentState.startingPopulation[r.nextInt(currentState.blueBlack+1)];
            if (!currentState.swabs.contains(x)) currentSimulation.doSwab(x);
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
        return "In questo scenario, ogni giorno vengono testate un numero preso in input di persone, scelte casualmente.\n";
    }

    /**
     * Restituisce il nome dello scenario.
     * @return  nome dello scenario.
     */
    @Override
    public String getName() {
        return "Random Swabs Scenario\n";
    }
}
