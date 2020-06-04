package robbyhorvath.honorsmobileapps.unsocialmediaapp;

public class User {

    private String uid;
    private String email;
    private String username;
    private String bio;
    private String[] posts;

    public User() {
        // public empty constructor
    }

    public User(String uid, String email, String username) {
        // default new user constructor
        this.uid = uid;
        this.email = email;
        this.username = username;
        bio = "Add Bio.";
    }

    public User(String uid, String bio,String email, String username) {
        this.uid = uid;
        this.email = email;
        this.username = username;
        this.bio = bio;
    }

    public User(String uid, String bio, String[] posts, String email, String username) {
        this.uid = uid;
        this.email = email;
        this.posts = posts;
        this.username = username;
        this.bio = bio;
    }
    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String[] getPosts() {
        return posts;
    }

    public void setPosts(String[] posts) {
        this.posts = posts;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

}
