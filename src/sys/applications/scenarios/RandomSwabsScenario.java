package sys.applications.scenarios;

import assets.Person;
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
                "Parametri:\n" +
                "\t-numero massimo di persone da testare giornalmente (il numero non sarà fisso sempre).");
        SCENARIO_INFOS.setName("Random Swabs Scenario");
    }

    RandomSwabsScenario(Simulation currentSimulation, int swabs) {
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
            Person x = currentState.startingPopulation[Rng.R.nextInt(currentState.blueBlack+1)];
            if (!currentState.swabs.contains(x)) currentSimulation.getCurrentState().doSwab(x);
        }
    }

    // Non fa nulla
    @Override
    public void frameAction() { }

    /**
     * Restituisce una breve descrizione dello scenario e dei parametri di cui necessita per essere eseguito.
     * @return  oggetto ScenariosInfos contenente le informazioni dello scenario.
     */
    @Override
    public ScenarioInfos getInfos() {
        return SCENARIO_INFOS;
    }
}
