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

    @Override
    public void oneTimeAction() {

    }

    @Override
    public void dailyAction() {
        currentSimulation.swabQueue(percent);
    }

    @Override
    public void frameAction() {

    }

    @Override
    public int getID() {
        return ID;
    }

    @Override
    public String getInfos() {
        return "In questo scenario, quando si trova una persona infetta, si testano tutte le persone che ha incontrato.\n";
    }

    @Override
    public String getName() {
        return "People Met Gets Tested Scenario\n";
    }
}
