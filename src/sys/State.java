package sys;

import assets.Person;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import javafx.util.Pair;
import sys.Core.*;

public class State {
    // ----------------------------- SIMULATION SETTINGS -----------------------------
    public Config configs;
    public Config backupConfigs;
    public Person[] startingPopulation;
    public int resources;
    public HashMap<Person, ArrayList<Person>> contacts;     // Infected person, {set of people met}
    public HashSet<Person> swabs;                           // If present, the person represented by the instance
                                                            // is positive to the swab
    public boolean unoPatientFound = false;
    public SimulationStatus status = SimulationStatus.NOT_YET_STARTED;     // Simulation status
    public ArrayList<ArrayList<Integer>> total;
    public ArrayList<ArrayList<Integer>> daily;
    public int currentDay = 0;
    public boolean defaultScenario = true;
    public PersonList[][] space;
    public int currentlyStationary = 0;
    public Queue<Person> swabPersons;
    private boolean poorCountry = false;
    private boolean bigBrother = false;
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

    /**
     * Utilizza dei codici pre-generati per eseguire
     * delle sub-routine e modificare alcuni attributi.
     * Fa il backup delle configurazioni correnti per
     * poterle ricaricare in seguito.
     * Se le configurazioni vengono modificate, il backup
     * viene perso.
     * @param el    codice
     * @return      un intero che segnala al menù cosa eseguire
     */
    @ToRevise
    public int checkExCode(String el){
        poorCountry = false;
        bigBrother = false;
        //Se non c'è già un backup, creane uno nuovo
        if (configs.haveConfigsChanged()) {
            configs.setConfigsChanged(false);
            backupConfigs = new Config();
            backupConfigs.copy(configs);
        }

        switch (el) {
            case "WEAREEUROPEAN":
                configs.initialResources = 2147483647;
                return 1;
            case "THEYSTEALOURMONEY":
                configs.initialResources = 5000;
                configs.populationNumber = 200000;
                configs.size = new Pair<>(200, 200);
                configs.velocity = 2;
                configs.ageAverage = 30;
                poorCountry = true;
                return 2;
            case "BIGBROTHERISWATCHINGYOU":
                bigBrother = true;
                return 3;
            default:
                //Ricarica i backup
                configs = backupConfigs;
                configs.setConfigsChanged(false);
                return 0;
        }
    }

    public boolean isPoorCountry(){
        return poorCountry;
    }

    public boolean isBigBrother(){
        return bigBrother;
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
