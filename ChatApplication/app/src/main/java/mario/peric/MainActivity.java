package mario.peric;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

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

        TextWatcher checkInput = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (username.getText().length() > 0 && password.getText().length() > 5) {
                    buttonLogin.setEnabled(true);
                } else {
                    buttonLogin.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };

        buttonRegister.setOnClickListener(this);
        buttonLogin.setOnClickListener(this);
        username.addTextChangedListener(checkInput);
        password.addTextChangedListener(checkInput);
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
}
