package com.fi.botapp.ui;


import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fi.botapp.R;
import com.fi.botapp.utils.ChatMsg;

import java.util.ArrayList;

public class ListAdapter extends ArrayAdapter<String> {
    private ArrayList<ChatMsg> data;
    private int layoutRes;

    public ListAdapter(Context context, int layout, ArrayList<ChatMsg> dataList) {
        super(context, layout);
        data = dataList;
        layoutRes = layout;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ChatMsg item = data.get(position);
        View inflater = LayoutInflater.from(getContext()).inflate(layoutRes, parent, false);
        TextView text = (TextView) inflater.findViewById(R.id.text1);
        text.setText(item.getMsg());
        if (!item.isQues()) {
            text.setBackground(getContext().getResources().getDrawable(R.drawable.round_green_bg));
            text.setTextColor(getContext().getResources().getColor(R.color.buttonHolder));
            text.setGravity(Gravity.CENTER);
        } else {
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            text.setLayoutParams(lp);
            text.setBackground(getContext().getResources().getDrawable(R.drawable.round_ques_bg));
            text.setTextColor(getContext().getResources().getColor(R.color.buttonHolder));
        }
        return inflater;
    }
}

