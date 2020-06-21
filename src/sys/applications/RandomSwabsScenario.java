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
    private static final Random R = new Random();
    private int swabsNumber;

    public RandomSwabsScenario(Simulation currentSimulation, int swabs) {
        super(currentSimulation);
        this.currentSimulation = currentSimulation;
        this.currentState = currentSimulation.getCurrentState();
        swabsNumber = swabs;
    }

    // Non fa nulla
    @Override
    public void oneTimeAction() { }

    /**
     * Fa il tampone ad un numero preso in input di persone, scelte casualmente tra la popolazione.
     */
    @Override
    public void dailyAction() {
        for (int i = 0; i < swabsNumber; i++) {
            Person x = currentState.startingPopulation[R.nextInt(currentState.blueBlack+1)];
            if (!currentState.swabs.contains(x)) currentSimulation.doSwab(x);
        }
    }

    // Non fa nulla
    @Override
    public void frameAction() { }

    /**
     * Restituisce l'ID univoco che identifica uno scenario.
     * @return  ID dello scenario.
     */
    @Override
    public int getID() {
        return ID;
    }

    /**
     * Restituisce una breve descrizione dello scenario e dei parametri di cui necessita per essere eseguito.
     * @return  breve descrizione dello scenario.
     */
    @Override
    public String getInfos() {
        return "In questo scenario, ogni giorno vengono testate un massimo numero preso in input di persone, scelte casualmente.\n" +
                "Parametri:\n" +
                "\t-numero massimo di persone da testare giornalmente (il numero non sarà fisso sempre).";
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
