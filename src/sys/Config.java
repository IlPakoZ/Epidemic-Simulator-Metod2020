package sys;

import javafx.util.Pair;
import sys.Core.*;

public class Config {

    // ----------------------------- SIMULATION PARAMETERS (COMPULSORY) -----------------------------

    public int populationNumber;
    public int initialResources;
    public int swabsCost;
    public int infectivity;
    public int sintomaticity;
    public int letality;
    public int diseaseDuration;
    public Pair<Integer,Integer> size;                    // width, height


    // ----------------------------- SIMULATION PARAMETERS (OPTIONAL) -------------------------------

    public int dayDuration = 500;                         //Loop duration measured in milliseconds
    public double velocity = 1;
    public int socialDistance;
    boolean masks = false;
    public int frameADay = 25;
    public int maxAge = 110;
    public int ageAverage = 50;


    // ----------------------------------------- CONSTANTS ------------------------------------------

    //DO NOT EDIT THOSE IF YOU DON'T KNOW WHAT YOU'RE DOING

    public static final double SWAB_SUCCESS_RATE = 99;
    public static final int DAILY_COST_IF_STATIONARY = 1;
    public static final double INCUBATION_TO_YELLOW_DEADLINE = 1/6.0;
    public static final double YELLOW_TO_RED_DEADLINE = 1/3.0;
    public static final int POPULATION_NUMBER_UPPER_BOUND = 100000;
    public static final int POPULATION_NUMBER_LOWER_BOUND = 2;
    public static final int DISEASE_DURATION_UPPER_BOUND = 90;
    public static final int DISEASE_DURATION_LOWER_BOUND = 6;
    public static final int RESOURCES_LOWER_BOUND = 1;

    // --------------------------------------- WORK VARIABLES ---------------------------------------

    private boolean isValid = true;
    public int incubationToYellowDeadline;  //Deve essere impostato a currentState.configs.diseaseDuration*Config.INCUBATION_TO_YELLOW_DEADLINE (facendo casting ad intero)
    public int yellowToRedDeadline;         //Deve essere impostato a currentState.configs.diseaseDuration*Config.YELLOW_TO_RED_DEADLINE (facendo casting ad intero)
    private boolean configsChanged = false;

    // ----------------------------------------------------------------------------------------------

    /**
     * Se una configurazione è pronta per l'esecuzione,
     * copia nella seguente configurazione i dati contenuti
     * nella configurazione passata come parametro.
     *
     * @param c     configurazione da cui copiare i parametri.
     */
    void copy(Config c){
        if (isValid){
            this.ageAverage = c.ageAverage;
            this.socialDistance = c.socialDistance;
            this.populationNumber = c.populationNumber;
            this.initialResources = c.initialResources;
            this.swabsCost = c.swabsCost;
            this.infectivity = c.infectivity;
            this.sintomaticity = c.sintomaticity;
            this.letality = c.letality;
            this.diseaseDuration = c.diseaseDuration;
            this.dayDuration = c.dayDuration;
            this.masks = c.masks;
            this.maxAge = c.maxAge;
        }
    }

    /**
     * Permette di settare la configurazione come
     * valida o non valida: True se valida, False se
     * non valida.
     *
     * @param status    nuovo stato della configurazione.
     */
    void validate(boolean status){
        isValid = status;
    }

    /**
     * Controlla se il numero della popolazione in input
     * è regolare ed entro i limiti del programma, se si,
     * inserisce il dato e restituisce true, altrimenti
     * restituisce false.
     *
     * @param number   input populationNumber
     * @return  true se il dato è valido, false altrimenti
     */
    @ToRevise
    public boolean setPopulationNumber(int number){
        configsChanged = true;
        if (number > POPULATION_NUMBER_UPPER_BOUND || number < POPULATION_NUMBER_LOWER_BOUND){
            return false;
        }
        populationNumber = number;
        return true;
    }

    /**
     * Quando richiamata, restituisce l'attributo
     * del numero della popolazione.
     *
     * @return  numero della popolazione
     */
    public int getPopulationNumber(){
        return populationNumber;
    }

