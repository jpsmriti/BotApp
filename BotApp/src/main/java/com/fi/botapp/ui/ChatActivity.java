package com.fi.botapp.ui;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.fi.botapp.R;
import com.fi.botapp.network.VolleyCallBack;
import com.fi.botapp.network.requests.GetRequest;
import com.fi.botapp.utils.BotReply;
import com.fi.botapp.utils.ChatMsg;
import com.fi.botapp.utils.Constants;
import com.fi.botapp.utils.Logger;
import com.fi.botapp.utils.ParserTask;
import com.fi.botapp.utils.Utils;

import java.util.ArrayList;
import java.util.Locale;

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
        Utils.hideKeyboard(ChatActivity.this, messageBox);
        mListAdapter = new ListAdapter(this, 0, chatMessages);
        bubbleList.setAdapter(mListAdapter);
    }

    @OnClick(R.id.send)
    protected void sendMessage(View v) {
        String tempMsg = messageBox.getText().toString();
        if (Utils.checkForInternet(ChatActivity.this) &&
                !TextUtils.isEmpty(tempMsg) &&
                !tempMsg.equalsIgnoreCase(" ")) {

            BotReply message = new BotReply(String.valueOf(System.currentTimeMillis()), tempMsg, "");
            refreshList(message, true);
            sendRequest(tempMsg);
        }
    }

    @OnClick(R.id.speak)
    protected void listenMessage(View v) {
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
                    BotReply message = new BotReply(String.valueOf(System.currentTimeMillis()), speech, "");
                    refreshList(message, true);
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

    private void refreshList(BotReply reply, boolean isQuestion) {
        ChatMsg msgObject = new ChatMsg(reply, isQuestion);
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
            String queryInput = query.replaceAll(" ", "+");
            new GetRequest().getJson(queryInput, new VolleyCallBack<String>() {
                @Override
                public void onSuccess(String result) {
                    Logger.D("onSuccess !!");
                    new ParserTask(result, ChatActivity.this).execute();
                }

                @Override
                public void onFailure(Exception e) {
                    Logger.D("Failure");
                }
            });
        } else {
            Logger.D("Please ask a question first !");
        }
    }

    public void updateScreen(BotReply parsedData) {
        Logger.D("updateScreen");
        if (parsedData != null) {
            Logger.D("getTimestamp " + parsedData.getTimestamp());
            Logger.D("getSpeech " + parsedData.getSpeech());
            Logger.D("getImageUrl " + parsedData.getImageUrl());

            Logger.D("split Date " + parsedData.getTimestamp().split("T")[0]);
            Logger.D("split Time " + parsedData.getTimestamp().split("T")[1]);
            //Logger.D("formatted " + Utils.getDateTime(Long.parseLong(parsedData.getTimestamp())));

            refreshList(parsedData, false);
        }
    }
}
