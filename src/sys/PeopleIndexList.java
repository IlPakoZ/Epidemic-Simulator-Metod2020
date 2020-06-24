package sys;

import assets.Person;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PeopleIndexList implements Iterable<Integer> {


    private List<Integer> arrayList = new ArrayList<>();

    void addElement(Integer person){
        arrayList.add(person);
    }

    @Override
    public Iterator<Integer> iterator() {
        return arrayList.iterator();
    }
}
