package ivanc.swap;

/**
 * Created by ivanc on 12/1/2016.
 */

public class Post {
    private String title;
    private String description;
    private double longitude;
    private double latitude;
    private String id;

    public Post (String title) {
        this.title = title;
        this.description = "";
        this.longitude = 0;
        this.latitude = 0;
        this.id = "Anonymous";
    }

    public Post (String title, String description) {
        this.title = title;
        this.description = description;
        this.longitude = 0;
        this.latitude = 0;
        this.id = "Anonymous";
    }

    public String getTitle () {
        return this.title;
    }
    public String getDescription () {
        return this.description;
    }
    public double getLongitude () { return this.longitude; }
    public double getLatitude () { return this.latitude; }
    public String getId () { return this.id; }

    public void setTitle (String title) { this.title = title; }
    public void setDescription (String description) { this.description = description; }
    public void setLongitude (double longitude) { this.longitude = longitude; }
    public void setLatitude (double latitude) { this.latitude = latitude; }
    public void setId (String id) { this.id = id; }
}
