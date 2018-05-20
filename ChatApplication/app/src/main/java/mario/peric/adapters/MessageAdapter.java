package mario.peric.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import mario.peric.R;
import mario.peric.helpers.HTTPHelper;
import mario.peric.models.Message;
import mario.peric.utils.Preferences;

public class MessageAdapter extends BaseAdapter implements View.OnLongClickListener {

    private Context mContext;
    private ArrayList<Message> mMessages;
    private HTTPHelper mHTTPHelper;
    private String mSessionID;
    private String mSender;

    public MessageAdapter(Context context, String sender) {
        mContext = context;
        mMessages = new ArrayList<>();
        mHTTPHelper = new HTTPHelper();
        SharedPreferences sharedPref = mContext.getSharedPreferences(Preferences.NAME, Context.MODE_PRIVATE);
        mSessionID = sharedPref.getString(Preferences.SESSION_ID, null);
        mSender = sender;
    }

    public void addMessage(Message message) {
        mMessages.add(message);
    }

    public void clear() {
        mMessages.clear();
    }

    @Override
    public int getCount() {
        return mMessages.size();
    }

    @Override
    public Object getItem(int i) {
        Object rv = null;
        try {
            rv = mMessages.get(i);
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
            view = inflater.inflate(R.layout.view_message, null);
            MessageHolder holder = new MessageHolder();
            holder.message = view.findViewById(R.id.message);
            holder.messageContainer = view.findViewById(R.id.message_container);

            view.setTag(holder);
        }

        MessageHolder holder = (MessageHolder) view.getTag();
        Message message = (Message) getItem(i);

        SharedPreferences sharedPref = mContext.getSharedPreferences(Preferences.NAME, Context.MODE_PRIVATE);

        if (!message.getReceived()) {
            holder.message.setBackground(view.getResources().getDrawable(R.drawable.message_sent));
            holder.messageContainer.setGravity(Gravity.END);
        } else {
            holder.message.setBackground(view.getResources().getDrawable(R.drawable.message_received));
            holder.messageContainer.setGravity(Gravity.START);
        }

        holder.message.setTag(i);
        holder.message.setOnLongClickListener(this);
        holder.message.setText(message.getMessage());

        return view;
    }

    @Override
    public boolean onLongClick(View view) {
        switch (view.getId()) {
            case R.id.message:
                int i = Integer.parseInt(view.getTag().toString());
                final Message message = mMessages.get(i);
                mMessages.remove(i);
                notifyDataSetChanged();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put(HTTPHelper.DATA, message.getMessage());
                            final HTTPHelper.HTTPResponse res = mHTTPHelper.deleteJSONObjectFromURL(HTTPHelper.URL_MESSAGES + mSender, jsonObject, mSessionID);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                return true;
            default:
                return false;
        }
    }

    private class MessageHolder {
        public TextView message = null;
        public RelativeLayout messageContainer = null;
    }
}
