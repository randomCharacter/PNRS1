package mario.peric.models;

import java.util.Date;

public class User {

    private String username;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private String gender;
    private boolean notifications;
    private Date birthday;

    public User(String username, String password, String email,
                String firstName, String lastName, String gender,
                boolean notifications, Date birthday) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.notifications = notifications;
        this.birthday = birthday;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getGender() {
        return gender;
    }

    public boolean isNotifications() {
        return notifications;
    }

    public Date getBirthday() {
        return birthday;
    }
}
