package mario.peric.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

import mario.peric.R;
import mario.peric.models.Contact;
import mario.peric.wrappers.db.ContactWrapper;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener, View.OnFocusChangeListener, TextWatcher {

    private static final int MIN_PASSWORD_LENGTH = 5;
    private static final int MIN_EMAIL_LENGTH = 5;
    private static final int MIN_USERNAME_LENGTH = 1;

    EditText username, password, firstName, lastName, email;
    Spinner gender;
    CheckBox notifications;
    DatePicker calendar;
    Button buttonRegister;
    ContactWrapper mContactWrapper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username = findViewById(R.id.register_username);
        password = findViewById(R.id.register_password);
        firstName = findViewById(R.id.register_first_name);
        lastName = findViewById(R.id.register_last_name);
        email = findViewById(R.id.register_email);
        calendar = findViewById(R.id.calendar);
        notifications = findViewById(R.id.notifications);
        gender = findViewById(R.id.gender);
        buttonRegister = findViewById(R.id.button_register);

        mContactWrapper = new ContactWrapper(this);

        // Add genders to spinner
        String[] genders = { getString(R.string.male), getString(R.string.female) };
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                R.layout.support_simple_spinner_dropdown_item, genders);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        gender.setAdapter(adapter);

        // Set today's date as max date user can choose
        Date date = Calendar.getInstance().getTime();
        calendar.setMaxDate(date.getTime());

        buttonRegister.setEnabled(false);

        username.addTextChangedListener(this);
        password.addTextChangedListener(this);
        email.addTextChangedListener(this);
        buttonRegister.setOnClickListener(this);
        username.setOnFocusChangeListener(this);
        password.setOnFocusChangeListener(this);
        email.setOnFocusChangeListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_register:
                if (mContactWrapper.getContact(username.getText().toString()) == null) {
                    Contact contact = new Contact(0, username.getText().toString(),
                            firstName.getText().toString(), lastName.getText().toString());
                    mContactWrapper.insertContact(contact);
                    Intent loginIntent = new Intent(getApplicationContext(), MainActivity.class);
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(loginIntent);
                } else {
                    Toast.makeText(getApplicationContext(), R.string.username_exists,
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
        if (username.length() >= MIN_USERNAME_LENGTH &&
                password.length() >= MIN_PASSWORD_LENGTH &&
                email.length() > MIN_EMAIL_LENGTH) {
            buttonRegister.setEnabled(true);
        } else {
            buttonRegister.setEnabled(false);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        if (!hasFocus) {
            switch (view.getId()) {
                case R.id.register_username:
                    if (username.length() < MIN_USERNAME_LENGTH) {
                        username.setError(getString(R.string.invalid_value));
                    }
                    break;
                case R.id.register_password:
                    if (password.length() < MIN_PASSWORD_LENGTH) {
                        password.setError(getString(R.string.invalid_value));
                    }
                    break;
                case R.id.register_email:
                    if (email.length() < MIN_EMAIL_LENGTH) {
                        email.setError(getString(R.string.invalid_value));
                    }
                    break;
            }
        }
    }
}
