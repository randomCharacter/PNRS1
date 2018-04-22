package mario.peric.providers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import mario.peric.helpers.DBHelper;
import mario.peric.models.Contact;
import mario.peric.models.Message;


public class MessageProvider {

    private DBHelper mHelper;
    private ContactProvider mContactProvider;

    public MessageProvider(Context context) {
        super();
        mHelper = new DBHelper(context);
        mContactProvider = new ContactProvider(context);
    }

    public void insertMessage(Message message) {
        SQLiteDatabase db = mHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_SENDER, message.getSender().getId());
        values.put(DBHelper.COLUMN_RECEIVER, message.getReceiver().getId());
        values.put(DBHelper.COLUMN_MESSAGE, message.getMessage());

        db.insert(DBHelper.MESSAGES_TABLE_NAME, null, values);
        db.close();
    }

    public Message[] getMessages() {
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor = db.query(DBHelper.MESSAGES_TABLE_NAME, null, null, null, null, null, null);

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
        SQLiteDatabase db = mHelper.getReadableDatabase();

        Cursor cursor = db.query(DBHelper.MESSAGES_TABLE_NAME, null,
                DBHelper.COLUMN_MESSAGE_ID + "=?", new String[] {Integer.toString(id)}, null,
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
        SQLiteDatabase db = mHelper.getReadableDatabase();

        Cursor cursor = db.query(DBHelper.MESSAGES_TABLE_NAME, null,
                "(" + DBHelper.COLUMN_SENDER + "=? AND " + DBHelper.COLUMN_RECEIVER + "=?) OR " +
                        "(" + DBHelper.COLUMN_SENDER + "=? AND " + DBHelper.COLUMN_RECEIVER + "=?)",
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
        SQLiteDatabase db = mHelper.getWritableDatabase();
        db.delete(DBHelper.MESSAGES_TABLE_NAME, DBHelper.COLUMN_MESSAGE_ID + "=?",
                new String[] {Integer.toString(id)});
        db.close();
    }

    private Message createMessage(Cursor cursor) {
        int messageId = cursor.getInt(cursor.getColumnIndex(DBHelper.COLUMN_MESSAGE_ID));
        int senderId = cursor.getInt(cursor.getColumnIndex(DBHelper.COLUMN_SENDER));
        int receiverId = cursor.getInt(cursor.getColumnIndex(DBHelper.COLUMN_RECEIVER));
        String message = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_MESSAGE));

        Contact sender = mContactProvider.getContact(senderId);
        Contact receiver = mContactProvider.getContact(receiverId);

        return new Message(messageId, sender, receiver, message);
    }
}
