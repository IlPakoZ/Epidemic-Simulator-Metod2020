package sys;

import assets.Person;
import sys.Core.*;
import java.util.ArrayList;

public class Rng {
    /**
     * Restituisce un booleano che indica se l'evento casuale
     * deve essere eseguito oppure no. In base ad una certa
     * probabilità specificata come parametro (double da 0 a 100)
     * e ad un modificatore (double da 0 a 1) che rende l'evento
     * più o meno probabile, restituisce true se l'evento deve
     * essere eseguito e false se non deve essere eseguito.
     * Può essere usato per un calcolo generico di probabilità.
     *
     * @param percent   double da 0 a 100 che specifica la probabilità che l'evento dia risultato positivo
     * @param modifier  double da 0 (evento certo) a 2 (evento meno probabile) che modifica la probabilità
     *                  che l'evento dia risultato positivo. Viene moltiplicato al numero massimo generato
     *                  in modo tale che un numero basso aumenti le possibilità che l'evento venga eseguito e uno
     *                  alto le diminuisca fino a dimezzarle.
     * @return          se l'evento deve essere eseguito o no
     */
    @NotImplemented
    public static boolean generateFortune(double percent, double modifier){ return false; }

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
        return 0;
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
        return 0;
    }

    /**
     * Data una media di età e un'età massima per la popolazione,
     * restituisce un ArrayList di persone la cui età è distribuita
     * seguendo una distribuzione Gaussiana. La generazione della popolazione
     * avviene immediatamente prima dell'inizio della simulazione e utilizza il
     * parametro "dim" di distanziamento sociale per fornire un numero casuale di
     * default alle persone che corrisponderà alla propria "casa".
     * @param mean      età media (trend) della popolazione
     * @param maxAge    età massima della popolazione
     * @param dim       distanziamento sociale, serve per il calcolo delle "case"
     * @return          persone con età generata casualmente
     */
    @NotImplemented
    private static ArrayList<Person> generatePopulation(int mean, int maxAge, int dim){ return new ArrayList<>();}

}
