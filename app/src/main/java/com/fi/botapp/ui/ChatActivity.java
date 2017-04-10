package com.fi.botapp.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.fi.botapp.R;
import com.fi.botapp.utils.Utils;

import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity {

    ListView bubbleList;
    ArrayList<String> chatMessages = new ArrayList<>();
    ListAdapter mListAdapter;
    Button sendBtn;
    EditText messageBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_chat);

        bubbleList = (ListView)findViewById(R.id.bubbleList);
        messageBox = (EditText) findViewById(R.id.message);
        sendBtn = (Button) findViewById(R.id.send);

        chatMessages.add("Hi");
        chatMessages.add("Hello");
        chatMessages.add("How are you?");
        chatMessages.add("M good. What about you?");
        chatMessages.add("Good :)");
        chatMessages.add("See you. Bye :)");
        chatMessages.add("BuBye..");
        mListAdapter = new ListAdapter(this, R.layout.bubblelist, chatMessages);
        bubbleList.setAdapter(mListAdapter);


        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.showShortToast(ChatActivity.this, "sending....");
                String tempMsg = messageBox.getText().toString();
                if (!TextUtils.isEmpty(tempMsg)) {
                    chatMessages.add(tempMsg);
                    messageBox.setText("");
                    mListAdapter.notifyDataSetChanged();
                }
            }
        });
    }

}
