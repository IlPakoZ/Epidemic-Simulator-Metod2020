package sys;

import javafx.util.Pair;

public class Config {

    // ----------------------------- SIMULATION PARAMETERS (COMPULSORY) -----------------------------

    public int ageAverage;
    public int populationNumber;
    public int initialResources;
    public int swabsCost;
    public int infectivity;
    public int sintomaticity;
    public int letality;
    public int diseaseDuration;
    public int maxAge;
    public Pair<Integer,Integer> size;              // width, height

    // ----------------------------------------------------------------------------------------------

    // ----------------------------- SIMULATION PARAMETERS (OPTIONAL) -------------------------------

    public int dayDuration = 500;                         //Loop duration measured in milliseconds
    public double velocity = 2;
    public int socialDistance;
    boolean masks = false;
    public int frameADay = 15;

    // ----------------------------------------------------------------------------------------------

    public static final double SWAB_SUCCESS_RATE = 99;
    public static final int DAILY_COST_IF_STATIONARY = 1;
    public static final double INCUBATION_TO_YELLOW_DEADLINE = 1/6.0;
    public static final double YELLOW_TO_RED_DEADLINE = 1/3.0;

    public boolean isValid;
    public int incubationToYellowDeadline;  //Deve essere impostato a currentState.configs.diseaseDuration*Config.INCUBATION_TO_YELLOW_DEADLINE (facendo casting ad intero)
    public int yellowToRedDeadline;         //Deve essere impostato a currentState.configs.diseaseDuration*Config.YELLOW_TO_RED_DEADLINE (facendo casting ad intero)

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
     * @return
     */
    public boolean setPopulationNumber(int number){
        if (number > 100000 || number <= 1){
            return false;
        }
        populationNumber = number;
        return true;
    }

    /**
     * Quando richiamata, restituisce il parametro
     * del numero della popolazione.
     *
     * @return
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
     * @return
     */
    public boolean setDiseaseDuration(int number){
        if (number > 90 || number <=0){
            return false;
        }
        diseaseDuration = number;
        return true;
    }

    /**
     *  Quando richiamata, restituisce il paramentro
     *  della durata della malattia.
     * @return
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
     * @param number  input initialResources
     * @return
     */
    public boolean setInitialResources(int number){
        if (number >= (populationNumber * diseaseDuration) || number <= 0){
            return false;
        }
        initialResources = number;
        return true;
    }

    /**
     *  Quando richiamata, restituisce il paramentro
     *  delle risorse iniziali.
     *
     * @return
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
     * @param number  input swabsCost
     * @return
     */
    public boolean setSwabsCost(int number){
        if (number <= (initialResources/(populationNumber*10))){
            return false;
        }
        swabsCost = number;
        return true;
    }

    /**
     *  Quando richiamata, restituisce il paramentro
     *  del costo del tampone.
     *
     * @return
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
     * @param number  input infectivity
     * @return
     */
    public boolean setInfectivity(int number){
        if (number <= 0.0 || number > 100.0){
            return false;
        }
        infectivity = number;
        return true;
    }

    /**
     *  Quando richiamata, restituisce il paramentro
     *  dell'infettività.
     *
     * @return
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
     * @param number  input sintomaticity
     * @return
     */
    public boolean setSintomaticity(int number){
        if (number <= 0.0 || number > 100.0){
            return false;
        }
        sintomaticity = number;
        return true;
    }

    /**
     *  Quando richiamata, restituisce il paramentro
     *  della sintomacity.
     *
     * @return
     */
    public int getSintomaticity(){
        return sintomaticity;
    }

}
