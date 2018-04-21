package mario.peric.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import mario.peric.models.Contact;
import mario.peric.models.Message;
import mario.peric.providers.ContactProvider;

public class MessageDBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "chat_app_messages.db";
    public static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "messages";
    public static final String COLUMN_MESSAGE_ID = "message_id";
    public static final String COLUMN_SENDER = "sender_id";
    public static final String COLUMN_RECEIVER = "receiver_id";
    public static final String COLUMN_MESSAGE = "message";

    private SQLiteDatabase mDb = null;
    private ContactDBHelper mContactHelper = null;

    public MessageDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContactHelper = new ContactDBHelper(context);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_MESSAGE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_SENDER + " INTEGER, " +
                COLUMN_RECEIVER + " INTEGER, " +
                COLUMN_MESSAGE + " TEXT, " +
                " FOREIGN KEY (" + COLUMN_SENDER + ") REFERENCES " + ContactDBHelper.TABLE_NAME + "(" + ContactDBHelper.COLUMN_CONTACT_ID + ")," +
                " FOREIGN KEY (" + COLUMN_RECEIVER + ") REFERENCES " + ContactDBHelper.TABLE_NAME + "(" + ContactDBHelper.COLUMN_CONTACT_ID + ")" +
                ");" );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insertMessage(Message message) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_SENDER, message.getSender().getId());
        values.put(COLUMN_RECEIVER, message.getReceiver().getId());
        values.put(COLUMN_MESSAGE, message.getMessage());

        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public Message[] getMessages() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null);

        if (cursor.getCount() <= 0) {
            return null;
        }

        Message[] messages = new Message[cursor.getCount()];

        int i = 0;
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            messages[i++] = createMessage(cursor);
        }

        db.close();

        return messages;
    }

    public Message getMessage(int id) {
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME, null,
                COLUMN_MESSAGE_ID + "=?", new String[] {Integer.toString(id)}, null,
                null, null, null);

        if (cursor.getCount() <= 0) {
            return null;
        }

        cursor.moveToFirst();
        Message message = createMessage(cursor);

        db.close();

        return message;
    }

    public Message[] getMessages(int contactId1, int contactId2) {
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME, null,
                "(" + COLUMN_SENDER + "=? AND " + COLUMN_RECEIVER + "=?) OR " +
                        "(" + COLUMN_SENDER + "=? AND " + COLUMN_RECEIVER + "=?)",
                new String[] {Integer.toString(contactId1), Integer.toString(contactId2),
                        Integer.toString(contactId2), Integer.toString(contactId1)},
                null, null, null, null);

        if (cursor.getCount() <= 0) {
            return null;
        }

        Message[] messages = new Message[cursor.getCount()];

        int i = 0;
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            messages[i++] = createMessage(cursor);
        }

        db.close();

        return messages;
    }

    public void deleteMessage(int id) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_MESSAGE_ID + "=?",
                new String[] {Integer.toString(id)});
        db.close();
    }

    private Message createMessage(Cursor cursor) {
        int messageId = cursor.getInt(cursor.getColumnIndex(COLUMN_MESSAGE_ID));
        int senderId = cursor.getInt(cursor.getColumnIndex(COLUMN_SENDER));
        int receiverId = cursor.getInt(cursor.getColumnIndex(COLUMN_RECEIVER));
        String message = cursor.getString(cursor.getColumnIndex(COLUMN_MESSAGE));

        Contact sender = mContactHelper.getContact(senderId);
        Contact receiver = mContactHelper.getContact(receiverId);

        return new Message(messageId, sender, receiver, message);
    }
}
