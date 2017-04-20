package com.fi.botapp.utils;

import android.app.Activity;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.fi.botapp.ui.ChatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ParserTask extends AsyncTask<Void, Void, Void> {

    private String jsonData;
    private BotReply parsedData;
    private Activity screen;

    public ParserTask(String jsonInput, Activity activity) {
        this.jsonData = jsonInput;
        this.screen = activity;
    }

    @Override
    protected Void doInBackground(Void... params) {
        JSONObject root = null;
        JSONObject resultObject = null;
        JSONObject fulfillmentObject = null;
        JSONArray messagesArray = null;
        String timestamp = null;
        String speechString = null;
        String imageUrl = null;

        try {
            if (this.jsonData != null){
                Logger.D("root built");
                root = new JSONObject(this.jsonData);
            }
            if (root != null && root.has(Constants.TAGS.TIMESTAMP)) {
                 String temp = root.getString(Constants.TAGS.TIMESTAMP);
                if (!TextUtils.isEmpty(temp)){
                    timestamp = temp;
                }
            }
            if (root != null && root.has(Constants.TAGS.RESULT)) {
                resultObject = root.getJSONObject(Constants.TAGS.RESULT);
            }
            if (resultObject != null && resultObject.has(Constants.TAGS.FULFILLMENT)) {
                 fulfillmentObject = resultObject.getJSONObject(Constants.TAGS.FULFILLMENT);
            }
            if (fulfillmentObject != null && fulfillmentObject.has(Constants.TAGS.SPEECH)) {
                speechString = fulfillmentObject.getString(Constants.TAGS.SPEECH);
            }
            if (fulfillmentObject != null && fulfillmentObject.has(Constants.TAGS.MESSAGES)) {
                 messagesArray = fulfillmentObject.getJSONArray(Constants.TAGS.MESSAGES);
            }
            if (messagesArray != null) {
                for (int i = 0; i < messagesArray.length(); i++) {
                    JSONObject arrayObject = messagesArray.getJSONObject(i);
                    try {
                        if (arrayObject != null && arrayObject.has(Constants.TAGS.IMAGE_URL)) {
                            imageUrl = arrayObject.getString(Constants.TAGS.IMAGE_URL);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            this.parsedData = new BotReply(Utils.getTime(), speechString, imageUrl);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        if (this.screen instanceof ChatActivity) {
            ((ChatActivity) screen).updateScreen(this.parsedData);
        }
    }
}