    /**
     * Controlla se la durata della malattia in input
     * è regolare ed entro i limiti del programma, se si,
     * inserisce il dati e restituisce true, altrimenti
     * restituisce false.
     *
     * @param number   input diseaseDuration
     * @return         true se il valore è nel formato corretto, false altrimenti.
     */
    @ToRevise
    public boolean setDiseaseDuration(int number){
        configsChanged = true;
        if (number > DISEASE_DURATION_UPPER_BOUND || number <= DISEASE_DURATION_LOWER_BOUND){
            return false;
        }
        diseaseDuration = number;
        incubationToYellowDeadline = (int) (diseaseDuration * Config.INCUBATION_TO_YELLOW_DEADLINE);
        yellowToRedDeadline = (int) (diseaseDuration * Config.YELLOW_TO_RED_DEADLINE);
        return true;
    }

    /**
     *  Quando richiamata, restituisce l'attributo
     *  della durata della malattia.
     * @return  durata della malattia
     */
    public int getDiseaseDuration(){
        return diseaseDuration;
    }

    /**
     * Controlla se le risorse iniziali in input
     * sono regolari ed entro i limiti del programma, se si,
     * inserisce il dato e restituisce true, altrimenti
     * restituisce false.
     *
     * @param number    input initialResources
     * @return          true se il valore è nel formato corretto, false altrimenti.
     */
    public boolean setInitialResources(int number){
        configsChanged = true;
        if (number >= (populationNumber * diseaseDuration) || number <= RESOURCES_LOWER_BOUND){
            return false;
        }
        initialResources = number;
        return true;
    }

    /**
     *  Quando richiamata, restituisce l'attributo
     *  delle risorse iniziali.
     *
     * @return  risorse iniziali
     */
    public int getInitialResources(){
        return initialResources;
    }

    /**
     * Controlla se il costo del tampone in input
     * è regolare ed entro i limiti del programma, se si,
     * inserisce il dato e restituisce true, altrimenti
     * restituisce false.
     *
     * @param number    input swabsCost
     * @return          true se il valore è nel formato corretto, false altrimenti.
     */
    public boolean setSwabsCost(int number){
        configsChanged = true;
        if (number <= (initialResources/(populationNumber*10))){
            return false;
        }
        swabsCost = number;
        return true;
    }

    /**
     *  Quando richiamata, restituisce l'attributo
     *  del costo del tampone.
     *
     * @return  costo del tampone
     */
    public int getSwabsCost(){
        return swabsCost;
    }

    /**
     * Controlla se la percentuale di infettività
     * è regolare ed entro i limiti del programma, se si,
     * inserisce il dato e restituisce true, altrimenti
     * restituisce false.
     *
     * @param number    input infectivity
     * @return          true se il valore è nel formato corretto, false altrimenti.
     */
    public boolean setInfectivity(int number){
        configsChanged = true;
        if (number <= 0.0 || number > 100.0){
            return false;
        }
        infectivity = number;
        return true;
    }

    /**
     *  Quando richiamata, restituisce l'attributo
     *  dell'infettività.
     *
     * @return  infettività
     */
    public int getInfectivity(){
        return infectivity;
    }

    /**
     * Controlla se la percentuale di sintomaticità
     * è regolare ed entro i limiti del programma, se si,
     * inserisce il dato e restituisce true, altrimenti
     * restituisce false.
     *
     * @param number    input sintomaticity
     * @return          true se il valore è nel formato corretto, false altrimenti.
     */

    public boolean setSintomaticity(int number){
        configsChanged = true;
        if (number <= 0.0 || number > 100.0){
            return false;
        }
        sintomaticity = number;
        return true;
    }

    /**
     *  Quando richiamata, restituisce l'attributo
     *  della sintomacity.
     *
     * @return  sintomaticità
     */
    public int getSintomaticity(){
        return sintomaticity;
    }

    boolean haveConfigsChanged() { return configsChanged; }

    void setConfigsChanged(boolean changed) {this.configsChanged = changed;}
}
