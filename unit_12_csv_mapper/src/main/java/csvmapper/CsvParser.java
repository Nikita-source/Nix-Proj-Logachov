package csvmapper;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CsvParser {
    private final String path;
    private List<String[]> csvData;

    public CsvParser(String path) {
        this.path = path;
        csvData = new ArrayList<>();
        csvData = read();
    }

    public List<String[]> getCsvData() {
        return csvData;
    }

    public String[] getRow(int row) {
        return csvData.get(row);
    }

    public Map<String, Integer> getHeaders() {
        String[] headers = csvData.get(0);
        Map<String, Integer> headersMap = new HashMap<>();
        for (int i = 0; i < headers.length; i++) {
            headersMap.put(headers[i], i);
        }
        return headersMap;
    }

    public String getElement(int row, String col) {
        if (getHeaders().containsKey(col)) {
            return csvData.get(row)[getHeaders().get(col)];
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
