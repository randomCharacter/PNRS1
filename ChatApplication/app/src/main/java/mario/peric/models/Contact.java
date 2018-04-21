package mario.peric.models;

import java.util.Date;

public class Contact {

    public static final String ID = "id";

    private int mId;

    public int getId() {
        return mId;
    }

    private String mUsername;
    private String mPassword;
    private String mEmail;
    private String mFirstName;
    private String mLastName;
    private String mGender;
    private boolean mNotifications;
    private Date mBirthday;

    public Contact(int id, String username, String password, String email,
                   String firstName, String lastName, String gender,
                   boolean notifications, Date birthday) {
        mId = id;
        mUsername = username;
        mPassword = password;
        mEmail = email;
        mFirstName = firstName;
        mLastName = lastName;
        mGender = gender;
        mNotifications = notifications;
        mBirthday = birthday;
    }

    public Contact(int id, String username, String firstName, String lastName) {
        mId = id;
        mUsername = username;
        mFirstName = firstName;
        mLastName = lastName;
    }

    public String getUsername() {
        return mUsername;
    }

    public String getPassword() {
        return mPassword;
    }

    public String getEmail() {
        return mEmail;
    }

    public String getFirstName() {
        return mFirstName;
    }

    public String getLastName() {
        return mLastName;
    }

    public String getGender() {
        return mGender;
    }

    public boolean isNotifications() {
        return mNotifications;
    }

    public Date getBirthday() {
        return mBirthday;
    }

    public String getFullName() {
        return getFirstName() + " " + getLastName();
    }
}
