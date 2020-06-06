package sys;

import assets.Person;
import sys.Core.*;
import java.util.ArrayList;
//import java.util.Math;
import java.util.Random;

public class Rng {
    /**
     * Restituisce un booleano che indica se l'evento casuale
     * deve essere eseguito oppure no. In base ad una certa
     * probabilità specificata come parametro (double da 0 a 100)
     * e ad un modificatore (double da 0 a 2) che rende l'evento
     * più o meno probabile, restituisce true se l'evento deve
     * essere eseguito e false se non deve essere eseguito.
     * Può essere usato per un calcolo generico di probabilità.
     *
     * @param percent   double da 0 a 100 che specifica la probabilità che l'evento dia risultato positivo
     * @param modifier  double da 0 (evento certo) a 2 (evento meno probabile) che modifica la probabilità
     *                  che l'evento dia risultato positivo. Viene diviso al numero massimo generato
     *                  in modo tale che un numero basso aumenti le possibilità che l'evento venga eseguito e uno
     *                  alto le diminuisca fino a dimezzarle.
     * @return          se l'evento deve essere eseguito o no
     */
    @NotImplemented
    public static boolean generateFortune(double percent, double modifier){
        if (modifier == 0) return true;
        double newPercent = percent/modifier;
        double isItGonna = Math.random();
        if (isItGonna*100 <= newPercent) return true;
        return false;
    }

    /**
     * Restituisce un double da utilizzare come modificatore nel metodo
     * generateFortune che modifica la probabilità di morire in base
     * all'età del paziente.
     *
     * @param age   intero che indica l'età del paziente
     * @return      modificatore di probabilità di tipo double
     */
    @NotImplemented
    private static double generateDeathModifiers(int age){
        Random r = new Random();
        double valore = 0;
        if (age <= 29) {
            valore = r.nextGaussian()+1.5;
            if (valore>2)
                valore = 2;
            if (valore<0)
                valore = 0;
        }else if (age <= 59) {
            valore = r.nextGaussian()+0.8;
            if (valore>2)
                valore = 2;
            if (valore<0)
                valore = 0;
        }else {
            valore = r.nextGaussian()+0.1;
            if (valore>2)
                valore = 2;
            if (valore<0)
                valore = 0;
        }
        return valore;
    }

    /**
     * Restituisce un double da utilizzare come modificatore nel motodo
     * generateFortune che modifica la probabilità di contagiare qualcuno
     * in base a se possiede la mascherina o no.
     *
     * @param mask  booleano che indica se possiede la mascherina o no
     * @return      modificatore di probabilità di tipo double
     */
    @NotImplemented
    private static double generateInfectivityModifiers(boolean mask){
        if (mask) return 2;
        else return 1;
    }

    /**
     * Data una media di età e un'età massima per la popolazione,
     * restituisce un ArrayList di persone la cui età è distribuita
     * seguendo una distribuzione Gaussiana. La generazione della popolazione
     * avviene immediatamente prima dell'inizio della simulazione e utilizza il
     * parametro currentState della simulazione per generare un certo numero
     * di persone di una determinata età.
     *
     * @return  restituisce l'array di persone
     */
    @NotImplemented
    public static Person[] generatePopulation(State currentState){
        Person[] people = new Person[currentState.configs.populationNumber];
        for (int i=0;i<currentState.configs.populationNumber;i++){
            people[i] = new Person(currentState.configs.ageAverage, i, currentState);
        }
        currentState.greenIncubation=currentState.configs.populationNumber-1;
        currentState.incubationYellow=currentState.configs.populationNumber-1;
        currentState.yellowRed=currentState.configs.populationNumber-1;
        currentState.redBlue=currentState.configs.populationNumber-1;
        currentState.blueBlack=currentState.configs.populationNumber-1;
        return people;
    }

}
