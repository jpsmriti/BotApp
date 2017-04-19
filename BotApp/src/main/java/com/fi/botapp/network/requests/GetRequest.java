package com.fi.botapp.network.requests;


import com.fi.botapp.network.VolleyCallBack;
import com.fi.botapp.network.VolleyController;
import com.fi.botapp.utils.Constants;

import java.util.HashMap;

public class GetRequest extends VolleyController {

    public GetRequest() {
    }


    public void getJson(final String searchText, final VolleyCallBack<String> callBack) {

        try {
            String urlEndpoint = "https://api.api.ai/v1/query?v=20150910&query="+ searchText+"&timezone=Europe/Paris&lang=en&contexts=weather&contexts=europe&latitude=37.459157&longitude=-122.17926&sessionId=1234567890";

            urlEndpoint = urlEndpoint.replace(" ", "%20");

            final HashMap<String, String> headers = new HashMap<String, String>();
            headers.put("Content-Type", "application/json");
            headers.put("Authorization", "Bearer " + Constants.APP_ID);

            getRequest(urlEndpoint, String.class, headers, true, new VolleyCallBack<String>() {
                @Override
                public void onSuccess(String jsonString) {
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

