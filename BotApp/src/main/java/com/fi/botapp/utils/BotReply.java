package com.fi.botapp.utils;

import android.os.Parcel;
import android.os.Parcelable;

public class BotReply implements Parcelable{
    private String timestamp;
    private String speech;
    private String imageUrl;

    public BotReply(String timestamp, String speech, String imageUrl) {
        this.timestamp = timestamp;
        this.speech = speech;
        this.imageUrl = imageUrl;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getSpeech() {
        return speech;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    protected BotReply(Parcel in) {
        timestamp = in.readString();
        speech = in.readString();
        imageUrl = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(timestamp);
        dest.writeString(speech);
        dest.writeString(imageUrl);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<BotReply> CREATOR = new Creator<BotReply>() {
        @Override
        public BotReply createFromParcel(Parcel in) {
            return new BotReply(in);
        }

        @Override
        public BotReply[] newArray(int size) {
            return new BotReply[size];
        }
    };
}
