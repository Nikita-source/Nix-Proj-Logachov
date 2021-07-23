package personmodel;

public class Person {
    @CsvMapping(value = "id")
    public int id;
    @CsvMapping(value = "name")
    public String name;
    @CsvMapping(value = "email")
    public String email;
    @CsvMapping(value = "gender")
    public Gender gender;
    @CsvMapping(value = "yearOfBirth")
    public int yearOfBirth;

    @Override
    public String toString() {
        return "personmodel.Person{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", gender=" + gender +
                ", yearOfBirth=" + yearOfBirth +
                '}';
    }
}
