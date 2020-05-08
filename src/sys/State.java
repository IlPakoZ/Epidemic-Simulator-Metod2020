package sys;

import assets.Person;
import assets.SimulationStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class State {
    // ----------------------------- SIMULATION PARAMETERS -----------------------------
    public Config configs;
    public Person[] startingPopulation;
    public double vd;
    public HashMap<Person, HashSet<Person>> contacts;     //Person, {set of infected people met}
    public HashMap<Person, Boolean> swabs;                //True (positive), False (not tested/negative)
    public boolean unoPatientFound = false;
    public SimulationStatus STATUS = SimulationStatus.NOT_YET_STARTED;     //Simulation status
    public ArrayList<Integer> totalInfected;
    public ArrayList<Integer> dailyInfected;
    // ---------------------------------------------------------------------------------

    // -------------------------------- SYSTEM SETTINGS --------------------------------
    int greenYellow;
    int yellowRed;
    int redBlue;
    int blueBlack;
    // ---------------------------------------------------------------------------------
}
