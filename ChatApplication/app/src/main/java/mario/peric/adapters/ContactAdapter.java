package mario.peric.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Handler;

import mario.peric.R;
import mario.peric.activities.MessageActivity;
import mario.peric.helpers.HTTPHelper;
import mario.peric.models.Contact;
import mario.peric.utils.Preferences;

public class ContactAdapter extends BaseAdapter implements View.OnClickListener, View.OnLongClickListener {

    private Context mContext;
    private ArrayList<Contact> mContacts;
    private HTTPHelper mHttpHelper;
    private String mSessionID;
    private Handler mHandler;

    public ContactAdapter(Context context) {
        mContext = context;
        mContacts = new ArrayList<>();
        mHttpHelper = new HTTPHelper();
        SharedPreferences sharedPref = mContext.getSharedPreferences(Preferences.NAME, Context.MODE_PRIVATE);
        mSessionID = sharedPref.getString(Preferences.SESSION_ID, null);
    }

    public void addContact(Contact contact) {
        mContacts.add(contact);
    }

    public void clear() {
        mContacts.clear();
    }

    @Override
    public int getCount() {
        return mContacts.size();
    }

    @Override
    public Object getItem(int i) {
        Object rv = null;
        try {
            rv = mContacts.get(i);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }

        return rv;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.view_contact, null);
            ContactHolder holder = new ContactHolder();
            holder.buttonSend = view.findViewById(R.id.button_message);
            holder.firstLetter = view.findViewById(R.id.first_letter);
            holder.fullName = view.findViewById(R.id.full_name);
            holder.buttonSend.setOnClickListener(this);
            view.setTag(holder);

            Random random = new Random();
            holder.firstLetter.setBackgroundColor(Color.argb(255, random.nextInt(210),
                    random.nextInt(210), random.nextInt(210)));
        }

        Contact contact = (Contact) getItem(i);
        ContactHolder holder = (ContactHolder) view.getTag();
        holder.firstLetter.setText(contact.getUsername().substring(0, 1).toUpperCase());
        holder.fullName.setText(contact.getUsername());
        holder.buttonSend.setTag(i);
        holder.fullName.setTag(i);

        holder.fullName.setOnLongClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_message:
                int i = Integer.parseInt(view.getTag().toString());
                Contact clicked = mContacts.get(i);

                if (view.getId() == R.id.button_message) {
                    Intent intent = new Intent(mContext.getApplicationContext(), MessageActivity.class);
                    intent.putExtra(Contact.ID, clicked.getUsername());
                    mContext.startActivity(intent);
                }
                break;
        }
    }

    @Override
    public boolean onLongClick(View view) {
        switch (view.getId()) {
            case R.id.full_name:
                int i = Integer.parseInt(view.getTag().toString());
                final Contact contact = mContacts.get(i);
                mContacts.remove(i);
                notifyDataSetChanged();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject jsonObject = new JSONObject();
                            final HTTPHelper.HTTPResponse res = mHttpHelper.deleteJSONObjectFromURL(HTTPHelper.URL_CONTACT + contact.getUsername(), jsonObject, mSessionID);
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                Toast.makeText(mContext, "Contact removed", Toast.LENGTH_LONG).show();
                return true;
            default:
                return false;
        }
    }

    private class ContactHolder {
        TextView firstLetter = null;
        TextView fullName = null;
        ImageView buttonSend = null;
    }
}
