package com.fi.botapp.ui;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.fi.botapp.R;
import com.fi.botapp.utils.ChatMsg;
import com.fi.botapp.utils.Logger;
import com.fi.botapp.utils.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ai.api.AIServiceException;
import ai.api.RequestExtras;
import ai.api.model.AIContext;
import ai.api.model.AIResponse;
import ai.api.model.Result;

public class ChatActivity extends BaseActivity {

    private ListView bubbleList;
    private ArrayList<ChatMsg> chatMessages = new ArrayList<>();
    private ListAdapter mListAdapter;
    private Button sendBtn;
    private EditText messageBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_chat);
        initApiService();

        bubbleList = (ListView) findViewById(R.id.bubbleList);
        messageBox = (EditText) findViewById(R.id.message);
        sendBtn = (Button) findViewById(R.id.send);
        Utils.hideKeyboard(ChatActivity.this, messageBox);

        mListAdapter = new ListAdapter(this, R.layout.bubblelist, chatMessages);
        bubbleList.setAdapter(mListAdapter);

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tempMsg = messageBox.getText().toString();
                if (Utils.checkForInternet(ChatActivity.this) &&
                        !TextUtils.isEmpty(tempMsg) &&
                        !tempMsg.equalsIgnoreCase(" ")) {
                    refreshList(tempMsg, true);
                    sendRequest(tempMsg);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == R.id.action_logout) {
            logoutUser();
            finish();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }


    private void refreshList(String newMsg, boolean isQuestion) {
        ChatMsg msgObject = new ChatMsg(newMsg, isQuestion);
        chatMessages.add(msgObject);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                messageBox.setText("");
                mListAdapter.notifyDataSetChanged();
            }
        });
    }

    private void sendRequest(final String query) {
        if (!TextUtils.isEmpty(query)) {
            final String contextString = String.valueOf(query);
            final List<AIContext> contexts = Collections.singletonList(new AIContext(contextString));
            final RequestExtras requestExtras = new RequestExtras(contexts, null);

            AsyncTask botTask = new AsyncTask() {
                @Override
                protected Object doInBackground(Object[] objects) {
                    try {
                        aiService.resume();
                        processQuery(aiService.textRequest(query, requestExtras));
                    } catch (AIServiceException e) {
                        e.printStackTrace();
                    }
                    return null;
                }
            };
            botTask.execute();
        } else {
            Logger.D("Please ask a question first !");
        }
    }

    private void processQuery(AIResponse aiResponse) {
        final Result result = aiResponse.getResult();
        final String speech = result.getFulfillment().getSpeech();
        Logger.D("result received " + speech);
        onResultReturned(speech);
    }

    @Override
    void onResultReturned(String result) {
        refreshList(result, false);
    }
}
