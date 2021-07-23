package csvmapper;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CsvParser {
    private String path;
    private List<String[]> csvData;

    public CsvParser() {
        this.path = ClassLoader.getSystemClassLoader().getResource("person.csv").getPath();
        csvData = new ArrayList<>();
        csvData = read();
    }

    public List<String[]> getCsvData() {
        return csvData;
    }

    public String[] getRow(int row) {
        return csvData.get(row);
    }

    public String[] getHeaders() {
        return csvData.get(0);
    }

    public String getElement(int row, String col) {
        for (int i = 0; i < getHeaders().length; i++) {
            if (getHeaders()[i].equals(col)) {
                return csvData.get(row)[i];
            }
        }
        throw new RuntimeException("Column does not exist: " + col);
    }

    public String getElement(int row, int col) {
        return csvData.get(row)[col - 1];
    }

    private List<String[]> read() {
        try (CSVReader reader = new CSVReader(new FileReader(path))) {
            return reader.readAll();
        } catch (CsvException | IOException ex) {
            throw new RuntimeException(ex);
        }
    }

}
