package com.fi.botapp.network;

 public interface VolleyCallBack<StoreOut> {

    void onSuccess(StoreOut storeOut);
    void onFailure(Exception e);
}
