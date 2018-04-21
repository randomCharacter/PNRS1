package mario.peric.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import mario.peric.R;
import mario.peric.helpers.ContactDBHelper;
import mario.peric.models.Contact;
import mario.peric.providers.ContactProvider;
import mario.peric.utils.Preferences;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher {

    private static final int MIN_USERNAME_LENGTH = 1;
    private static final int MIN_PASSWORD_LENGTH = 5;

    Button mButtonRegister, mButtonLogin;
    EditText mUsername, mPassword;
    ContactDBHelper mHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mHelper = new ContactDBHelper(this);

        mButtonRegister = findViewById(R.id.button_register);
        mButtonLogin = findViewById(R.id.button_login);
        mUsername = findViewById(R.id.username);
        mPassword = findViewById(R.id.password);

        mButtonLogin.setEnabled(false);

        mButtonRegister.setOnClickListener(this);
        mButtonLogin.setOnClickListener(this);
        mUsername.addTextChangedListener(this);
        mPassword.addTextChangedListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_register:
                Intent registerIntent = new Intent(this, RegisterActivity.class);
                startActivity(registerIntent);
                break;
            case R.id.button_login:
                Contact contact = mHelper.getContact(mUsername.getText().toString());
                if (contact != null) {
                    Intent loginIntent = new Intent(this, ContactsActivity.class);
                    SharedPreferences sharedPref = getSharedPreferences(Preferences.NAME,
                            Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putInt(Preferences.USER_LOGGED_IN, contact.getId());
                    editor.apply();
                    startActivity(loginIntent);
                } else {
                    Toast.makeText(getApplicationContext(), R.string.username_does_not_exists,
                            Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (mUsername.getText().length() >= MIN_USERNAME_LENGTH &&
                mPassword.getText().length() >= MIN_PASSWORD_LENGTH) {
            mButtonLogin.setEnabled(true);
        } else {
            mButtonLogin.setEnabled(false);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}
