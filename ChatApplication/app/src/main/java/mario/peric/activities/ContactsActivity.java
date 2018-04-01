package mario.peric.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.Calendar;
import java.util.Date;

import mario.peric.R;
import mario.peric.adapters.ContactAdapter;
import mario.peric.models.Contact;

public class ContactsActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mButtonLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        mButtonLogout = findViewById(R.id.button_log_out);

        mButtonLogout.setOnClickListener(this);

        ContactAdapter contactAdapter = new ContactAdapter(this);

        Date date = Calendar.getInstance().getTime();

        contactAdapter.addUser(new Contact(getString(R.string.sample_username), getString(R.string.sample_password),
                getString(R.string.sample_email), getString(R.string.sample_fname1), getString(R.string.sample_lname1),
                getString(R.string.male), false, date));
        contactAdapter.addUser(new Contact(getString(R.string.sample_username), getString(R.string.sample_password),
                getString(R.string.sample_email), getString(R.string.sample_fname2), getString(R.string.sample_lname2),
                getString(R.string.male), false, date));
        contactAdapter.addUser(new Contact(getString(R.string.sample_username), getString(R.string.sample_password),
                getString(R.string.sample_email), getString(R.string.sample_fname3), getString(R.string.sample_lname3),
                getString(R.string.male), false, date));
        contactAdapter.addUser(new Contact(getString(R.string.sample_username), getString(R.string.sample_password),
                getString(R.string.sample_email), getString(R.string.sample_fname4), getString(R.string.sample_lname4),
                getString(R.string.male), false, date));

        ListView contacts = findViewById(R.id.contacts);
        contacts.setAdapter(contactAdapter);
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
