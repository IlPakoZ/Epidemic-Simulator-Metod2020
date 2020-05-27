package sys;

public class Config {

    // ----------------------------- SIMULATION PARAMETERS (COMPULSORY) -----------------------------

    int ageAverage;
    public int populationNumber;
    int initialResources;
    int swabsCost;
    double infectivity;
    double sintomaticity;
    double letality;
    int diseaseDuration;
    int maxAge;
    int meanAge;

    // ----------------------------------------------------------------------------------------------

    // ----------------------------- SIMULATION PARAMETERS (OPTIONAL) -------------------------------

    int dayDuration = 500;                         //Loop duration measured in milliseconds
    double velocity = 1;
    int socialDistance;
    boolean masks = false;

    // ----------------------------------------------------------------------------------------------

    boolean isValid;

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
            this.meanAge = c.meanAge;
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
