package mario.peric.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

import mario.peric.R;
import mario.peric.activities.MessageActivity;
import mario.peric.models.Contact;

public class ContactAdapter extends BaseAdapter implements View.OnClickListener {

    private Context mContext;
    private ArrayList<Contact> mContacts;

    public ContactAdapter(Context context) {
        mContext = context;
        mContacts = new ArrayList<>();
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

    private class ContactHolder {
        TextView firstLetter = null;
        TextView fullName = null;
        ImageView buttonSend = null;
    }
}
