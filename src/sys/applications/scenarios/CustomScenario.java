package sys.applications.scenarios;

import sys.Core.*;
import sys.Simulation;
import sys.models.Scenario;

import java.util.HashSet;

public class CustomScenario extends Scenario {

    private Scenario[] scenarioSlots = new Scenario[100];
    private HashSet<Integer> scenariosInserted = new HashSet<>();
    private static final ScenarioInfos SCENARIO_INFOS = new ScenarioInfos(0);

    static {
        SCENARIO_INFOS.setInfos("E' uno scenario personalizzato che contiene al suo interno altri scenari."+
                "Non viene utilizzata nessuna trategia particolare per\nil contenimento della malattia.");
        SCENARIO_INFOS.setInfos("Scenario Personalizzato");
    }

    public CustomScenario(Simulation currentSimulation) {
        super(currentSimulation);
    }

    /**
     * Restituisce true se contiene lo scenario ricercato,
     * false se non lo contiene.
     *
     * @param scenarioID    ID dello scenario di cui verificare la presenza
     * @return              true se lo scenario è presente, false altrimenti.
     */
    @Ready
    public boolean containsScenario(int scenarioID) {
        return scenariosInserted.contains(scenarioID);
    }

    /**
     * Aggiunge uno scenario tra gli scenari abilitati
     * o lancia una eccezione di tipo "DuplicatedIDScenarioException"
     * se c'è un conflitto di ID, ossia se è presente uno scenario
     * con lo stesso ID ma di un'altra classe.
     *
     * @param newScenario   scenario da aggiungere
     */
    @Ready
    public void addScenario(Scenario newScenario){
        if (!containsScenario(newScenario.getInfos().getID())) {
            scenarioSlots[newScenario.getInfos().getID()] = newScenario;
            scenariosInserted.add(newScenario.getInfos().getID());
        } else {
            if (newScenario.getClass() != scenarioSlots[newScenario.getInfos().getID()].getClass()){
                try {
                    throw new DuplicatedIDScenarioException();
                } catch (DuplicatedIDScenarioException e) { e.printStackTrace(); System.exit(-1);}
            }
        }
     }

     /**
     * Rimuove uno scenario tra gli scenari
     * abilitati.
     * @param scenario      scenario da rimuovere.
     */
    @Ready
    public void removeScenario(Scenario scenario){
        if (scenarioSlots[scenario.getInfos().getID()] != null){
            scenarioSlots[scenario.getInfos().getID()] = null;
            scenariosInserted.remove(scenario.getInfos().getID());
        }
    }

    /**
     * Esegue le oneTimeAction di tutti gli scenari abilitati.
     */
    @Ready
    @Override
    public void oneTimeAction() {
        for (Integer i: scenariosInserted){
            scenarioSlots[i].oneTimeAction();
        }
    }


    /**
     * Esegue le dailyAction di tutti gli scenari abilitati.
     */
    @Ready
    @Override
    public void dailyAction() {
        for (Integer i: scenariosInserted){
            scenarioSlots[i].dailyAction();
        }
    }

    /**
     * Esegue le frameAction di tutti gli scenari abilitati.
     */
    @Ready
    @Override
    public void frameAction() {
        for (Integer i: scenariosInserted){
            scenarioSlots[i].frameAction();
        }
    }

    /**
     * Restituisce una breve descrizione dello scenario e dei parametri di cui necessita per essere eseguito.
     * @return  oggetto ScenariosInfos contenente le informazioni dello scenario.
     */
    @Override
    public ScenarioInfos getInfos() {
        return SCENARIO_INFOS;
    }

    static class DuplicatedIDScenarioException extends Exception{
        DuplicatedIDScenarioException(){
            super("Conflict in scenario IDs: two scenarios have the same ID value, and that's bad.");
        }
    }
}
