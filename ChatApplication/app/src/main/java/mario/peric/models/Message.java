package mario.peric.models;

import java.util.Calendar;
import java.util.Date;

public class Message {

    private Contact mSender;
    private Contact mReceiver;
    private String mMessage;
    private Date mTime;

    public Message(Contact sender, Contact receiver, String message) {
        mSender = sender;
        mReceiver = receiver;
        mMessage = message;
        mTime = Calendar.getInstance().getTime();
    }

    public Contact getSender() {
        return mSender;
    }

    public Contact getReceiver() {
        return mReceiver;
    }

    public void setSender(Contact sender) {
        mSender = sender;
    }

    public String getMessage() {
        return mMessage;
    }

    public Date getTime() {
        return mTime;
    }
}
