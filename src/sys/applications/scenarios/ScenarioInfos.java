package sys.applications.scenarios;

public class ScenarioInfos {

    private int ID;
    private String infos;
    private String name;

    public ScenarioInfos(int ID){ this.ID = ID; }

    /**
     * Restituisce l'ID univoco che identifica uno scenario.
     * @return  ID dello scenario.
     */
    public int getID() {
        return ID;
    }

    /**
     * Restituisce una breve descrizione dello scenario e dei parametri di cui necessita per essere eseguito.
     * @return  breve descrizione dello scenario.
     */
    public String getInfos() { return infos; }

    /**
     * Restituisce il nome dello scenario.
     * @return  nome dello scenario.
     */
    public String getName(){ return name; }

    /**
     * Imposta le informazioni dello scenario.
     * @param infos informazioni dello scenario.
     */
    public void setInfos(String infos) { this.infos = infos; }

    /**
     * Imposta il nome dello scenario.
     * @param name  nome dello scenario.
     */
    public void setName(String name) { this.name = name; }

}
