package assets;
import javafx.util.Pair;
import sun.misc.Unsafe;
import sys.Config;
import sys.Core.*;
import sys.Rng;
import sys.State;

import java.util.Random;

public class Person {

    private State currentState;
    private int age;
    private int index;
    Pair<Double, Double> position;                  //x, y
    private Pair<Double, Double> speed;             //horizontalSpeed, verticalSpeed
    public boolean contact = false;
    public ColorStatus color = ColorStatus.GREEN;
    public MovementStatus movement = MovementStatus.MOVING;
    private int dayOfDeath = -1;
    private int dayToStop = -1;                     //Indica che la persona o è sempre ferma o è sempre in movimento
    private int daysFromInfection = -1;
    private double severityModifier = 1;
    private double infectivityModifier = 1;
    private boolean isInfected = false;
    private static Random ran = new Random();

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
        this.speed = getRandomSpeed();
        this.position = new Pair<>((double)ran.nextInt(currentState.configs.size.getKey()), (double)ran.nextInt(currentState.configs.size.getValue()));
    }

    /**
     * Se la persona è infetta, aggiorna il suo
     * contatore del giorno e aggiorna il suo status
     * di infezione in base al numero dei giorni
     * passati. Se si aggiorna lo stato di infezione
     * bisogna chiamare il metodo makeOfColor per far diventare
     * lo status della persona di un certo colore.
     * Se la persona diventa rossa, questa deve
     * essere impostata come ferma. Se la persona è ferma,
     * togliere opportuno denaro dalle casse dello Stato.
     * Se la persona è in terapia intensiva (rossa), togliere
     * il denaro.
     * Rendere indipendente il processo di stoppare le persone.
     */
    @ToRevise
    public void refresh() {
        if (movement == MovementStatus.STATIONARY){
            currentState.currentlyStationary+=1;
            if (dayToStop > -1) dayToStop--;
        }
        if (isInfected) {
            daysFromInfection = daysFromInfection + 1;
            if (daysFromInfection == currentState.configs.incubationToYellowDeadline) {
                makeOfColor(ColorStatus.YELLOW);
            } else if (daysFromInfection == currentState.configs.yellowToRedDeadline) {
                if (Rng.generateFortune(currentState.configs.sintomaticity, severityModifier)) {
                    int toStop;
                    makeOfColor(ColorStatus.RED);
                    if (Rng.generateFortune(currentState.configs.letality, severityModifier)) {
                        dayOfDeath = ran.nextInt(currentState.configs.diseaseDuration - (daysFromInfection + 1) - 1) + daysFromInfection + 1;
                        toStop = -1;
                    } else {
                        toStop = currentState.configs.diseaseDuration - daysFromInfection;
                    }
                    try {
                        setStationary(toStop);
                    } catch (UnsafeMovementStatusChangeException ignored) {}    //Se si entra nel catch, allora l'errore probabilmente è stato generato
                                                                                //a causa di una modifica proveniente dall'esterno non strutturata correttamente.
                }
            } else if (daysFromInfection == dayOfDeath) {
                makeOfColor(ColorStatus.BLACK);
            } else if (daysFromInfection == currentState.configs.diseaseDuration) {
                makeOfColor(ColorStatus.BLUE);
            }
        }
        if (dayToStop == 0) {
            try {
                setMoving();
            } catch (UnsafeMovementStatusChangeException ignored) {}    //Se si entra nel catch, allora l'errore probabilmente è stato generato
                                                                        //a causa di una modifica proveniente dall'esterno non strutturata correttamente.
        }
    }

    /**
     * Imposta l'attributo movement di Person a MovementStatus.STATIONARY per una certa
     * durata "duration" espressa in giorni. Se la persona è già ferma, allora viene lanciata
     * una eccezione, poiché il metodo non è stato usato correttamente.
     * @param duration Indica il tempo (in giorni) per il quale la persona deve essere ferma.
     *                 Se vale -1, allora la persona sarà ferma fino a modifica manuale
     *                 del parametro.
     *
     */
    @Ready
    public void setStationary(int duration) throws UnsafeMovementStatusChangeException {
        if (duration < -1) duration = -1;
        if (movement == MovementStatus.STATIONARY) throw new UnsafeMovementStatusChangeException();
        else {
            dayToStop = duration;
            movement = MovementStatus.STATIONARY;
        }
    }

    /**
     * Imposta l'attributo movement di Person a MovementStatus.MOVING.
     * Se la persona è ferma e il suo movimento riprende prima di quanto stabilito
     * nell'attributo "dayToStop", viene lanciata una eccezione, poiché il metodo
     * non è stato usato correttamente.
     */
    @Ready
    public void setMoving() throws UnsafeMovementStatusChangeException {
        if (dayToStop > 0) throw new UnsafeMovementStatusChangeException();
        else {
            dayToStop = -1;
            movement = MovementStatus.MOVING;
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
                if (index != currentState.incubationYellow) switchPerson(currentState.incubationYellow);
                currentState.incubationYellow-=1;
                break;
            case RED:
                this.color = ColorStatus.RED;
                if (index != currentState.yellowRed) switchPerson(currentState.yellowRed);
                currentState.yellowRed-=1;
                break;
            case BLUE:
                if (this.color==ColorStatus.YELLOW){
                    if (index != currentState.yellowRed) switchPerson(currentState.yellowRed);
                    currentState.yellowRed-=1;
                    switchPerson(currentState.redBlue);         //Qui no perché è molto più raro che accada;
                } else {
                    if (index != currentState.redBlue) switchPerson(currentState.redBlue);
                }
                currentState.redBlue-=1;
                isInfected = false;
                this.color = ColorStatus.BLUE;
                break;
            case BLACK:
                this.color = ColorStatus.BLACK;
                if (index != currentState.redBlue) switchPerson(currentState.redBlue);
                switchPerson(currentState.blueBlack);           //Anche qui è più raro che accada
                currentState.blueBlack-=1;
                currentState.redBlue-=1;
                isInfected = false;
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
        if (index!=currentState.greenIncubation) {      //E' inutile effetturare lo scambio se l'indice è già quello giusto.
            switchPerson(currentState.greenIncubation);
        }
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
     * @param severityModifier     modificatore di morte generato da Rng
     */
    @ToRevise
    public void setSeverityModifier(double severityModifier){
        this.severityModifier = severityModifier;
    }

    /**
     * Restituisce l'attributo deathModifier
     * @return  restituisce l'attributo deathModifier
     */
    @Ready
    public double getSeverityModifier() {
        return severityModifier;
    }

    /**
     * Restituisce l'attributo infectivityModifier
     * @return  restituisce l'attributo infectivityModifier
     */
    @Ready
    public double getInfectivityModifier() {
        return infectivityModifier;
    }



    public Pair<Integer, Integer> getPosition() {
        return new Pair<>(position.getKey().intValue(),position.getValue().intValue());
    }

    public Pair<Integer,Integer> nextPosition() {
        if (movement == MovementStatus.MOVING) {
            if (contact) {
                Pair<Double, Double> speedA = getRandomSpeed();
                speed = new Pair<>(Math.copySign(speedA.getKey(), -speed.getKey()), Math.copySign(speedA.getValue(), -speed.getKey()));
                contact = false;
            }
            double newSpeedX = speed.getKey();
            double newSpeedY = speed.getValue();

            boolean changed = false;
            if ((position.getKey() + newSpeedX <= 0) || (position.getKey() + newSpeedX > currentState.configs.size.getKey())) {
                changed = true;
                newSpeedX = -newSpeedX;
            }
            if ((position.getValue() + newSpeedY <= 0) || (position.getValue() + newSpeedY > currentState.configs.size.getValue())) {
                changed = true;
                newSpeedY = -newSpeedY;
            }
            if (changed) speed = new Pair<>(newSpeedX, newSpeedY);
            position = new Pair<>(position.getKey() + newSpeedX, position.getValue() + newSpeedY);
        }
        return getPosition();
    }

    private Pair<Double,Double> getRandomSpeed() {
        double x = (ran.nextDouble()-0.5)*2;
        double y = Math.sqrt(1-Math.abs(x));
        if (ran.nextInt(2) == 0) {
            return new Pair<>(x*currentState.configs.velocity, y*currentState.configs.velocity);
        }
        return new Pair<>(x*currentState.configs.velocity, -y*currentState.configs.velocity);
    }

    static class UnsafeMovementStatusChangeException extends Exception{
        UnsafeMovementStatusChangeException(){
            super("Unsafe movement status change from external class.");
        }
    }

    // ------------------------- DEBUGGING ---------------------------

    @Debug
    public void debugForceColor(ColorStatus color){
        try {
            switch (color) {
                case YELLOW:
                    setAsInfected();
                    makeOfColor(ColorStatus.YELLOW);
                    break;
                case RED:
                    debugForceColor(ColorStatus.YELLOW);
                    makeOfColor(ColorStatus.RED);
                    setStationary(currentState.configs.diseaseDuration - daysFromInfection);
                    break;
                case BLUE:
                    debugForceColor(ColorStatus.RED);
                    makeOfColor(ColorStatus.BLUE);
                case BLACK:
                    debugForceColor(ColorStatus.RED);
                    makeOfColor(ColorStatus.BLACK);
                    setStationary(-1);
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + color);
            }
        }catch (UnsafeMovementStatusChangeException ignored) {}
    }


}

