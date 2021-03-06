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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import mario.peric.R;
import mario.peric.adapters.MessageAdapter;
import mario.peric.helpers.HTTPHelper;
import mario.peric.models.Contact;
import mario.peric.models.Message;
import mario.peric.utils.Encryption;
import mario.peric.utils.Preferences;

public class MessageActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher {

    Button mButtonLogout, mButtonSend, mButtonRefresh;
    EditText mMessage;
    MessageAdapter mMessageAdapter;
    TextView mContactName;
    String mSender;
    HTTPHelper mHTTPHelper;
    String mSessionID;
    Handler mHandler;
    Encryption mEncryption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        mEncryption = new Encryption();

        mHTTPHelper = new HTTPHelper();
        mHandler = new Handler();

        mButtonLogout = findViewById(R.id.button_log_out);
        mButtonSend = findViewById(R.id.button_send);
        mButtonRefresh = findViewById(R.id.button_refresh);
        mMessage = findViewById(R.id.message_text);
        mContactName = findViewById(R.id.contact_name);

        mSender = getIntent().getStringExtra(Contact.ID);

        mContactName.setText(mSender);

        mButtonSend.setEnabled(false);

        mMessage.addTextChangedListener(this);

        mButtonSend.setOnClickListener(this);
        mButtonLogout.setOnClickListener(this);
        mButtonRefresh.setOnClickListener(this);

        mMessageAdapter = new MessageAdapter(this, mSender);

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
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            final HTTPHelper.HTTPResponse res = mHTTPHelper.postJSONObjectFromURL(HTTPHelper.URL_LOGOUT, new JSONObject(), mSessionID);
                            if (res.code == HTTPHelper.CODE_SUCCESS) {
                                mHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getApplicationContext(), R.string.logged_out,
                                                Toast.LENGTH_LONG).show();
                                    }
                                });
                            } else {
                                mHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getApplicationContext(), getString(R.string.error) + " " +
                                                res.code + ": " +res.message, Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

                Intent logoutIntent = new Intent(getApplicationContext(), MainActivity.class);
                logoutIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(logoutIntent);
                break;
            case R.id.button_send:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put(HTTPHelper.RECEIVER, mSender);

                            jsonObject.put(HTTPHelper.DATA, mEncryption.encrypt(mMessage.getText().toString(), mEncryption.KEY));
                            final HTTPHelper.HTTPResponse response = mHTTPHelper.postJSONObjectFromURL(HTTPHelper.URL_MESSAGE_SEND, jsonObject, mSessionID);

                            if (response.code != HTTPHelper.CODE_SUCCESS) {
                                mHandler.post(new Runnable() {
                                    public void run() {
                                        Toast.makeText(getApplicationContext(), R.string.unable_to_send_message + "\n" +
                                                response.message, Toast.LENGTH_LONG).show();
                                    }
                                });
                            } else {
                                mHandler.post(new Runnable() {
                                    public void run() {
                                        mMessageAdapter.addMessage(new Message(mMessage.getText().toString(), false));
                                        mMessageAdapter.notifyDataSetChanged();
                                        mMessage.setText("");
                                        mButtonSend.setEnabled(false);
                                        Toast.makeText(getApplicationContext(), R.string.message_sent, Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } finally {
                            mHandler.post(new Runnable(){
                                public void run() {
                                    mMessageAdapter.notifyDataSetChanged();
                                }
                            });
                        }
                    }
                }).start();
                break;
            case R.id.button_refresh:
                fetchMessages();
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
        mMessageAdapter.clear();
        SharedPreferences sharedPref = getSharedPreferences(Preferences.NAME, Context.MODE_PRIVATE);
        mSessionID = sharedPref.getString(Preferences.SESSION_ID, null);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONArray jsonArray = mHTTPHelper.getJSONArrayFromURL(HTTPHelper.URL_MESSAGES +
                            mSender, mSessionID);
                    if (jsonArray == null) {
                        Toast.makeText(MessageActivity.this, R.string.unknown_error, Toast.LENGTH_LONG).show();
                        Intent loginIntent = new Intent(getApplicationContext(), MainActivity.class);
                        loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(loginIntent);
                    } else {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String sender = jsonObject.getString(HTTPHelper.SENDER);
                            String data = mEncryption.decrypt(jsonObject.getString(HTTPHelper.DATA), Encryption.KEY);
                            Message message = new Message(data, sender.compareTo(mSender) == 0);
                            mMessageAdapter.addMessage(message);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    mHandler.post(new Runnable(){
                        public void run() {
                            mMessageAdapter.notifyDataSetChanged();
                        }
                    });
                }
            }
        }).start();
    }
}
