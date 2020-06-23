package sys;

import javafx.util.Pair;
import sys.Core.*;

import java.util.function.Function;
import java.util.function.IntSupplier;

public class Config {

    // ----------------------------- SIMULATION PARAMETERS (COMPULSORY) -----------------------------

    private int populationNumber;
    private int initialResources;
    private int swabsCost;
    private double infectivity;
    private double sintomaticity;
    private double letality;
    private int diseaseDuration;
    private int[] size = new int[] {0,0};                  // width, height


    // ----------------------------- SIMULATION PARAMETERS (OPTIONAL) -------------------------------

    private int dayDuration = 500;                         //Loop duration measured in milliseconds
    private double velocity = 1;
    private int socialDistance;
    private int frameADay = 25;
    private int maxAge = 110;
    private int ageAverage = 50;
    private String outputTotalFile = "total.png";
    private String outputDailyFile = "daily.png";

    // ------------------------------------- CONSTANTS & BOUNDS --------------------------------------

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
    public static final int MAX_AGE_LOWER_BOUND = 50;
    public static final int MAX_AGE_UPPER_BOUND = 110;
    public static final int AGE_AVERAGE_LOWER_BOUND = 20;
    public static final int AGE_AVERAGE_UPPER_BOUND = 80;
    public Function<Integer, Integer> SizeLowerBound = ((i)-> Math.max(2, (size[1-i] != 0 ? (populationNumber / 10) / size[1-i] : 2)));
    public Function<Integer, Integer> SizeUpperBound = ((i)-> (populationNumber * 10) / (size[1-i]==0?4:size[1-i]));
    public Function<Integer, Integer> PreferredSizeBound = ((i)-> ((int)(size[1-i]!=0 ? populationNumber*4/size[1-i]: Math.sqrt(populationNumber * 2))));

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
    public double getInfectivity(){
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
    public double getSintomaticity(){
        return sintomaticity;
    }

    /**
     * Controlla se la percentuale di letalità
     * è regolare ed entro i limiti del programma, se si,
     * inserisce il dato e restituisce true, altrimenti
     * restituisce false.
     *
     * @param number    input letality
     * @return          true se il valore è nel formato corretto, false altrimenti.
     */
    public boolean setLetality(int number){
        if (number <= 0.0 || number > 100.0){
            return false;
        }
        letality = number;
        configsChanged = true;
        return true;
    }

    /**
     *  Quando richiamata, restituisce l'attributo
     *  della letality.
     *
     * @return  letalità
     */
    public double getLetality() { return letality; }

    /**
     * Controlla se l'età massima è regolare
     * ed entro i limiti del programma, se si,
     * inserisce il dato e restituisce true, altrimenti
     * restituisce false.
     *
     * @param number    input maxAge
     * @return          true se il valore è nel formato corretto, false altrimenti.
     */
    public boolean setMaxAge(int number){
        if (number < 50 || number > 110){
            return false;
        }
        maxAge = number;
        configsChanged = true;
        return true;
    }

    /**
     *  Quando richiamata, restituisce l'attributo
     *  dell'età massima.
     *
     * @return  età massima.
     */
    public int getMaxAge() { return maxAge; }

    /**
     * Controlla se l'età media è regolare
     * ed entro i limiti del programma, se si,
     * inserisce il dato e restituisce true, altrimenti
     * restituisce false.
     *
     * @param number    input ageAverage
     * @return          true se il valore è nel formato corretto, false altrimenti.
     */
    public boolean setAgeAverage(int number){
        if (number < 20 || number > 80){
            return false;
        }
        ageAverage = number;
        configsChanged = true;
        return true;
    }

    /**
     *  Quando richiamata, restituisce l'attributo
     *  dell'ageAverage.
     *
     * @return  età media
     */
    public int getAgeAverage() { return ageAverage; }

    /**
     * Controlla se la larghezza è regolare
     * ed entro i limiti del programma,
     * se sì, inserisce il dato e resituisce true,
     * altrimenti false.
     *
     * @param number    input width
     * @return          true se il valore è nel formato corretto, false altrimenti.
     */
    public boolean setSizeX(int number) {
        if (number >= SizeLowerBound.apply(0) && number <= SizeUpperBound.apply(0)) {
            size[0] = number;
            configsChanged = true;
            return true;
        }
        return false;
    }

    /**
     * Controlla se l'altezza è regolare
     * ed entro i limiti del programma,
     * se sì, inserisce il dato e resituisce true,
     * altrimenti false.
     *
     * @param number    input height
     * @return          true se il valore è nel formato corretto, false altrimenti.
     */
    public boolean setSizeY(int number) {
        if (number >= SizeLowerBound.apply(1) && number <= SizeUpperBound.apply(1)){
            size[1] = number;
            configsChanged = true;
            return true;
        }
        return false;
    }

    /**
     *  Quando richiamata, restituisce i dati
     *  della larghezza e dell altezza
     *
     * @return  larghezza, altezza
     */
    public int[] getSize() { return size; }

    /**
     * Restituisce se i valori di configs sono cambiati dall'ultimo
     * backup
     * @return
     */
    boolean haveConfigsChanged() { return configsChanged; }

    /**
     * Setta il valore della velocità
     * @return
     */
    public boolean setVelocity(int number){
        if (number < 1){
            return false;
        }
        velocity = number;
        configsChanged = true;
        return true;
    }

    /**
     * Restituisce l'attributo della velocità
     * @return
     */
    public double getVelocity() {return velocity;}

    /**
     * Setta il numero dei frame per ogni giorno
     * @return
     */
    public boolean setFrameADay(int number){
        if (number < 1){
            return false;
        }
        frameADay = number;
        configsChanged = true;
        return true;
    }
    /**
     * Restituisce il numero di frame per ogni giorno
     * @return
     */
    public int getFrameADay() {return frameADay;}

     /**
     * Setta il valore della distanza sociale
     * @return
     */
    public boolean setSocialDistance(int number){
        if (number < 1){
            return false;
        }
        socialDistance = number;
        configsChanged = true;
        return true;
    }

     /**
     * Restituisce la distanza sociale
     * @return
     */
    public int getSocialDistance() {return socialDistance;}

    void setConfigsChanged(boolean changed) {this.configsChanged = changed;}
}
