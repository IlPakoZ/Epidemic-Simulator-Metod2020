package sys;

import assets.Person;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class State {
    // ----------------------------- SIMULATION SETTINGS -----------------------------
    public Config configs;
    public Person[] startingPopulation;
    public int resources;
    public double vd;
    public double r0;
    public HashMap<Person, ArrayList<Person>> contacts;     // Infected person, {set of people met}
    public HashSet<Person> swabs;                           // If present, the person represented by the instance
                                                            // is positive to the swabs
    public boolean unoPatientFound = false;
    public SimulationStatus status = SimulationStatus.NOT_YET_STARTED;     // Simulation status
    public ArrayList<Integer> totalInfected;
    public ArrayList<Integer> dailyInfected;
    // ---------------------------------------------------------------------------------

    // -------------------------------- SYSTEM SETTINGS --------------------------------
    public int greenIncubation;
    public int incubationYellow;
    public int yellowRed;
    public int redBlue;
    public int blueBlack;
    // ---------------------------------------------------------------------------------
}
