package com.fi.botapp.utils;

public class ChatMsg {

    private String msg;
    private boolean isQues;

    public ChatMsg(String msg, boolean isQues) {
        this.msg = msg;
        this.isQues = isQues;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isQues() {
        return isQues;
    }

    public void setIsQuestion(boolean isQuestion) {
        isQues = isQuestion;
    }
}
