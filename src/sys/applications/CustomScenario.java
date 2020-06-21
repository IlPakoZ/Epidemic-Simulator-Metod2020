package sys.applications;

import sys.Core.*;
import sys.Simulation;
import sys.models.Scenario;

import java.util.HashSet;

public class CustomScenario extends Scenario {

    private static final int ID = 0;

    private Scenario[] scenarioSlots = new Scenario[100];
    private HashSet<Integer> scenariosInserted = new HashSet<>();

    CustomScenario(Simulation currentSimulation) {
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
        if (!containsScenario(newScenario.getID())) {
            scenarioSlots[newScenario.getID()] = newScenario;
            scenariosInserted.add(newScenario.getID());
        } else {
            if (newScenario.getClass() != scenarioSlots[newScenario.getID()].getClass()){
                try {
                    throw new DuplicatedIDScenarioException();
                } catch (DuplicatedIDScenarioException e) { e.printStackTrace(); System.exit(-1);}
            }
        }
    }

    /**
     * Aggiunge uno scenario tra gli scenari
     * abilitati.
     *
     * @param scenario      scenario da rimuovere.
     */
    @Ready
    public void removeScenario(Scenario scenario){
        if (scenarioSlots[scenario.getID()] != null){
            scenarioSlots[scenario.getID()] = null;
            scenariosInserted.remove(scenario.getID());
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
     * Restituisce l'ID univoco che identifica uno scenario.
     * @return  ID dello scenario.
     */
    @Ready
    @Override
    public int getID() {
        return ID;
    }

    /**
     * Restituisce una breve descrizione dello scenario.
     * @return  breve descrizione dello scenario.
     */
    @NotImplemented
    @Override
    public String getInfos() {
        return null;
    }

    /**
     * Restituisce il nome dello scenario.
     * @return  nome dello scenario.
     */
    @Override
    public String getName() {
        return "Scenario personalizzato.\n";
    }

    static class DuplicatedIDScenarioException extends Exception{
        DuplicatedIDScenarioException(){
            super("Conflict in scenario IDs: two scenarios have the same ID value, and that's bad.");
        }
    }
}
