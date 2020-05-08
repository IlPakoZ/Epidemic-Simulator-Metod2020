package sys;

import assets.Person;
import assets.SimulationStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class State {
    // ----------------------------- SIMULATION PARAMETERS -----------------------------
    Config configs;
    Person[] startingPopulation;
    double vd;
    int dayDuration = 500;                         //Loop duration measured in milliseconds
    HashMap<Person, HashSet<Person>> contacts;     //Person, {set of infected people met}
    HashMap<Person, Boolean> swabs;                //True (positive), False (not tested/negative)
    boolean unoPatientFound = false;
    SimulationStatus STATUS = SimulationStatus.NOT_YET_STARTED;     //Game status
    ArrayList<Integer> totalInfected;
    ArrayList<Integer> dailyInfected;
    // ---------------------------------------------------------------------------------

    // -------------------------------- SYSTEM SETTINGS --------------------------------
    int greenYellow;
    int yellowRed;
    int redBlue;
    int blueBlack;
    // ---------------------------------------------------------------------------------
}
