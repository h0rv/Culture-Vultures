package robbyhorvath.honorsmobileapps.unsocialmediaapp;

public class User {


    private String email;
    private String username;
    private String bio;
    private String profilePictureId;
    private String[] postsIDs;

    public User() {
    }

    public User(String email, String username) {
        this.email = email;
        this.username = username;
        bio = "Add Bio.";
        profilePictureId = "";
        postsIDs = new String[0];
    }
}
