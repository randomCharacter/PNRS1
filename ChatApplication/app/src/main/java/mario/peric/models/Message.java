package mario.peric.models;

public class Message {

    private boolean mReceived;
    private String mMessage;

    public Message(String message, boolean received) {
        mMessage = message;
        mReceived = received;
    }

    public boolean getReceived() {
        return mReceived;
    }

    public String getMessage() {
        return mMessage;
    }

}
