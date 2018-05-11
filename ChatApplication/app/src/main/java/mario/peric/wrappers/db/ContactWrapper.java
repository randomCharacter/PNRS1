package mario.peric.wrappers.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import mario.peric.helpers.DBHelper;
import mario.peric.models.Contact;

public class ContactWrapper {

    private DBHelper mHelper = null;
    
    public ContactWrapper(Context context) {
        super();
        mHelper = new DBHelper(context);
    }

    public void insertContact(Contact contact) {
        SQLiteDatabase db = mHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_USERNAME, contact.getUsername());
        values.put(DBHelper.COLUMN_FIRST_NAME, contact.getFirstName());
        values.put(DBHelper.COLUMN_LAST_NAME, contact.getLastName());

        db.insert(DBHelper.CONTACTS_TABLE_NAME, null, values);
        db.close();
    }

    public Contact getContact(String username) {
        SQLiteDatabase db = mHelper.getReadableDatabase();

        Cursor cursor = db.query(DBHelper.CONTACTS_TABLE_NAME, null, DBHelper.COLUMN_USERNAME + "=?",
                new String[] {username}, null, null, null);

        if (cursor.getCount() <= 0) {
            return null;
        }

        cursor.moveToFirst();
        Contact contact = createContact(cursor);

        db.close();

        return contact;
    }

    public Contact[] getContacts() {
        SQLiteDatabase db = mHelper.getReadableDatabase();

        Cursor cursor = db.query(DBHelper.CONTACTS_TABLE_NAME, null, null, null, null, null, null);

        if (cursor.getCount() <= 0) {
            return null;
        }

        Contact[] contacts = new Contact[cursor.getCount()];

        int i = 0;
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            contacts[i++] = createContact(cursor);
        }

        db.close();

        return contacts;
    }

    public Contact getContact(int id) {
        SQLiteDatabase db = mHelper.getReadableDatabase();

        Cursor cursor = db.query(DBHelper.CONTACTS_TABLE_NAME, null,
                DBHelper.COLUMN_CONTACT_ID + "=?", new String[] {Integer.toString(id)},
                null, null, null);

        if (cursor.getCount() <= 0) {
            return null;
        }

        cursor.moveToFirst();
        Contact contact = createContact(cursor);

        db.close();

        return contact;
    }

    public void deleteContact(int id) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        db.delete(DBHelper.CONTACTS_TABLE_NAME, DBHelper.COLUMN_CONTACT_ID + "=?",
                new String[] {Integer.toString(id)});
        db.close();
    }

    private Contact createContact(Cursor cursor) {
        int contactId = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_CONTACT_ID)));
        String username = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_USERNAME));
        String firstName = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_FIRST_NAME));
        String lastName = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_LAST_NAME));

        return new Contact(contactId, username, firstName, lastName);
    }

}
