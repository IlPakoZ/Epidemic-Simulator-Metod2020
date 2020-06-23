package sys;

import sys.models.IMenu;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public class Core {

    @Retention(RetentionPolicy.SOURCE)
    @Target({ElementType.METHOD,ElementType.CONSTRUCTOR})
    public @interface Debug{ boolean value() default true;}

    @Retention(RetentionPolicy.SOURCE)
    @Target({ElementType.METHOD,ElementType.CONSTRUCTOR})
    public @interface NotImplemented{ boolean value() default true;}

    @Retention(RetentionPolicy.SOURCE)
    @Target({ElementType.METHOD,ElementType.CONSTRUCTOR})
    public @interface ToRevise{ boolean value() default true;}

    @Retention(RetentionPolicy.SOURCE)
    @Target({ElementType.METHOD,ElementType.CONSTRUCTOR})
    public @interface Ready{ boolean value() default true;}

    private Simulation sim;

    /**
     * Crea un'istanza di una nuova simulazione
     * prendendo in input il menù col quale interagire.
     *
     * @param menu  implementazione dell'interfaccia IMenu
     */
    public void newSimulation (IMenu menu){ sim = new Simulation(menu); }

    /**
     * Dò il via alla simulazione salvata negli attributi.
     */
    @ToRevise
    public void run() {
        try {
            sim.run();
        } catch (UnsupportedOperationException usopex){
            System.out.println("Operazione non supportata. La simulazione verrà interrotta.");
        } catch (InterruptedException interex){
            System.out.println("C'è stata una interruzione imprevista del programma.");
        }
    }

    public Simulation debug() {
        sim.debug();
        return sim;
    }

}