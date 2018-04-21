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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import mario.peric.R;
import mario.peric.adapters.MessageAdapter;
import mario.peric.helpers.ContactDBHelper;
import mario.peric.helpers.MessageDBHelper;
import mario.peric.models.Contact;
import mario.peric.models.Message;
import mario.peric.utils.Preferences;

public class MessageActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher {

    private Button mButtonLogout, mButtonSend;
    private EditText mMessage;
    private MessageAdapter mMessageAdapter;
    private TextView mContactName;
    private ContactDBHelper mContactHelper;
    private Contact mReceiver, mSender;
    private MessageDBHelper mMessageHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        mContactHelper = new ContactDBHelper(this);
        mMessageHelper = new MessageDBHelper(this);

        mButtonLogout = findViewById(R.id.button_log_out);
        mButtonSend = findViewById(R.id.button_send);
        mMessage = findViewById(R.id.message_text);
        mContactName = findViewById(R.id.contact_name);

        int receiverId = getIntent().getIntExtra(Contact.ID, -1);
        mReceiver = mContactHelper.getContact(receiverId);

        SharedPreferences sharedPref = getSharedPreferences(Preferences.NAME, Context.MODE_PRIVATE);
        int senderId = sharedPref.getInt(Preferences.USER_LOGGED_IN, -1);
        mSender = mContactHelper.getContact(senderId);

        mContactName.setText(mReceiver.getFullName());

        mButtonSend.setEnabled(false);

        mMessage.addTextChangedListener(this);

        mButtonSend.setOnClickListener(this);
        mButtonLogout.setOnClickListener(this);

        mMessageAdapter = new MessageAdapter(this);

        ListView messages = findViewById(R.id.messages);

        messages.setAdapter(mMessageAdapter);

    }

    @Override
    public void onResume() {
        super.onResume();
        fetchMessages();
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
                Message message = new Message(0, mSender, mReceiver, mMessage.getText().toString());

                mMessageHelper.insertMessage(message);

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

    private void fetchMessages() {
        Message[] messages = mMessageHelper.getMessages(mSender.getId(), mReceiver.getId());
        if (messages != null) {
            for (Message message : messages) {
                mMessageAdapter.addMessage(message);
            }
        }
    }
}
