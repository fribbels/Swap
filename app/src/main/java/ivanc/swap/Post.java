package ivanc.swap;

/**
 * Created by ivanc on 12/1/2016.
 */

public class Post {
    private String title;
    private String description;

    public Post (String title) {
        this.title = title;
        this.description = "";
    }

    public Post (String title, String description) {
        this.title = title;
        this.description = description;
    }

    public String getTitle () {
        return title;
    }
    public String getDescription () {
        return description;
    }
}
