package com.fi.botapp.utils;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class MyFulfillment implements Serializable {
    @SerializedName("speech")
    private String speech;

    public MyFulfillment() {
    }

    public String getSpeech() {
        return this.speech;
    }

    public void setSpeech(String speech) {
        this.speech = speech;
    }
}
