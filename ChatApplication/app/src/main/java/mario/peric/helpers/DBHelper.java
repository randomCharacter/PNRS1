package mario.peric.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import mario.peric.models.Contact;
import mario.peric.models.Message;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "chat_app.db";
    public static final int DATABASE_VERSION = 1;

    public static final String CONTACTS_TABLE_NAME = "contacts";
    public static final String COLUMN_CONTACT_ID = "contact_id";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_FIRST_NAME = "firstname";
    public static final String COLUMN_LAST_NAME = "lastname";

    public static final String MESSAGES_TABLE_NAME = "messages";
    public static final String COLUMN_MESSAGE_ID = "message_id";
    public static final String COLUMN_SENDER = "sender_id";
    public static final String COLUMN_RECEIVER = "receiver_id";
    public static final String COLUMN_MESSAGE = "message";

    private SQLiteDatabase mDb = null;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + CONTACTS_TABLE_NAME + " (" +
                COLUMN_CONTACT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_USERNAME + " TEXT, " +
                COLUMN_FIRST_NAME + " TEXT, " +
                COLUMN_LAST_NAME + " TEXT " +
                ");" );
        db.execSQL("CREATE TABLE " + MESSAGES_TABLE_NAME + " (" +
                COLUMN_MESSAGE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_SENDER + " INTEGER, " +
                COLUMN_RECEIVER + " INTEGER, " +
                COLUMN_MESSAGE + " TEXT, " +
                " FOREIGN KEY (" + COLUMN_SENDER + ") REFERENCES " + DBHelper.CONTACTS_TABLE_NAME + "(" + DBHelper.COLUMN_CONTACT_ID + ")," +
                " FOREIGN KEY (" + COLUMN_RECEIVER + ") REFERENCES " + DBHelper.CONTACTS_TABLE_NAME + "(" + DBHelper.COLUMN_CONTACT_ID + ")" +
                ");" );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
