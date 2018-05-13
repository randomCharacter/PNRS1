package mario.peric.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import mario.peric.R;
import mario.peric.helpers.HTTPHelper;
import mario.peric.utils.Preferences;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher {

    private static final int MIN_USERNAME_LENGTH = 1;
    private static final int MIN_PASSWORD_LENGTH = 5;

    Button mButtonRegister, mButtonLogin;
    EditText mUsername, mPassword;
    Handler mHandler;
    HTTPHelper mHTTPHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mHandler = new Handler();
        mHTTPHelper = new HTTPHelper();

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
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put(HTTPHelper.USERNAME, mUsername.getText().toString());
                            jsonObject.put(HTTPHelper.PASSWORD, mPassword.getText().toString());

                            final HTTPHelper.HTTPResponse res = mHTTPHelper.postJSONObjectFromURL(HTTPHelper.URL_LOGIN, jsonObject);

                            mHandler.post(new Runnable(){
                                public void run() {
                                    if (res.code == HTTPHelper.SUCCESS) {
                                        Toast.makeText(MainActivity.this, R.string.user_logged_in, Toast.LENGTH_LONG).show();
                                        Intent contactsIntent = new Intent(getApplicationContext(), ContactsActivity.class);

                                        SharedPreferences.Editor editor = getSharedPreferences(Preferences.NAME, Context.MODE_PRIVATE).edit();
                                        editor.putString(Preferences.SESSION_ID, res.sessionId);
                                        editor.putString(Preferences.USER_LOGGED_IN, mUsername.getText().toString());
                                        editor.apply();

                                        startActivity(contactsIntent);
                                    } else {
                                        Toast.makeText(MainActivity.this, getString(R.string.error) + " " +
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
