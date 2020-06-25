package assets;
import javafx.util.Pair;
import sys.Core.*;
import sys.Rng;
import sys.State;

public class Person {

    private State currentState;
    private int age;
    private int index;
    private double x, y;                      //x, y
    private double speedX, speedY;            //horizontalSpeed, verticalSpeed
    public boolean contact = false;
    public ColorStatus color = ColorStatus.GREEN;
    private MovementStatus movement = MovementStatus.MOVING;
    private int dayOfDeath = -1;
    private int dayToStop = -1;                     //Indica che la persona o è sempre ferma o è sempre in movimento
    private int daysFromInfection = -1;
    private double severityModifier = 1;
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
        Pair<Double, Double> speed = getRandomSpeed();
        speedX = speed.getKey();
        speedY = speed.getValue();
        this.x = Rng.R.nextInt(currentState.configs.getSize()[0]);
        this.y = Rng.R.nextInt(currentState.configs.getSize()[1]);
    }

    /**
     * Se la persona è infetta, aggiorna il suo
     * contatore del giorno e aggiorna il suo status
     * di infezione in base al numero dei giorni
     * passati. Se si aggiorna lo stato di infezione
     * bisogna chiamare il metodo makeOfColor per far diventare
     * lo status della persona di un certo colore.
     * Se la persona diventa rossa, questa deve
     * essere impostata come ferma.
     */
    @Ready
    public void refresh() {
        if (movement == MovementStatus.STATIONARY){
            currentState.currentlyStationary+=1;
            if (dayToStop > -1) dayToStop--;
        }
        if (isInfected) {
            daysFromInfection = daysFromInfection + 1;
            if (daysFromInfection == currentState.configs.incubationToYellowDeadline) {
                makeOfColor(ColorStatus.YELLOW);
                if (currentState.isBigBrother()) setStationary(currentState.configs.yellowToRedDeadline-daysFromInfection);
            } else if (daysFromInfection == currentState.configs.yellowToRedDeadline) {
                if (Rng.generateFortune(currentState.configs.getSintomaticity(), currentState.isPoorCountry() ? severityModifier*5 :severityModifier)) {
                    int toStop;
                    makeOfColor(ColorStatus.RED);
                    if (Rng.generateFortune(currentState.configs.getLetality(), currentState.isPoorCountry() ? severityModifier*5 :severityModifier)) {
                        dayOfDeath = Rng.R.nextInt(currentState.configs.getDiseaseDuration() - (daysFromInfection + 1) - 1) + daysFromInfection + 1;
                        toStop = -1;
                    } else {
                        toStop = currentState.configs.getDiseaseDuration() - daysFromInfection;
                    }
                    setStationary(toStop);
                }
            } else if (daysFromInfection == dayOfDeath) {
                makeOfColor(ColorStatus.BLACK);
            } else if (daysFromInfection == currentState.configs.getDiseaseDuration()) {
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
     * durata "duration" espressa in giorni. Se la persona è già ferma, la persona viene fermata
     * per la durata massima tra i due parametri.
     * @param duration Indica il tempo (in giorni) per il quale la persona deve essere ferma.
     *                 Se vale -1, allora la persona sarà ferma fino a modifica manuale
     *                 del parametro.
     *
     */
    @Ready
    public void setStationary(int duration) {
        if (duration < -1) duration = -1;
        if (movement == MovementStatus.STATIONARY) {
            dayToStop = duration == -1 ? -1 : Math.max(duration, dayToStop);
        }
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
     * Restituisce l'attributo movement.
     *
     * @return movement
     */
    @Ready
    public MovementStatus getMovement() {
        return movement;
    }

    /**
     * Metodo di supporto interno utilizzato per settare
     * la persona di un determinato colore.
     *
     * @param color     colore di destinazione
     */
    @Ready
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
                currentState.swabPersons.enqueue(this);
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
    @Ready
    public void setAsInfected() {
        isInfected = true;
        if (index!=currentState.greenIncubation) {      //E' inutile effetturare lo scambio se l'indice è già quello giusto.
            switchPerson(currentState.greenIncubation);
        }
        currentState.greenIncubation-=1;
    }

    /**
     * Metodo utilizzato per scambiare la posizione nell'array della
     * persona corrente con la persona ad indice "newIndex"
     *
     * @param newIndex  indice di destinazione
     */
    @Ready
    private void switchPerson(int newIndex){
        Person temp = currentState.startingPopulation[newIndex];
        temp.index = index;
        currentState.startingPopulation[newIndex] = this;
        currentState.startingPopulation[index] = temp;
        index = newIndex;
    }

    /**
     * Restituisce i giorni che sono passati dall'infezione iniziale.
     * @return  giorni dall'infezione.
     */
    @Ready
    public int getDaysFromInfection(){ return daysFromInfection; }

    /**
     * Restituisce l'attributo isInfected.
     *
     * @return  isInfected
     */
    @Ready
    public boolean isInfected(){
        return isInfected;
    }

    /**
     * Imposta il modificatore di infettività in base
     * al parametro in input. Questo metodo è utilizzato dalla
     * classe Rng durante la creazione della popolazione.
     *
     * @param severityModifier     modificatore di morte generato da Rng
     */
    @Ready
    public void setSeverityModifier(double severityModifier){
        this.severityModifier = severityModifier;
    }

    /**
     * Restituisce l'attributo deathModifier.
     *
     * @return  restituisce l'attributo deathModifier
     */
    @Ready
    public double getSeverityModifier() {
        return severityModifier;
    }

    /**
     * Restituisce l'attributo age.
     *
     * @return restituisce l'attributo age
     */
    @Ready
    public int getAge(){
        return age;
    }

    /**
     * Restituisce la posizione della persona sulla quale viene invocato il metodo.
     *
     * @return  la posizione della persona
     */
    @Ready
    public int[] getPosition() {
        return new int[]{(int)x, (int)y};
    }

    /**
     * Calcola la prossima posizione che la persona deve assumere e,
     * se c'è un contatto, sia questo con un muro dello spazio
     * o con un'altra persona, gestisce le loro velocità e posizioni future.
     *
     * @return  la prossima posizione della persona
     */
    @Ready
    public int[] nextPosition() {
        if (movement == MovementStatus.MOVING) {
            if (contact) {
                Pair<Double, Double> speedA = getRandomSpeed();
                speedX = Math.copySign(speedA.getKey(), -speedX);
                speedY = Math.copySign(speedA.getValue(), -speedY);
                contact = false;
            }
            double newSpeedX = speedX;
            double newSpeedY = speedY;

            boolean changed = false;
            if ((x + newSpeedX <= 0) || (x + newSpeedX >= currentState.configs.getSize()[0])) {
                changed = true;
                newSpeedX = -newSpeedX;
            }
            if ((y + newSpeedY <= 0) || (y + newSpeedY >= currentState.configs.getSize()[1])) {
                changed = true;
                newSpeedY = -newSpeedY;
            }
            if (changed) {speedX = newSpeedX; speedY = newSpeedY;}

            x += newSpeedX;
            y += newSpeedY;
        }
        return getPosition();
    }

    /**
     * Restituisce due velocità (una orizzontale e una verticale) generate randomicamente.
     *
     * @return  le velocità di movimento
     */
    @Ready
    private Pair<Double,Double> getRandomSpeed() {
        double x = (Rng.R.nextDouble()-0.5)*2;
        double y = Math.sqrt(1-Math.abs(x));
        if (Rng.R.nextInt(2) == 0) {
            return new Pair<>(x*currentState.configs.getVelocity(), y*currentState.configs.getVelocity());
        }
        return new Pair<>(x*currentState.configs.getVelocity(), -y*currentState.configs.getVelocity());
    }

    public static class UnsafeMovementStatusChangeException extends Exception{
        UnsafeMovementStatusChangeException(){
            super("Unsafe movement status change from external class.");
        }
    }

    // ------------------------- DEBUGGING ---------------------------

    @Debug
    public void debugForceColor(ColorStatus color){
        switch (color) {
            case YELLOW:
                setAsInfected();
                makeOfColor(ColorStatus.YELLOW);
                daysFromInfection = currentState.configs.getDiseaseDuration()/6;
                break;
            case RED:
                debugForceColor(ColorStatus.YELLOW);
                makeOfColor(ColorStatus.RED);
                daysFromInfection = currentState.configs.getDiseaseDuration()/3;
                setStationary(currentState.configs.getDiseaseDuration() - daysFromInfection);
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
    }


}

