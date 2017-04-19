package com.fi.botapp.utils;

public class ChatMsg {

    private BotReply message;
    private boolean isQues;

    public ChatMsg(BotReply msg, boolean isQues) {
        this.message = msg;
        this.isQues = isQues;
    }

    public BotReply getMessage() {
        return message;
    }

    public void setMessage(BotReply message) {
        this.message = message;
    }

    public boolean isQues() {
        return isQues;
    }

    public void setQues(boolean ques) {
        isQues = ques;
    }
}
