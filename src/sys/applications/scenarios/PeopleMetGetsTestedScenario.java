package sys.applications.scenarios;

import sys.Simulation;
import sys.models.Scenario;

public class PeopleMetGetsTestedScenario extends Scenario {

    private Simulation currentSimulation;
    private double percent;
    public static final ScenarioInfos SCENARIO_INFOS = new ScenarioInfos(2);

    static {
        SCENARIO_INFOS.setInfos("In questo scenario, quando viene fatto il tampone ad una persona infetta e risulta positiva, si testano le persone che ha incontrato.\n" +
                "\nParametri:\n" +
                "\t1) probabilità in percentuale di fare il tampone ad una persona (0<x<=100).");
        SCENARIO_INFOS.setName("People Met Gets Tested Scenario");
    }

    public PeopleMetGetsTestedScenario(Simulation currentSimulation, double percent) {
        super(currentSimulation);
        this.currentSimulation = currentSimulation;
        this.percent = percent;
    }

    // Non fa nulla
    @Override
    public void oneTimeAction() {
    }

    /**
     * Ogni giorno fa i tamponi alle persone che sono entrate in contatto con chi risulta positivo al tampone.
     */
    @Override
    public void dailyAction() {
        currentSimulation.getCurrentState().swabQueue(percent);
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