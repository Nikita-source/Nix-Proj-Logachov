import csvmapper.CsvMapper;
import csvmapper.CsvParser;
import personmodel.Person;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class MapperMain {
    public static void main(String[] args) {
        System.out.println("Please write data file path (unit_12_csv_mapper/src/main/resources/person.csv)");
        String path = "";
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        try {
            path = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        CsvParser csvParser = new CsvParser(path);
        CsvMapper csvMapper = new CsvMapper();
        List<Person> persons = csvMapper.crateListOfObjects(Person.class, csvParser);
        for (Person person : persons) {
            System.out.println(person);
        }
    }
}
