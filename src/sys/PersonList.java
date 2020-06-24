package sys;

import assets.Person;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PersonList implements Iterable<Person> {


    private List<Person> arrayList = new ArrayList<>();

    void addElement(Person person){
        arrayList.add(person);
    }

    @Override
    public Iterator<Person> iterator() {
        return arrayList.iterator();
    }
}
