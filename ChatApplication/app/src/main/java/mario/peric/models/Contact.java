package mario.peric.models;

public class Contact {

    public static final String ID = "id";

    private String mUsername;
    private String mPassword;
    private String mEmail;

    public Contact(String username, String password, String email) {
        mUsername = username;
        mPassword = password;
        mEmail = email;
    }

    public Contact(String username) {
        mUsername = username;
    }

    public String getUsername() {
        return mUsername;
    }
    
}
