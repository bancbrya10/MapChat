package edu.temple.chatapplication;

import java.util.ArrayList;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class ChatAdapter extends BaseAdapter {
    private Activity activityContext;
    private final ArrayList<Message> messages;

    public ChatAdapter(Activity activityContext, ArrayList<Message> messages) {
        this.messages = messages;
        this.activityContext = activityContext;
    }

    @Override
    public int getCount() {
        return (messages != null ? messages.size() : 0);
    }

    @Override
    public Object getItem(int position) {
        return (messages != null ? messages.get(position) : null);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }
}
