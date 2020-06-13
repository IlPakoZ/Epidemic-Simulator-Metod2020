package sys;

import assets.Person;
import sys.Core.*;
import java.util.ArrayList;
//import java.util.Math;
import java.util.Random;

public class Rng {

    private static final double SEVERITY_PERCENT = 10;
    private static final int YOUNG_RANGE = 29;
    private static final int MIDDLE_AGE_RANGE = 59;
    private static final double YOUNG_MODIFIER= 2;
    private static final double MIDDLE_AGE_MODIFIER = 0;
    private static final double OLD_AGE_MODIFIER = -2;
    private static final double GAUSSIAN_AGE_MODIFIER = 20;

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
    @ToRevise
    public static boolean generateFortune(double percent, double modifier){
        if (modifier == 0) return true;
        double newPercent;
        newPercent = percent + (percent*modifier/100);
        if (newPercent>100) newPercent = percent;
        double isItGonna = Math.random();
        return isItGonna * 100 <= newPercent;
    }

    /**
     * Restituisce un double da utilizzare come modificatore nel metodo
     * generateFortune che modifica la probabilità di morire in base
     * all'età del paziente.
     *
     * @param age   intero che indica l'età del paziente
     * @return      modificatore di probabilità di tipo double
     */
    @ToRevise
    private static double generateSeverityModifiers(int age){
        Random r = new Random();
        double valore;
        if (age <= YOUNG_RANGE) {
            valore = r.nextGaussian()*SEVERITY_PERCENT+YOUNG_MODIFIER;
        }else if (age <= MIDDLE_AGE_RANGE) {
            valore = r.nextGaussian()*SEVERITY_PERCENT+MIDDLE_AGE_MODIFIER;
        }else {
            valore = r.nextGaussian()*SEVERITY_PERCENT+OLD_AGE_MODIFIER;
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
    @ToRevise
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
    @ToRevise
    public static Person[] generatePopulation(State currentState){
        Person[] people = new Person[currentState.configs.populationNumber];
        Random r = new Random();
        for (int i=0;i<currentState.configs.populationNumber;i++){
            double age;
            do {
                age = r.nextGaussian() * GAUSSIAN_AGE_MODIFIER + currentState.configs.ageAverage;
            }while(age<0 || age>currentState.configs.maxAge);
            //people[i] = new Person(currentState.configs.ageAverage, i, currentState);
            people[i] = new Person((int) age, i, currentState);
            people[i].setSeverityModifier(generateSeverityModifiers((int)age));
            people[i].setInfectivityModifier(generateInfectivityModifiers(false));
        }
        currentState.greenIncubation=currentState.configs.populationNumber-1;
        currentState.incubationYellow=currentState.configs.populationNumber-1;
        currentState.yellowRed=currentState.configs.populationNumber-1;
        currentState.redBlue=currentState.configs.populationNumber-1;
        currentState.blueBlack=currentState.configs.populationNumber-1;
        return people;
    }

}
