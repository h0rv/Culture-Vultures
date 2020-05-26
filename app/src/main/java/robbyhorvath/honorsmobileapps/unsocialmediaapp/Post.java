package robbyhorvath.honorsmobileapps.unsocialmediaapp;

import java.util.Calendar;


public class Post {
    private String postId;
    private String description;
    private String posterId;
    private String timePosted;

    public Post() {
        // public empty constructor
    }

    public Post(String postId, String description, String posterId) {
        this.postId = postId;
        this.description = description;
        this.posterId = posterId;
        timePosted = Calendar.getInstance().getTime().toString();
    }

    public Post(String postId, String description, String posterId, String timePosted) {
        this.postId = postId;
        this.description = description;
        this.posterId = posterId;
        this.timePosted = timePosted;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPosterId() {
        return posterId;
    }

    public void setPosterId(String posterId) {
        this.posterId = posterId;
    }

    public String getTimePosted() {
        return timePosted;
    }

    public void setTimePosted(String timePosted) {
        this.timePosted = timePosted;
    }

}
