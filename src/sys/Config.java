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
    private int frameADay = 25;
    private int maxAge = 110;
    private int ageAverage = 50;

    // ------------------------------------- CONSTANTS & BOUNDS --------------------------------------

    //DO NOT EDIT THOSE IF YOU DON'T KNOW WHAT YOU'RE DOING

    public static final String OUTPUT_TOTAL_FILE = "total.png";
    public static final String OUTPUT_DAILY_FILE = "daily.png";
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
    public static final int DAY_DURATION_LOWER_BOUND = 50;
    public static final int DAY_DURATION_UPPER_BOUND = 1000;
    public static final int FRAME_A_DAY_LOWER_BOUND = 1;
    public static final int FRAME_A_DAY_UPPER_BOUND = 120;
    public static final int VELOCITY_LOWER_BOUND = 1;
    public IntSupplier VELOCITY_UPPER_BOUND = (()-> Math.min(Math.min(size[0],size[1])/10,VELOCITY_LOWER_BOUND));
    public Function<Integer, Integer> SizeLowerBound = ((i)-> Math.max(2, (size[1-i] != 0 ? (populationNumber / 4) / size[1-i] : 2)));
    public Function<Integer, Integer> SizeUpperBound = ((i)-> Math.min((populationNumber * 4) / (size[1-i]==0?16:size[1-i]), 2000));
    public Function<Integer, Integer> PreferredSizeBound = ((i)-> ((int)(size[1-i]!=0 ? populationNumber*4/size[1-i]: Math.sqrt(populationNumber) * 2)));

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
        if (number > POPULATION_NUMBER_UPPER_BOUND || number < POPULATION_NUMBER_LOWER_BOUND){
            return false;
        }
        configsChanged = true;
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
        if (number > DISEASE_DURATION_UPPER_BOUND || number <= DISEASE_DURATION_LOWER_BOUND){
            return false;
        }
        configsChanged = true;
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
        if (number >= (populationNumber * diseaseDuration) || number <= RESOURCES_LOWER_BOUND){
            return false;
        }
        configsChanged = true;
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
        if (number <= (initialResources/(populationNumber*10))){
            return false;
        }
        configsChanged = true;
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
        if (number <= 0.0 || number > 100.0){
            return false;
        }
        configsChanged = true;
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
        if (number <= 0.0 || number > 100.0){
            return false;
        }
        configsChanged = true;
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
        if (number <= MAX_AGE_LOWER_BOUND || number >= MAX_AGE_UPPER_BOUND){
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
        if (number < AGE_AVERAGE_LOWER_BOUND || number > AGE_AVERAGE_UPPER_BOUND){
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
        if (number < VELOCITY_LOWER_BOUND || number > VELOCITY_UPPER_BOUND.getAsInt()){
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
     * Controlla se i frame giornalieri sono regolari
     * ed entro i limiti del programma, se si,
     * inserisce il dato e restituisce true, altrimenti
     * restituisce false.
     *
     * @param number    input frameADay
     * @return          true se il valore è nel formato corretto, false altrimenti.
     */
    public boolean setFrameADay(int number){
        if (number < FRAME_A_DAY_LOWER_BOUND || number > FRAME_A_DAY_UPPER_BOUND){
            return false;
        }
        frameADay = number;
        configsChanged = true;
        return true;
    }

    /**
     * Setta la durata del giorno
     * @param number    durata
     * @return          true se il valore è valido, false altrimenti
     */
    public boolean setDayDuration(int number){
        if (number < DAY_DURATION_LOWER_BOUND || number > DAY_DURATION_UPPER_BOUND){
            return false;
        }
        dayDuration = number;
        configsChanged = true;
        return true;
    }

    /**
     * Restituisce il numero di frame per ogni giorno
     * @return
     */
    public int getFrameADay() {return frameADay;}

    /**
     * Quando richiamata, restituisce l'attributo
     * della durata del giorno.
     *
     * @return  dayDuration.
     */
    public int getDayDuration() { return dayDuration; }

    /**
     * Questo metodo forza il numero della popolazione ad essere pari
     * al parametro. Questo metodo non è sicuro (non effettua
     * alcun controllo sulla correttezza dei dati) e non dovrebbe
     * essere usato per prendere in input dati dall'esterno.
     * @param populationNumber  input populationNumber
     */
    @Ready
    public void forcePopulationNumber(int populationNumber){
        this.populationNumber = populationNumber;
    }

    /**
     * Questo metodo forza il numero delle risorse ad essere pari
     * al parametro. Questo metodo non è sicuro (non effettua
     * alcun controllo sulla correttezza dei dati) e non dovrebbe
     * essere usato per prendere in input dati dall'esterno.
     * @param resources  input resources
     */
    @Ready
    public void forceInitialResources(int resources){
        this.initialResources = resources;
    }

    /**
     * Questo metodo forza la size ad essere pari alle due
     * variabili di input. Questo metodo non è sicuro (non effettua
     * alcun controllo sulla correttezza dei dati) e non dovrebbe
     * essere usato per prendere in input dati dall'esterno.
     * @param x  input x coordinate
     * @param y  input y coordinate
     */
    @Ready
    public void forceSize(int x, int y){
        size = new int[]{x,y};
    }

    /**
     * Questo metodo forza il costo del tampone ad essere pari
     * al parametro. Questo metodo non è sicuro (non effettua
     * alcun controllo sulla correttezza dei dati) e non dovrebbe
     * essere usato per prendere in input dati dall'esterno.
     * @param swabsCost  input swabsCost
     */
    @Ready
    public void forceSwabsCost(int swabsCost){
        this.swabsCost = swabsCost;
    }

    /**
     * Questo metodo forza l'attributo segnalato da index ad essere
     * pari al primo parametro. Questo metodo non è sicuro (non effettua
     * alcun controllo sulla correttezza dei dati) e non dovrebbe
     * essere usato per prendere in input dati dall'esterno.
     * @param value  input value
     * @param index  input index
     */
    @Ready
    public void forceHealthParameters(int value, int index){
        switch (index) {
            case 0:
                infectivity = value;
                break;
            case 1:
                sintomaticity = value;
                break;
            case 2:
                letality = value;
                break;
        }
    }

    /**
     * Questo metodo forza l'attributo segnalato da index ad essere
     * pari al primo parametro. Questo metodo non è sicuro (non effettua
     * alcun controllo sulla correttezza dei dati) e non dovrebbe
     * essere usato per prendere in input dati dall'esterno.
     * @param value  input value
     * @param index  input index
     */
    @Ready
    public void forceAge(int value, int index){
        if (index == 0) {
            maxAge = value;
        } else if (index == 1){
            ageAverage = value;
        }
    }

    void setConfigsChanged(boolean changed) {this.configsChanged = changed;}
}
