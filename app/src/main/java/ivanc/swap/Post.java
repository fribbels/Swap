package ivanc.swap;

/**
 * Created by ivanc on 12/1/2016.
 */

public class Post {
    private String title;
    private String image;
    private String description;
    private double longitude;
    private double latitude;
    private String userid;
    private String username;
    private String timestamp;

    public Post (String title) {
        this.title = title;
        this.description = "";
        this.longitude = 0;
        this.latitude = 0;
        this.userid = "Anonymous";
        this.image = "";
        this.username = "";
    }

    public Post (String title, String description, String image, String userid) {
        this.title = title;
        this.description = description;
        this.longitude = 0;
        this.latitude = 0;
        this.userid = userid;
        this.image = image;
    }
    public Post (String title, String description, String image, String userid, String username) {
        this.title = title;
        this.description = description;
        this.longitude = 0;
        this.latitude = 0;
        this.userid = userid;
        this.image = image;
        this.username = username;
    }
    public Post (String title, String description, String image, String userid, String username, String timestamp) {
        this.title = title;
        this.description = description;
        this.longitude = 0;
        this.latitude = 0;
        this.userid = userid;
        this.image = image;
        this.username = username;
        this.timestamp = timestamp;
    }

    public String getTitle () {
        return this.title;
    }
    public String getDescription () {
        return this.description;
    }
    public double getLongitude () { return this.longitude; }
    public double getLatitude () { return this.latitude; }
    public String getUserid () { return this.userid; }
    public String getImage () { return this.image; }
    public String getUsername () { return this.username; }
    public String getTimestamp() { return this.timestamp; }

    public void setTitle (String title) { this.title = title; }
    public void setDescription (String description) { this.description = description; }
    public void setLongitude (double longitude) { this.longitude = longitude; }
    public void setLatitude (double latitude) { this.latitude = latitude; }
    public void setUserid (String userid) { this.userid = userid; }
    public void getImage (String image) { this.image = image; }
    public void setUsername (String image) { this.username = username; }
    public void setTimestamp (String timestamp) { this.timestamp = timestamp; }
}
