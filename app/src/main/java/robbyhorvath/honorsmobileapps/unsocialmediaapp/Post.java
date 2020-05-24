package robbyhorvath.honorsmobileapps.unsocialmediaapp;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.ServerValue;

import java.util.HashMap;


public class Post {

    private String description;
    private String posterId;
    private HashMap<String, Object> timePosted;

    public Post() {
        // public empty constructor
    }

    public Post(String description, String posterId) {
        this.description = description;
        this.posterId = posterId;
        timePosted = new HashMap<>();
        timePosted.put("timestamp", ServerValue.TIMESTAMP);
    }

    public Post(String description, String posterId, HashMap<String, Object> timePosted) {
        this.description = description;
        this.posterId = posterId;
        this.timePosted = timePosted;
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

    public HashMap<String, Object> getTimePosted() {
        return timePosted;
    }

    @Exclude
    public long getTimestampCreatedLong() {
        return (long) timePosted.get("timestamp");
    }

    public void setTimePosted(HashMap<String, Object> timePosted) {
        this.timePosted = timePosted;
    }


}
