package mario.peric.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import mario.peric.R;
import mario.peric.adapters.ContactAdapter;
import mario.peric.helpers.ContactDBHelper;
import mario.peric.models.Contact;
import mario.peric.providers.ContactProvider;
import mario.peric.utils.Preferences;

public class ContactsActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mButtonLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        mButtonLogout = findViewById(R.id.button_log_out);

        mButtonLogout.setOnClickListener(this);

        ContactDBHelper helper = new ContactDBHelper(this);
        ContactAdapter contactAdapter = new ContactAdapter(this);

        Contact[] contacts = helper.getContacts();

        SharedPreferences sharedPref = getSharedPreferences(Preferences.NAME, Context.MODE_PRIVATE);
        int loggedUserId = sharedPref.getInt(Preferences.USER_LOGGED_IN, -1);

        for (Contact contact : contacts) {
            if (contact.getId() != loggedUserId) {
                contactAdapter.addContact(contact);
            }
        }

        ListView contactList = findViewById(R.id.contacts);
        contactList.setAdapter(contactAdapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_log_out:
                Intent logoutIntent = new Intent(getApplicationContext(), MainActivity.class);
                logoutIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(logoutIntent);
                break;
        }
    }
}
