package com.fi.botapp.ui;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.fi.botapp.R;
import com.fi.botapp.utils.Logger;
import com.fi.botapp.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ai.api.AIServiceException;
import ai.api.GsonFactory;
import ai.api.RequestExtras;
import ai.api.model.AIContext;
import ai.api.model.AIResponse;
import ai.api.model.Result;

public class ChatActivity extends BaseActivity {

    ListView bubbleList;
    ArrayList<String> chatMessages = new ArrayList<>();
    ListAdapter mListAdapter;
    Button sendBtn;
    EditText messageBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_chat);
        initService();

        bubbleList = (ListView)findViewById(R.id.bubbleList);
        messageBox = (EditText) findViewById(R.id.message);
        sendBtn = (Button) findViewById(R.id.send);


        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        mListAdapter = new ListAdapter(this, R.layout.bubblelist, chatMessages);
        bubbleList.setAdapter(mListAdapter);


        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.showShortToast(ChatActivity.this, "processing....");
                String tempMsg = messageBox.getText().toString();
                if (!TextUtils.isEmpty(tempMsg) && !tempMsg.equalsIgnoreCase(" ")) {
                    refreshList(tempMsg);
                    sendRequest(tempMsg);
                }
            }
        });
    }

    private void refreshList(String newMsg){
        chatMessages.add(newMsg);
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
                        Logger.D("botTask : dib");
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
        Logger.D("processQuery " + aiResponse.toString());
        try {
            final String jsonResponse = GsonFactory.getGson().toJson(aiResponse);
            Logger.D("jsonResponse " + jsonResponse);
            final JSONObject jsonObject = new JSONObject(jsonResponse);
            final JSONObject jsonObjectResult = jsonObject.getJSONObject("result");

            Logger.D("jsonObject " + jsonObjectResult.toString());
//            Logger.D("telegram " + jsonObject.getString("result"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final Result result = aiResponse.getResult();
        final String speech = result.getFulfillment().getSpeech();
//        final String telegramSpeech = result.getFulfillment().
        Logger.D("result received " + speech);
        onResultReturned(speech);
    }

    @Override
    void onResultReturned(String result) {
        refreshList(result);
    }
}
