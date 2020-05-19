package robbyhorvath.honorsmobileapps.unsocialmediaapp;

public class User {


    private String username;
    private String bio;
    private String[] posts;
    private String profilePicUrl;

    public User() {
    }

    public User(String email, String username) {
        this.email = email;
        this.username = username;
        bio = "Add Bio.";
        profilePicUrl = "https://t3.ftcdn.net/jpg/00/64/67/80/240_F_64678017_zUpiZFjj04cnLri7oADnyMH0XBYyQghG.jpg";
    }


    private String email;

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
    public String[] getPosts() {
        return posts;
    }

    public void setPosts(String[] posts) {
        this.posts = posts;
    }

    public String getProfilePicUrl() {
        return profilePicUrl;
    }

    public void setProfilePicUrl(String profilePicUrl) {
        this.profilePicUrl = profilePicUrl;
    }

}
