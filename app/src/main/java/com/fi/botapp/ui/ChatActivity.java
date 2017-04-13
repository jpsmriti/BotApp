package com.fi.botapp.ui;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.fi.botapp.R;
import com.fi.botapp.utils.ChatMsg;
import com.fi.botapp.utils.Constants;
import com.fi.botapp.utils.Logger;
import com.fi.botapp.utils.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import ai.api.AIServiceException;
import ai.api.RequestExtras;
import ai.api.model.AIContext;
import ai.api.model.AIResponse;
import ai.api.model.Result;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChatActivity extends BaseActivity {

    @Bind(R.id.bubbleList)
    protected ListView bubbleList;
    @Bind(R.id.message)
    protected EditText messageBox;

    private ArrayList<ChatMsg> chatMessages = new ArrayList<>();
    private ListAdapter mListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_chat);
        ButterKnife.bind(this);
        initApiService();

        Utils.hideKeyboard(ChatActivity.this, messageBox);

        mListAdapter = new ListAdapter(this, R.layout.bubblelist, chatMessages);
        bubbleList.setAdapter(mListAdapter);
    }

    @OnClick(R.id.send)
    protected void sendMessage(View v){
        String tempMsg = messageBox.getText().toString();
        if (Utils.checkForInternet(ChatActivity.this) &&
                !TextUtils.isEmpty(tempMsg) &&
                !tempMsg.equalsIgnoreCase(" ")) {
            refreshList(tempMsg, true);
            sendRequest(tempMsg);
        }
    }

    @OnClick(R.id.speak)
    protected void listenMessage(View v){
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, Constants.SPEECH_RQST);
        } catch (ActivityNotFoundException a) {
            Utils.showShortToast(ChatActivity.this, getString(R.string.speech_not_supported));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case Constants.SPEECH_RQST: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    String speech = result.get(0);
                    Utils.showShortToast(ChatActivity.this, speech);
                    refreshList(speech, true);
                    sendRequest(speech);
                }
                break;
            }
        }
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
