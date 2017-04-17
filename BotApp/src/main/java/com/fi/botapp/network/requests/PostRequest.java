package com.fi.botapp.network.requests;


import com.fi.botapp.network.VolleyCallBack;
import com.fi.botapp.network.VolleyController;
import com.fi.botapp.utils.Constants;

import org.json.JSONObject;

import java.util.HashMap;

public class PostRequest extends VolleyController {

    public PostRequest() {
    }

    public void getJson(final VolleyCallBack<String> callBack) {

        try {
            String urlEndpoint = "https://api.api.ai/v1/query?v=20150910";
            urlEndpoint = urlEndpoint.replace(" ", "%20");

            HashMap<String, String> headers = new HashMap<String, String>();
//            String creds = String.format("%s:%s", NetworkCommons.AUTH_CLIENT_ID, NetworkCommons.AUTH_CLIENT_SECRET);
//            String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.NO_WRAP);
            headers.put("Authorization", "Bearer "+ Constants.APP_ID);
            headers.put("Content-Type", "application/json");

            postRequest(urlEndpoint, String.class, headers, new JSONObject(), null, true, new VolleyCallBack<String>() {
                @Override
                public void onSuccess(String jsonString) {
//                    AuthTokenModel model= NetworkCommons.parseJsonString(jsonString,AuthTokenModel.class);
//                    NetworkPrefs.getInstance().setAccessToken(model.getAccessToken());
                    callBack.onSuccess(jsonString);
                }

                @Override
                public void onFailure(Exception e) {
                    callBack.onFailure(e);
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
