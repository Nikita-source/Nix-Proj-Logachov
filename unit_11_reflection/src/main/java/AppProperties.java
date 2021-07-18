public class AppProperties {
    @PropertyKey(value = "connections.title")
    public String title = "name";
    @PropertyKey(value = "connections.limit")
    public int maxConnections = 10;
    @PropertyKey(value = "connections.path")
    public String path = "wrong/path";
    @PropertyKey(value = "connections.connect")
    public boolean isConnect = false;

    @Override
    public String toString() {
        return "AppProperties{" +
                "title='" + title + '\'' +
                ", maxConnections=" + maxConnections +
                ", path='" + path + '\'' +
                ", isConnect=" + isConnect +
                '}';
    }
}
