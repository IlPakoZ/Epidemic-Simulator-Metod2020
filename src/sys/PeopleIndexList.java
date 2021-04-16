package sys;

import sys.Core.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PeopleIndexList implements Iterable<Integer> {


    private List<Integer> arrayList = new ArrayList<>();

    /**
     * Aggiunge un elemento all'arrayList.
     *
     * @param person    indice della persona in startingPopulation.
     */
    @Ready
    void addElement(Integer person){
        arrayList.add(person);
    }

    @Override
    public Iterator<Integer> iterator() {
        return arrayList.iterator();
    }
}
