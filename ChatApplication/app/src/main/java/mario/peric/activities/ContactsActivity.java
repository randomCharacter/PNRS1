package mario.peric.activities;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import mario.peric.INotificationCallback;
import mario.peric.R;
import mario.peric.adapters.ContactAdapter;
import mario.peric.binders.NotificationBinder;
import mario.peric.helpers.HTTPHelper;
import mario.peric.models.Contact;
import mario.peric.services.NotificationService;
import mario.peric.utils.Preferences;

public class ContactsActivity extends AppCompatActivity implements View.OnClickListener, ServiceConnection {

    Button mButtonLogout, mButtonRefresh;
    HTTPHelper mHTTPHelper;
    Handler mHandler;
    String mLoggedUser;
    String mSessionID;
    ContactAdapter mContactAdapter;
    NotificationBinder mService = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        mHTTPHelper = new HTTPHelper();
        mHandler = new Handler();

        mButtonLogout = findViewById(R.id.button_log_out);
        mButtonRefresh = findViewById(R.id.button_refresh);

        mButtonLogout.setOnClickListener(this);
        mButtonRefresh.setOnClickListener(this);

        mContactAdapter = new ContactAdapter(this);

        SharedPreferences sharedPref = getSharedPreferences(Preferences.NAME, Context.MODE_PRIVATE);
        mSessionID = sharedPref.getString(Preferences.SESSION_ID, null);
        mLoggedUser = sharedPref.getString(Preferences.USER_LOGGED_IN, null);

        if (mSessionID == null) {
            Toast.makeText(this, R.string.unknown_error, Toast.LENGTH_LONG).show();
            Intent loginIntent = new Intent(getApplicationContext(), MainActivity.class);
            loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(loginIntent);
        }

        ListView contactList = findViewById(R.id.contacts);
        contactList.setAdapter(mContactAdapter);
        bindService(new Intent(ContactsActivity.this, NotificationService.class), this, Context.BIND_AUTO_CREATE);
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
                        }catch (IOException e) {
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
            case R.id.button_refresh:
                fetchContacts();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchContacts();
    }

    private void fetchContacts() {
        mContactAdapter.clear();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONArray jsonArray = mHTTPHelper.getJSONArrayFromURL(HTTPHelper.URL_CONTACTS, mSessionID);
                    if (jsonArray == null) {
                        Toast.makeText(ContactsActivity.this, R.string.unknown_error, Toast.LENGTH_LONG).show();
                        Intent loginIntent = new Intent(getApplicationContext(), MainActivity.class);
                        loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(loginIntent);
                    } else {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String username = jsonObject.getString(HTTPHelper.USERNAME);
                            Contact contact = new Contact(username);
                            if (!username.equals(mLoggedUser)) {
                                mContactAdapter.addContact(contact);
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    mHandler.post(new Runnable(){
                        public void run() {
                            mContactAdapter.notifyDataSetChanged();
                        }
                    });
                }
            }
        }).start();
    }

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        mService = (NotificationBinder) NotificationBinder.Stub.asInterface(iBinder);
        try {
            mService.setCallback(new NotificationCallback());
        } catch (RemoteException e) {
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {

    }

    private class NotificationCallback extends INotificationCallback.Stub {

        @Override
        public void onCallbackCall() throws RemoteException {

            final HTTPHelper mHTTPHelper = new HTTPHelper();
            final Handler handler = new Handler();

            final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), null)
                    .setSmallIcon(R.drawable.ic_notification)
                    .setContentTitle(getText(R.string.app_name))
                    .setContentText(getString(R.string.new_message))
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);

            final NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());


            new Thread(new Runnable() {
                public void run() {
                    try {
                        final boolean res = mHTTPHelper.getBooleanFromURL(HTTPHelper.URL_NOTIFICATION, mSessionID);

                        handler.post(new Runnable() {
                            public void run() {
                                if (res) {
                                    notificationManager.notify(2, mBuilder.build());
                                }
                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }
}
