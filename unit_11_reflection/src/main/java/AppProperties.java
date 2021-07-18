public class AppProperties {
    @PropertyKey(value = "connections.title")
    public String title;
    @PropertyKey(value = "connections.limit")
    public int maxConnections;
    @PropertyKey(value = "connections.path")
    public String path;
    @PropertyKey(value = "connections.connect")
    public boolean isConnect;

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
