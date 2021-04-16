package sys;
import sys.Core.*;
import assets.Person;

public class Queue<T> {
    /**
     * Un'implementazione di una coda semplice, utilizzata per fare i tamponi.
     */

    private Node first = null;
    private Node last = null;
    private int size = 0;

    private class Node{
        private T item;
        private Node next;

        Node(T item, Node next){
            this.item = item;
            this.next = next;
        }
    }

    /**
     * Elimina l'elemento in cima alla coda e lo restituisce, se la coda non è vuota.
     *
     * @return  l'elemento in cima alla coda
     * @throws  IndexOutOfBoundsException
     */
    @Ready
    public T dequeue() throws IndexOutOfBoundsException{
        if (size!=0){
            T item = first.item;
            first = first.next;
            size--;
            return item;
        }
        first = last;
        throw new IndexOutOfBoundsException();
    }

    /**
     * Aggiunge l'elemento preso in input alla coda, in ultima posizione
     * .
     * @param item  l'elemento da aggiungere alla coda
     */
    @Ready
    public void enqueue(T item){
        Node oldLast = last;
        last = new Node(item, null);
        if (size == 0) first = last;
        else oldLast.next = last;
        size++;
    }

    /**
     * Restituisce la grandezza della coda.
     *
     * @return  la grandezza della coda
     */
    @Ready
    public int getSize(){
        return size;
    }
}
