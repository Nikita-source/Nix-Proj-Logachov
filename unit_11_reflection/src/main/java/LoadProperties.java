import java.io.*;
import java.util.Properties;

public class LoadProperties {
    private Properties properties;

    public Properties getProperties() {
        return loadProperties();
    }

    private static Properties loadProperties() {
        Properties props = new Properties();
        String path = ClassLoader.getSystemClassLoader().getResource("app.properties").getPath();
        try (Reader input = new FileReader(new File(path))) {
            props.load(input);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return props;
    }
}
