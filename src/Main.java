import sys.*;
import sys.applications.CommandLineMenu;

public class Main {
    public static void main(String[] args) {
        Core x = new Core();
        CommandLineMenu c = new CommandLineMenu();
        x.newSimulation(c);
        x.run();
    }




    //x.run();
    //Simulation simulation = x.debug();
    //State state = simulation.getCurrentState();
    //state.startingPopulation[0].debugForceColor(ColorStatus.BLACK);
    //state.startingPopulation[0].debugForceColor(ColorStatus.BLACK);
    //state.startingPopulation[0].debugForceColor(ColorStatus.BLACK);
    //state.startingPopulation[0].debugForceColor(ColorStatus.RED);
    //state.startingPopulation[0].setAsInfected();
    //System.out.println(state.blueBlack);
    //simulation.debugDisableInfections();
    //simulation.doSwab(state.startingPopulation[0]);
    //simulation.doSwab(state.startingPopulation[state.startingPopulation.length-1]);
    //simulation.debugRun();
}
