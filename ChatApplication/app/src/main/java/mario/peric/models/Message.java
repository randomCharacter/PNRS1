package mario.peric.models;

import java.util.Calendar;
import java.util.Date;

public class Message {

    private User sender;
    private User receiver;
    private String message;
    private Date time;

    public Message(User sender, User receiver, String message) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.time = Calendar.getInstance().getTime();
    }

    public User getSender() {
        return sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public String getMessage() {
        return message;
    }

    public Date getTime() {
        return time;
    }
}
