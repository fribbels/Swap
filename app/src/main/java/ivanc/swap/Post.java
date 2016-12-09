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

    public Post (String title) {
        this.title = title;
        this.description = "";
        this.longitude = 0;
        this.latitude = 0;
        this.userid = "Anonymous";
        this.image = "";
    }

    public Post (String title, String description) {
        this.title = title;
        this.description = description;
        this.longitude = 0;
        this.latitude = 0;
        this.userid = "Anonymous";
        this.image = "";
    }
    public Post (String title, String description, String image) {
        this.title = title;
        this.description = description;
        this.longitude = 0;
        this.latitude = 0;
        this.userid = "Anonymous";
        this.image = image;
    }
    public Post (String title, String description, String image, String userid) {
        this.title = title;
        this.description = description;
        this.longitude = 0;
        this.latitude = 0;
        this.userid = userid;
        this.image = image;
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

    public void setTitle (String title) { this.title = title; }
    public void setDescription (String description) { this.description = description; }
    public void setLongitude (double longitude) { this.longitude = longitude; }
    public void setLatitude (double latitude) { this.latitude = latitude; }
    public void setUserid (String userid) { this.userid = userid; }
    public void getImage (String image) { this.image = image; }
}
