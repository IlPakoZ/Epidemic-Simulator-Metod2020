package assets;
import sys.Core;
import sys.Core.*;
import sys.State;

public class Person {

    private State currentState;
    private int age;
    private int index;
    public ColorStatus color = ColorStatus.GREEN;
    private MovementStatus movement = MovementStatus.MOVING;
    private int dayOfDeath = -1;
    private int daysFromInfection = -1;
    private double deathModifier = 1;
    private double infectivityModifier = 1;
    private boolean isInfected = false;

    /**
     * Costruttore di Person. Crea una nuova persona
     * con un attributo età e un attributo indice
     * che memorizza la sua posizione nell'ArrayList
     * startingPopulation.
     *
     * @param age       età della persona
     * @param index     la sua posizione nell'ArrayList startingPopulation
     */
    public Person(int age, int index, State currentState){
        this.age = age;
        this.index = index;
        this.currentState = currentState;
    }

    /**
     * Se la persona è infetta, aggiorna il suo
     * contatore del giorno e aggiorna il suo status
     * di infezione in base al numero dei giorni
     * passati. Se si aggiorna lo stato di infezione
     * bisogna chiamare il metodo makeOfColor per far diventare
     * lo status della persona di un certo colore.
     * Se la persona è ferma,
     * togliere opportuno denaro dalle casse dello Stato.
     * Se la persona è in terapia intensiva (rossa), togliere
     * il denaro.
     *
     */
    @NotImplemented
    public void refresh(){ if (isInfected);
        boolean becomeRed = False;
        boolean becomeBlack = False;
        daysFromInfection = daysFromInfection + 1;
        if (daysFromInfection < currentState.configs.diseaseDuration/6){
            break;
        }
        else if (daysFromInfection == (currentState.configs.diseaseDuration/6)){
            makeOfColor(YELLOW);
            becomeRed = rng.generateFortune(currentState.configs.sintomaticity , deathModifier);
        }
        else if (daysFromInfection < (currentState.configs.diseaseDuration/3)){
            break;
        }
        else if (daysFromInfection == (currentState.configs.diseaseDuration/3)) {
            if (becomeRed){
                makeOfColor(RED);
                becomeBlack = rng.generateFortune(currentState.configs.letality , deathModifier);
            else
                break;
            }
        }
        else if (daysFromInfection < currentState.configs.diseaseDuration){
            break;
        }
        else {
            if (becomeBlack) {
                makeOfColor(BLACK);
            }
            else if (color == GREEN){
                break;
            }
            else
                makeOfColor(BLUE);
        }
    }

    /**
     * Metodo di supporto interno utilizzato per settare
     * la persona di un determinato colore.
     *
     * @param color     colore di destinazione
     */

    @ToRevise
    private void makeOfColor(ColorStatus color){
        switch (color){
            case YELLOW:
                this.color = ColorStatus.YELLOW;
                switchPerson(currentState.incubationYellow);
                currentState.incubationYellow-=1;
                break;
            case RED:
                this.color = ColorStatus.RED;
                switchPerson(currentState.yellowRed);
                currentState.yellowRed-=1;
                movement = MovementStatus.STATIONARY;
                break;
            case BLUE:
                if (this.color==ColorStatus.YELLOW){
                    switchPerson(currentState.redBlue);
                    currentState.yellowRed-=1;
                    currentState.redBlue-=1;
                    movement = MovementStatus.MOVING;

                } else {
                    switchPerson(currentState.redBlue);
                    currentState.redBlue-=1;
                }
                this.color = ColorStatus.BLUE;

                break;
            case BLACK:
                this.color = ColorStatus.BLACK;
                switchPerson(currentState.blueBlack);
                currentState.blueBlack-=1;
                currentState.redBlue-=1;
                movement = MovementStatus.STATIONARY;
                break;
            default:
                break;
        }
    }

    /**
     * Metodo utilizzato per settare una persona come infetta.
     * Questo metodo deve essere chiamato solo se il contatto
     * ha avuto esito positivo.
     * Si occupa anche di mettere una persona nello status di
     * incubazione.
     */
    @ToRevise
    public void setAsInfected() {
        isInfected = true;
        switchPerson(currentState.greenIncubation);
        currentState.greenIncubation-=1;
    }

    /**
     * Restituisce l'attributo isInfected.
     *
     * @return  isInfected
     */
    @ToRevise
    public boolean isInfected(){
        return isInfected;
    }

    /**
     * Metodo utilizzato per scambiare la posizione nell'array della
     * persona corrente con la persona ad indice "newIndex"
     *
     * @param newIndex  indice di destinazione
     */
    @NotImplemented
    private void switchPerson(int newIndex){
        Person temp = currentState.startingPopulation[newIndex];
        temp.index = index;
        currentState.startingPopulation[newIndex] = this;
        currentState.startingPopulation[index] = temp;
        index = newIndex;
    }

    /**
     * Imposta il modificatore di infettività in base
     * al parametro in input. Questo metodo è utilizzato
     * dalla classe Rng.
     * @param infectivityModifier   modificatore di infettivitò generato da Rng
     */
    @ToRevise
    public void setInfectivityModifier(double infectivityModifier){
        this.infectivityModifier = infectivityModifier;
    }

    /**
     * Imposta il modificatore di infettività in base
     * al parametro in input. Questo metodo è utilizzato dalla
     * classe Rng durante la creazione della popolazione.
     * @param deathModifier     modificatore di morte generato da Rng
     */
    @ToRevise
    public void setDeathModifier(double deathModifier){
        this.deathModifier = deathModifier;
    }

    /**
     * Restituisce l'attributo deathModifier
     * @return  restituisce l'attributo deathModifier
     */
    @Ready
    public double getDeathModifier() {
        return deathModifier;
    }

    /**
     * Restituisce l'attributo infectivityModifier
     * @return  restituisce l'attributo infectivityModifier
     */
    @Ready
    public double getInfectivityModifier() {
        return infectivityModifier;
    }

    /**
     * Sottrae value dalle risorse. Se il nuovo valore calcolato
     * è minore di zero, allora impostalo a zero.
     * @param value     risorse da togliere
     * @return          restituisce True se ci sono ancora risorse disponibili, False altrimenti
     */
    @Ready
    private boolean subtractResources(int value){
        if (currentState.resources - value > 0) {
            currentState.resources -= value;
            return true;
        }
        currentState.resources = 0;
        return false;
    }
}

