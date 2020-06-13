package sys;
import sys.Core.*;
import assets.Person;

public class Queue<T> {
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

    @Ready
    public void enqueue(T item){
        Node oldLast = last;
        last = new Node(item, null);
        if (size == 0) first = last;
        else oldLast.next = last;
        size++;
    }

    @Ready
    public int getSize(){
        return size;
    }
}
