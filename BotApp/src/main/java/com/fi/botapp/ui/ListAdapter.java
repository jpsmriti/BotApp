package com.fi.botapp.ui;


import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fi.botapp.R;
import com.fi.botapp.utils.ChatMsg;
import com.fi.botapp.utils.DownloadImageTask;

import java.util.ArrayList;

public class ListAdapter extends ArrayAdapter<String> {
    private ArrayList<ChatMsg> data;
    private LayoutInflater mInflater;
    private TextView messageText;
    private ImageView messageImage;
    private ProgressBar imageLoader;
    private TextView messageTime;

    public ListAdapter(Context context, int layout, ArrayList<ChatMsg> dataList) {
        super(context, layout);
        data = dataList;
        mInflater = LayoutInflater.from(getContext());
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ChatMsg item = data.get(position);
        if (item.isQues()) {
            convertView = mInflater.inflate(R.layout.query, null);
        } else {
            convertView = mInflater.inflate(R.layout.reply, null);
        }
        messageText = (TextView) convertView.findViewById(R.id.text);
        messageImage = (ImageView) convertView.findViewById(R.id.image);
        imageLoader = (ProgressBar) convertView.findViewById(R.id.loader);
        messageTime = (TextView) convertView.findViewById(R.id.timestamp);

        String speech = item.getMessage().getSpeech();
        messageText.setText(!TextUtils.isEmpty(speech)?speech:"Sorry, please be more clear.");
        messageTime.setText(item.getMessage().getTimestamp());
        if (!item.isQues()) {
            new DownloadImageTask(messageImage, imageLoader,
                    item.getMessage().getImageUrl()).execute();
        }
        return convertView;
    }

}

