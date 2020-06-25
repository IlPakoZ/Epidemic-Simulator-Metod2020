package sys;

import assets.ColorStatus;
import assets.Person;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import sys.Core.*;

public class State {
    // ----------------------------- SIMULATION SETTINGS -----------------------------
    public Config configs;
    public Config backupConfigs;
    public Person[] startingPopulation;
    public int resources;
    public HashMap<Person, HashSet<Person>> contacts;       // Infected person, {set of people met}
    public HashSet<Person> swabs;                           // If present, the person represented by the instance
                                                            // is positive to the swab
    public boolean unoPatientFound = false;
    public SimulationStatus status = SimulationStatus.NOT_YET_STARTED;     // Simulation status
    public ArrayList<ArrayList<Integer>> total;
    public ArrayList<ArrayList<Integer>> daily;
    public int totalSwabsNumber = 0;
    public int currentDay = 0;
    public PeopleIndexList[][] space;
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
     * è minore di zero, allora viene impostato a zero.
     *
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
     *
     * @param el    codice
     * @return      un intero che segnala al menù cosa eseguire
     */
    @Ready
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
            case "0":
                return -1;
            case "WEAREEUROPEAN":
                configs.forceInitialResources(2147483647);
                return 1;
            case "THEYSTEALOURMONEY":
                configs.forceInitialResources(50000);
                configs.forcePopulationNumber(20000);
                configs.forceSize(200, 200);
                configs.forceSwabsCost(10);
                configs.forceAge(30, 1);
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

    @Ready
    public boolean isPoorCountry(){
        return poorCountry;
    }

    @Ready
    public boolean isBigBrother(){
        return bigBrother;
    }

    /**
     * Restituisce il numero di persone sane.
     *
     * @return  numero di persone sane
     */
    @Ready
    public int getHealthyNumber(){
        return greenIncubation+1;
    }

    /**
     * Restituisce il numero di persone in incubazione.
     *
     * @return  numero di persone in incubazione
     */
    @Ready
    public int getIncubationNumber(){
        return incubationYellow - greenIncubation;
    }

    /**
     * Restituisce il numero di persone asintomatiche.
     *
     * @return  numero di persone asintomatiche
     */
    @Ready
    public int getAsymptomaticNumber(){ return yellowRed - incubationYellow; }

    /**
     * Restituisce il numero di persone sintomatiche.
     *
     * @return  numero di persone sintomatiche
     */
    @Ready
    public int getSymptomaticNumber(){ return redBlue - yellowRed; }

    /**
     * Restituisce il numero di persone guarite.
     *
     * @return  numero di persone guarite
     */
    @Ready
    public int getCuredNumber(){
        return blueBlack - redBlue;
    }

    /**
     * Restituisce il numero di persone morte.
     *
     * @return  numero di persone morte
     */
    @Ready
    public int getDeathsNumber(){
        return configs.getPopulationNumber()-blueBlack-1;
    }

    /**
     * Restituisce l'età media delle persone comprese tra due indici
     * (startIndex e endIndex) presi in input.
     *
     * @param startIndex    l'indice dal quale iniziare a calcolare l'età media
     * @param endIndex      l'indice al quale finire di calcolare l'età media
     * @return              l'età media delle persone comprese tra startIndex e endIndex
     */
    @Ready
    public int getCurrentAgeAverage(int startIndex, int endIndex) {
        int totalAge = 0;
        if ((endIndex > configs.getPopulationNumber()) || (startIndex < 0)) throw new InvalidParameterException("Illegal parameter.");
        for (int i=startIndex; i< endIndex; i++) totalAge+= startingPopulation[i].getAge();
        return totalAge/ (endIndex-startIndex);
    }

    /**
     * Restituisce il numero totale di tamponi effettuati.
     *
     * @return  il numero totale di tamponi effettuati
     */
    @Ready
    public int getTotalSwabsNumber() {return totalSwabsNumber;}


    /**
     * Toglie denaro dalle casse dello Stato per effettuare un tampone
     * sulla persona. Effettuato il tampone, se la persona è nella
     * lista dei contatti, si può decidere di eseguire un tampone
     * anche ai contatti (contact tracing). Il tampone ha una certa
     * probabilità di fallire. Il metodo "generateFortune" in Rng
     * calcola le probabilità di riuscita del tampone.
     * Se il tampone è positivo, la persona viene fermata.
     * Il tampone verrà usato o meno in base allo scenario che si sceglie.
     *
     * @param p1    persona a cui sottoporre il tampone.
     * @return      true se la persona è positiva al tampone, false altrimenti.
     */
    @Ready
    public boolean doSwab(Person p1){
        boolean result = false;
        if (p1.color == ColorStatus.RED) {
            result = true;
        } else {
            totalSwabsNumber++;
            subtractResources(configs.getSwabsCost());
            if (p1.color == ColorStatus.YELLOW) {
                result = true;
            }
            if (!result) return false;
            if (!Rng.generateFortune(Config.SWAB_SUCCESS_RATE, 1)) {
                result = false;
            } else {
                if (!swabs.contains(p1))
                    p1.setStationary(configs.getDiseaseDuration() - configs.incubationToYellowDeadline);
            }
        }
        if (result) {
            swabs.add(p1);
            if (contacts.containsKey(p1)) {
                for (Person person : contacts.get(p1)) {
                    if (!swabs.contains(person)) swabPersons.enqueue(person);
                }
            }
        }

        return result;
    }


    /**
     * Fa il tampone alle persone scelte per quel determinato giorno e, se una di queste
     * risulta positiva, aggiunge alla coda le persone con cui è entrata in contatto.
     *
     * @param percent   percentuale di fare il tampone ad una persona.
     *
     */
    @Ready
    public void swabQueue(double percent){
        int oldSize = swabPersons.getSize();
        for (int i = 0; i < oldSize; i++) {
            Person x = swabPersons.dequeue();
            if (!swabs.contains(x) && Rng.generateFortune(percent, 1)) {
                doSwab(x);
            }
        }
    }
}
