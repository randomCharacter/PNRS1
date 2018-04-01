package mario.peric.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import mario.peric.R;
import mario.peric.adapters.MessageAdapter;
import mario.peric.models.Contact;
import mario.peric.models.Message;

public class MessageActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher {

    private Button mButtonLogout, mButtonSend;
    private EditText mMessage;
    private MessageAdapter mMessageAdapter;
    private TextView mContactName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        mButtonLogout = findViewById(R.id.button_log_out);
        mButtonSend = findViewById(R.id.button_send);
        mMessage = findViewById(R.id.message_text);
        mContactName = findViewById(R.id.contact_name);

        mContactName.setText(getIntent().getStringExtra(Contact.NAME));

        mButtonSend.setEnabled(false);

        mMessage.addTextChangedListener(this);

        mButtonSend.setOnClickListener(this);
        mButtonLogout.setOnClickListener(this);

        mMessageAdapter = new MessageAdapter(this);

        ListView messages = findViewById(R.id.messages);

        messages.setAdapter(mMessageAdapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_log_out:
                Intent logoutIntent = new Intent(getApplicationContext(), MainActivity.class);
                logoutIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(logoutIntent);
                break;
            case R.id.button_send:
                Toast.makeText(getApplicationContext(), R.string.message_sent, Toast.LENGTH_LONG).show();
                Message message = new Message(null, null, mMessage.getText().toString());
                mMessageAdapter.addMessage(message);
                mMessageAdapter.notifyDataSetChanged();
                mMessage.setText("");
                mButtonSend.setEnabled(false);
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (charSequence.length() != 0) {
            mButtonSend.setEnabled(true);
        } else {
            mButtonSend.setEnabled(false);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}
