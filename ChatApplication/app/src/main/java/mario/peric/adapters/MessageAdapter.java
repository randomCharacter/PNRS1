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

import java.util.ArrayList;

import mario.peric.R;
import mario.peric.models.Message;
import mario.peric.utils.Preferences;

public class MessageAdapter extends BaseAdapter implements View.OnLongClickListener {

    private Context mContext;
    private ArrayList<Message> mMessages;

    public MessageAdapter(Context context) {
        mContext = context;
        mMessages = new ArrayList<>();
    }

    public void addMessage(Message message) {
        mMessages.add(message);
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
                Message message = mMessages.get(i);
                mMessages.remove(i);
                notifyDataSetChanged();
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
