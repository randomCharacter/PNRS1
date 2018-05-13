package mario.peric.activities;

import android.content.Intent;
import android.os.Handler;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import mario.peric.R;
import mario.peric.helpers.HTTPHelper;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener, View.OnFocusChangeListener, TextWatcher {

    private static final int MIN_PASSWORD_LENGTH = 5;
    private static final int MIN_EMAIL_LENGTH = 5;
    private static final int MIN_USERNAME_LENGTH = 1;

    EditText mUsername, mPassword, mFirstName, mLastName, mEmail;
    Spinner mGender;
    CheckBox mNotifications;
    DatePicker mCalendar;
    Button mButtonRegister;
    HTTPHelper mHTTPHelper;
    Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mHTTPHelper = new HTTPHelper();
        mHandler = new Handler();

        mUsername = findViewById(R.id.register_username);
        mPassword = findViewById(R.id.register_password);
        mFirstName = findViewById(R.id.register_first_name);
        mLastName = findViewById(R.id.register_last_name);
        mEmail = findViewById(R.id.register_email);
        mCalendar = findViewById(R.id.calendar);
        mNotifications = findViewById(R.id.notifications);
        mGender = findViewById(R.id.gender);
        mButtonRegister = findViewById(R.id.button_register);

        // Add genders to spinner
        String[] genders = { getString(R.string.male), getString(R.string.female) };
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                R.layout.support_simple_spinner_dropdown_item, genders);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        mGender.setAdapter(adapter);

        // Set today's date as max date user can choose
        Date date = Calendar.getInstance().getTime();
        mCalendar.setMaxDate(date.getTime());

        mButtonRegister.setEnabled(false);

        mUsername.addTextChangedListener(this);
        mPassword.addTextChangedListener(this);
        mEmail.addTextChangedListener(this);
        mButtonRegister.setOnClickListener(this);
        mUsername.setOnFocusChangeListener(this);
        mPassword.setOnFocusChangeListener(this);
        mEmail.setOnFocusChangeListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_register:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put(HTTPHelper.USERNAME, mUsername.getText().toString());
                            jsonObject.put(HTTPHelper.PASSWORD, mPassword.getText().toString());
                            jsonObject.put(HTTPHelper.EMAIL, mEmail.getText().toString());

                            final HTTPHelper.HTTPResponse res = mHTTPHelper.postJSONObjectFromURL(HTTPHelper.URL_REGISTER, jsonObject);

                            mHandler.post(new Runnable(){
                                public void run() {
                                    if (res.code == HTTPHelper.SUCCESS) {
                                        Toast.makeText(RegisterActivity.this, R.string.user_registered, Toast.LENGTH_LONG).show();
                                        Intent loginIntent = new Intent(getApplicationContext(), MainActivity.class);
                                        loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(loginIntent);
                                    } else {
                                        Toast.makeText(RegisterActivity.this, getString(R.string.error) + " " +
                                                res.code + ": " +res.message, Toast.LENGTH_LONG).show();
                                    }
                                }
                            });

                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (mUsername.length() >= MIN_USERNAME_LENGTH &&
                mPassword.length() >= MIN_PASSWORD_LENGTH &&
                mEmail.length() > MIN_EMAIL_LENGTH) {
            mButtonRegister.setEnabled(true);
        } else {
            mButtonRegister.setEnabled(false);
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
                    if (mUsername.length() < MIN_USERNAME_LENGTH) {
                        mUsername.setError(getString(R.string.invalid_value));
                    }
                    break;
                case R.id.register_password:
                    if (mPassword.length() < MIN_PASSWORD_LENGTH) {
                        mPassword.setError(getString(R.string.invalid_value));
                    }
                    break;
                case R.id.register_email:
                    if (mEmail.length() < MIN_EMAIL_LENGTH) {
                        mEmail.setError(getString(R.string.invalid_value));
                    }
                    break;
            }
        }
    }
}
