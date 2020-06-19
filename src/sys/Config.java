package sys;

import javafx.util.Pair;

public class Config {

    // ----------------------------- SIMULATION PARAMETERS (COMPULSORY) -----------------------------

    public int ageAverage;
    public int populationNumber;
    public int initialResources;
    public int swabsCost;
    public double infectivity;
    public double sintomaticity;
    public double letality;
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
}
