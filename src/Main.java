import assets.ColorStatus;
import sys.*;
import sys.applications.CommandLineMenu;

import java.awt.*;

public class Main {
    public static void main(String[] args) {
        Core x = new Core();
        CommandLineMenu c = new CommandLineMenu();
        x.newSimulation(new CommandLineMenu());
        x.debug();
    /*
        State state = new State();
        state.configs = new Config();
        state.configs.populationNumber = 100;
        state.startingPopulation = Rng.generatePopulation(state);
        state.startingPopulation[0].setAsInfected();
        state.startingPopulation[0].setAsInfected();
        state.startingPopulation[0].setAsInfected();
        printResults(state);
        state.startingPopulation[97].makeOfColor(ColorStatus.YELLOW);
        printResults(state);
        state.startingPopulation[99].makeOfColor(ColorStatus.BLUE);
        printResults(state);
        state.startingPopulation[95].setAsInfected();
        state.startingPopulation[96].makeOfColor(ColorStatus.YELLOW);
        state.startingPopulation[98].makeOfColor(ColorStatus.RED);
        printResults(state);
        state.startingPopulation[98].makeOfColor(ColorStatus.BLACK);
        printResults(state);
        state.startingPopulation[92].setAsInfected();
        printResults(state);
        state.startingPopulation[97].makeOfColor(ColorStatus.YELLOW);
        printResults(state);
        state.startingPopulation[97].makeOfColor(ColorStatus.BLUE);
        printResults(state);
    }

     private static void printResults(State state){
            for (int i=0;i<state.configs.populationNumber;i++){
                System.out.println("La persona con indice " + i + (!state.startingPopulation[i].isInfected() ? " NON" : "")+ " è infetta ed è di colore "+ state.startingPopulation[i].color + "!");
            }
            System.out.println("\nDi seguito sono stampati gli indici della esapartizione:\n" +
                    "VERDI: da 0 a "+ state.incubationYellow +"\n"+
                    "GIALLI: da "+ (state.incubationYellow+1) + " a " + state.yellowRed+"\n"+
                    "ROSSI: da "+ (state.yellowRed+1)+ " a " + state.redBlue+"\n"+
                    "BLU: da " + (state.redBlue+1) + " a " + state.blueBlack+ "\n"+
                    "NERI: da "+ (state.blueBlack+1) + " a " + (state.configs.populationNumber-1));

        */
    }
}
