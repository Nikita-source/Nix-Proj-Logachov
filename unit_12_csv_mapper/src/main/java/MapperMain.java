import csvmapper.CsvMapper;
import personmodel.Person;

import java.util.List;

public class MapperMain {
    public static void main(String[] args) {
        CsvMapper csvMapper = new CsvMapper();
        List<Person> persons = csvMapper.crateListOfObjects(Person.class);
        for (Person person : persons) {
            System.out.println(person);
        }
    }
}
