package robbyhorvath.honorsmobileapps.unsocialmediaapp;

public class User {



    private String email;
    private String username;
    private String bio;
    private String profilePicUrl;

    public User() {
        // public empty constructor
    }

    public User(String email, String username) {
        // default new user constructor
        this.email = email;
        this.username = username;
        bio = "Add Bio.";
        profilePicUrl = "https://t3.ftcdn.net/jpg/00/64/67/80/240_F_64678017_zUpiZFjj04cnLri7oADnyMH0XBYyQghG.jpg";
    }

    public User(String email, String username, String bio, String profilePicUrl) {
        this.email = email;
        this.username = username;
        this.bio = bio;
        this.profilePicUrl = profilePicUrl;
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

    public String getProfilePicUrl() {
        return profilePicUrl;
    }

    public void setProfilePicUrl(String profilePicUrl) {
        this.profilePicUrl = profilePicUrl;
    }

}
