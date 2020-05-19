package assets;

import sys.Core.*;

public class Person {

    private int age;
    private int index;
    private ColorStatus color = ColorStatus.GREEN;
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
    Person(int age, int index){
        this.age = age;
        this.index = index;
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
     *
     */
    @NotImplemented
    public void refresh(){ if (isInfected); }

    /**
     * Metodo di supporto interno utilizzato per settare
     * la persona di un determinato colore.
     *
     * @param color     colore di destinazione
     */
    @NotImplemented
    private void makeOfColor(ColorStatus color){ }

    /**
     * Metodo utilizzato per settare una persona come infetta.
     * Usare questo metodo è sconsigliato ed è stato pensato
     * solo per l'infezione del paziente zero.
     */
    @ToRevise
    public void setAsInfected() {isInfected = true;}

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
}

