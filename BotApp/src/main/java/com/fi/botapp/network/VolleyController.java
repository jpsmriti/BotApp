package com.fi.botapp.network;


import android.support.annotation.Nullable;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;
import com.fi.botapp.BotApp;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;


public class VolleyController {

    private final String TAG = VolleyController.class.getSimpleName();
    public static final int DEFAULT_TIMEOUT_POLICY = 30000;
    public static final int DEFAULT_RETRY_ATTEMPTS = 1;
    public static final float DEFAULT_NETWORK_BACKOFF_MULTIPLIER = 1f;

    public static final String NO_INTERNET_TAG = "Please check your internet connection.";

    public static final String URL_BASE ="https://api.api.ai/v1/";

    public VolleyController() {
    }

    protected <T> void getRequest(String urlEndpoint, Class<T> clazz, final @Nullable HashMap<String, String> headers, boolean isOverrideUrl, @Nullable final VolleyCallBack<T> callBack) {
        if (BotApp.getInstance().isNetworkAvailable()) {
            final RequestQueue mRequestQueue = VolleySingleton.getRequestQueue();
            String url;
            if (isOverrideUrl) {
                url = urlEndpoint;
            } else {
                url = URL_BASE + urlEndpoint;
            }
            Log.e(TAG, "URl-" + url);
            final Get<T> mReq = new Get<T>(url, clazz, headers, new Response.Listener<T>() {
                @Override
                public void onResponse(T response) {
                    if (callBack != null)
                        callBack.onSuccess(response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (callBack != null)
                        callBack.onFailure(error);
                }
            });
            mReq.setRetryPolicy(new DefaultRetryPolicy(
                    DEFAULT_TIMEOUT_POLICY,
                    DEFAULT_RETRY_ATTEMPTS,
                    DEFAULT_NETWORK_BACKOFF_MULTIPLIER));
            mRequestQueue.add(mReq);
        } else {
            if (callBack != null)
                callBack.onFailure(new Exception(NO_INTERNET_TAG));
        }


    }

    public <T> void postRequest(String urlEndpoint, Class<T> clazz, @Nullable final HashMap<String, String> headers, JSONObject jsonObject, @Nullable final HashMap<String,
            String> params, boolean isOverrideUrl, @Nullable final VolleyCallBack<T> callBack) {

        if (BotApp.getInstance().isNetworkAvailable()) {
            RequestQueue mRequestQueue = VolleySingleton.getRequestQueue();
            String url;

            if (isOverrideUrl) {
                url = urlEndpoint;
            } else {
                url = URL_BASE + urlEndpoint;
            }
            Log.e("@@@", "URl-" + url);
            Post<T> postRequest = new Post<>(url, headers, jsonObject, params, clazz,
                    new Response.Listener<T>() {
                        @Override
                        public void onResponse(T response) {
                            if (callBack != null) {
                                callBack.onSuccess(response);
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            if (callBack != null) {
                                callBack.onFailure(error);
                            }
                        }
                    });
            postRequest.setRetryPolicy(new DefaultRetryPolicy(
                    DEFAULT_TIMEOUT_POLICY,
                    DEFAULT_RETRY_ATTEMPTS,
                    DEFAULT_NETWORK_BACKOFF_MULTIPLIER)
            );
            mRequestQueue.add(postRequest);
        } else {
            if (callBack != null)
                callBack.onFailure(new Exception(NO_INTERNET_TAG));
        }
    }

    private class Get<T> extends Request<T> {

        private Gson gson;
        private Class<T> clazz;
        private HashMap<String, String> headers;
        private Response.Listener<T> listener;


        public Get(String url, Class<T> clazz, HashMap<String, String> headers, Response.Listener<T> listener, Response.ErrorListener errorListener) {
            super(Method.GET, url, errorListener);
            this.gson = new Gson();
            this.clazz = clazz;
            this.listener = listener;
            this.headers = headers;
        }

        @Override
        public Map<String, String> getHeaders() throws AuthFailureError {
            HashMap<String, String> headers = new HashMap<>();
            if (this.headers != null) {
                headers = this.headers;
            }
            return headers;
        }

        @Override
        protected Response<T> parseNetworkResponse(NetworkResponse response) {
            try {
                final String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                if (clazz == String.class)
                    return (Response<T>) Response.success(
                            json,
                            HttpHeaderParser.parseCacheHeaders(response)
                    );

                else
                    return (Response<T>) Response.success(
                            gson.fromJson(json, clazz),
                            HttpHeaderParser.parseCacheHeaders(response)
                    );
            } catch (UnsupportedEncodingException e) {
                return Response.error(new ParseError(e));
            } catch (JsonSyntaxException e) {
                return Response.error(new ParseError(e));
            }
        }

        @Override
        protected void deliverResponse(T response) {
            listener.onResponse(response);
        }
    }

    private class Post<T> extends JsonRequest<T> {

        private Gson gson;
        private Class<T> clazz;
        private Response.Listener<T> listener;
        private HashMap<String, String> headers, params;
        JsonObject jsonObject = new JsonObject();


        public Post(String url, HashMap<String, String> headers, JSONObject jsonObject, HashMap<String, String> params, Class<T> clazz, Response.Listener<T> listener, Response.ErrorListener errorListener) {
            super(Method.POST, url, jsonObject.toString(), listener, errorListener);
            this.gson = new Gson();
            this.clazz = clazz;
            this.listener = listener;
            this.headers = headers;
            this.params = params;
        }

        @Override
        protected Map<String, String> getParams() throws AuthFailureError {
            return params != null ? params : super.getParams();
        }

        @Override
        public Map<String, String> getHeaders() throws AuthFailureError {
            HashMap<String, String> headers1 = new HashMap<>();
            if (headers != null) {
                headers1 = headers;
            }
            return headers1;
        }

        @Override
        protected Response<T> parseNetworkResponse(NetworkResponse response) {
            try {
                String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                if (clazz == String.class)
                    return (Response<T>) Response.success(
                            json,
                            HttpHeaderParser.parseCacheHeaders(response)
                    );

                else
                    return (Response<T>) Response.success
                            (
                                    gson.fromJson(json, clazz),
                                    HttpHeaderParser.parseCacheHeaders(response)
                            );

            } catch (UnsupportedEncodingException e) {
                return Response.error(new ParseError(e));
            } catch (JsonSyntaxException e) {
                return Response.error(new ParseError(e));
            }
        }

        @Override
        protected void deliverResponse(T response) {
            listener.onResponse(response);

        }
    }

}
