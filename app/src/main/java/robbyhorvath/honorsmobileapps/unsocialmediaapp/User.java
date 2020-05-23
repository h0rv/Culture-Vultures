package robbyhorvath.honorsmobileapps.unsocialmediaapp;

public class User {

    private String email;
    private String username;
    private String bio;

    public User() {
        // public empty constructor
    }

    public User(String email, String username) {
        // default new user constructor
        this.email = email;
        this.username = username;
        bio = "Add Bio.";
    }

    public User(String email, String username, String bio, String profilePicUrl) {
        this.email = email;
        this.username = username;
        this.bio = bio;
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
