package sys.applications.scenarios;

import assets.Person;
import sys.Core.*;
import sys.Rng;
import sys.Simulation;
import sys.State;
import sys.models.Scenario;

public class RandomSwabsScenario extends Scenario{

    private Simulation currentSimulation;
    private State currentState;
    private int swabsNumber;
    public static final ScenarioInfos SCENARIO_INFOS = new ScenarioInfos(1);

    static {
        SCENARIO_INFOS.setInfos("In questo scenario, ogni giorno vengono testate un massimo numero preso in input di persone, scelte casualmente.\n" +
                "\nParametri:\n" +
                "\t1) numero massimo di persone da testare giornalmente (il numero non sarà fisso ogni giorno).");
        SCENARIO_INFOS.setName("Random Swabs Scenario");
    }

    public RandomSwabsScenario(Simulation currentSimulation, int swabs) {
        super(currentSimulation);
        this.currentSimulation = currentSimulation;
        this.currentState = currentSimulation.getCurrentState();
        this.swabsNumber = swabs;
    }

    // Non fa nulla
    @Ready
    @Override
    public void oneTimeAction() { if (swabsNumber>currentState.configs.getPopulationNumber()) swabsNumber = currentState.configs.getPopulationNumber(); }

    /**
     * Fa il tampone ad un numero preso in input di persone, scelte casualmente tra la popolazione.
     */
    @Ready
    @Override
    public void dailyAction() {
        int[] indexes = Rng.getPersonShuffledIndex(currentState);
        int indexPerson = 0;
        for (int i=0; i<swabsNumber; i++) {
            if (indexPerson == currentState.configs.getPopulationNumber()) break;
            Person x = currentState.startingPopulation[indexes[indexPerson]];
            if (!currentState.swabs.contains(x)) currentSimulation.getCurrentState().doSwab(x);
            else i--;
            indexPerson++;
        }
    }

    // Non fa nulla
    @Override
    @Ready
    public void frameAction() { }

    /**
     * Restituisce una breve descrizione dello scenario e dei parametri di cui necessita per essere eseguito.
     * @return  oggetto ScenariosInfos contenente le informazioni dello scenario.
     */
    @Override
    @Ready
    public ScenarioInfos getInfos() {
        return SCENARIO_INFOS;
    }
}
