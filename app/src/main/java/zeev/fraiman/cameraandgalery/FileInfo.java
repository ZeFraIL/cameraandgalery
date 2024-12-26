package zeev.fraiman.cameraandgalery;

public class FileInfo {
    private String name;
    private String date;
    private String time;
    private String uri;
    private String type;

    public FileInfo(String name, String date, String time, String uri, String type) {
        this.name = name;
        this.date = date;
        this.time = time;
        this.uri = uri;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getUri() {
        return uri;
    }

    public String getType() {
        return type;
    }
}