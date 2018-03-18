package mario.peric;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher {

    private static final int MIN_USERNAME_LENGTH = 1;
    private static final int MIN_PASSWORD_LENGTH = 5;

    Button buttonRegister, buttonLogin;
    EditText username, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonRegister = findViewById(R.id.button_register);
        buttonLogin = findViewById(R.id.button_login);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);

        buttonLogin.setEnabled(false);

        buttonRegister.setOnClickListener(this);
        buttonLogin.setOnClickListener(this);
        username.addTextChangedListener(this);
        password.addTextChangedListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_register:
                Intent loginIntent = new Intent(this, RegisterActivity.class);
                startActivity(loginIntent);
                break;
            case R.id.button_login:
                Intent registerIntent = new Intent(this, ContactsActivity.class);
                startActivity(registerIntent);
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (username.getText().length() >= MIN_USERNAME_LENGTH &&
                password.getText().length() >= MIN_PASSWORD_LENGTH) {
            buttonLogin.setEnabled(true);
        } else {
            buttonLogin.setEnabled(false);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}
