package sys;

import assets.Person;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import sys.Core.*;

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
    public int currentDay = 0;
    public boolean defaultScenario = true;
    public PersonList[][] space;
    public int currentlyStationary = 0;

    // ---------------------------------------------------------------------------------

    // -------------------------------- SYSTEM SETTINGS --------------------------------
    public int greenIncubation;
    public int incubationYellow;
    public int yellowRed;
    public int redBlue;
    public int blueBlack;
    // ---------------------------------------------------------------------------------

    /**
     * Sottrae value dalle risorse. Se il nuovo valore calcolato
     * è minore di zero, allora impostalo a zero.
     * @param value     risorse da togliere
     * @return          restituisce True se ci sono ancora risorse disponibili, False altrimenti
     */
    @Ready
    public boolean subtractResources(int value){
        if (resources - value > 0) {
            resources -= value;
            return true;
        }
        resources = 0;
        return false;
    }

    @Ready
    public int getHealthyNumber(){
        return greenIncubation+1;
    }
    @Ready
    public int getIncubationNumber(){
        return incubationYellow - greenIncubation;
    }
    @Ready
    public int getAsymptomaticNumber(){ return yellowRed - incubationYellow; }
    @Ready
    public int getSymptomaticNumber(){ return redBlue - yellowRed; }
    @Ready
    public int getCuredNumber(){
        return blueBlack - redBlue;
    }
    @Ready
    public int getDeathsNumber(){
        return configs.populationNumber-blueBlack-1;
    }

    @Ready
    public int getCurrentAgeAverage(int startIndex, int endIndex) {
        int totalAge = 0;
        if ((endIndex > configs.populationNumber) || (startIndex < 0)) throw new InvalidParameterException("Illegal parameter.");
        for (int i=startIndex; i< endIndex; i++) totalAge+= startingPopulation[i].getAge();
        return totalAge/ (endIndex-startIndex);
    }

}
