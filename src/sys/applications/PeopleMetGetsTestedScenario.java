package sys.applications;

import assets.Person;
import sys.Simulation;
import sys.Core.*;
import sys.State;
import sys.models.Scenario;

public class PeopleMetGetsTestedScenario extends Scenario{

    private Simulation currentSimulation;
    private double percent;
    private static final int ID = 2;

    public PeopleMetGetsTestedScenario(Simulation currentSimulation, double percent) {
        super(currentSimulation);
        this.currentSimulation = currentSimulation;
        this.percent = percent;
    }

    // Non fa nulla
    @Override
    public void oneTimeAction() { }

    /**
     * Ogni giorno fa i tamponi alle persone che sono entrate in contatto con chi risulta positivo al tampone.
     */
    @Override
    public void dailyAction() {
        currentSimulation.swabQueue(percent);
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
        return "In questo scenario, quando viene fatto il tampone ad una persona infetta e risulta positiva, si testano le persone che ha incontrato.\n" +
                "Parametri:\n" +
                "\t-probabilità in percentuale di fare il tampone ad una persona (0<x<100).";
    }

    /**
     * Restituisce il nome dello scenario.
     * @return  nome dello scenario.
     */
    @Override
    public String getName() {
        return "People Met Gets Tested Scenario\n";
    }
}
