package mario.peric.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import mario.peric.R;

public class ContactsActivity extends AppCompatActivity implements View.OnClickListener {

    Button buttonLogout;
    ImageView contact;
    TextView contactName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        buttonLogout = findViewById(R.id.button_log_out);
        contact = findViewById(R.id.contact_image);
        contactName = findViewById(R.id.contact_name);

        contact.setOnClickListener(this);
        contactName.setOnClickListener(this);
        buttonLogout.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_log_out:
                Intent logoutIntent = new Intent(getApplicationContext(), MainActivity.class);
                logoutIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(logoutIntent);
                break;
            case R.id.contact_image:
            case R.id.contact_name:
                Intent messageIntent = new Intent(this, MessageActivity.class);
                startActivity(messageIntent);
                break;
        }
    }
}
