package csvmapper;

import personmodel.CsvMapping;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CsvMapper {
    private final CsvParser csvParser = new CsvParser();

    private <T> T createObject(Class<T> propertyClass, int rowNumber) {
        try {
            T instance = propertyClass.getDeclaredConstructor().newInstance();

            for (Field field : propertyClass.getDeclaredFields()) {
                if (!field.isAnnotationPresent(CsvMapping.class)) {
                    continue;
                }

                CsvMapping csvMapping = field.getAnnotation(CsvMapping.class);
                List<String> headers = Arrays.asList(csvParser.getHeaders());
                if (!headers.contains(csvMapping.value())) {
                    continue;
                }

                var type = field.getType();
                String value = csvParser.getElement(rowNumber, csvMapping.value());

                if (type == String.class) {
                    field.set(instance, value);
                } else if (type == int.class || type == Integer.class) {
                    field.set(instance, Integer.parseInt(value));
                } else if (type == boolean.class || type == Boolean.class) {
                    field.set(instance, Boolean.parseBoolean(value));
                } else if (type == char.class || type == Character.class) {
                    field.set(instance, value.charAt(0));
                } else if (type == long.class || type == Long.class) {
                    field.set(instance, Long.parseLong(value));
                } else if (type == float.class || type == Float.class) {
                    field.set(instance, Float.parseFloat(value));
                } else if (type == double.class || type == Double.class) {
                    field.set(instance, Double.parseDouble(value));
                } else if (type.isEnum()) {
                    Enum en = Enum.valueOf((Class<Enum>) type, value);
                    field.set(instance, en);
                } else {
                    throw new RuntimeException("Property error");
                }
            }
            return instance;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> List<T> crateListOfObjects(Class<T> cl) {
        List<T> instances = new ArrayList<>();
        for (int i = 1; i < csvParser.getCsvData().size(); i++) {
            T instance = createObject(cl, i);
            instances.add(instance);
        }
        return instances;
    }
}
