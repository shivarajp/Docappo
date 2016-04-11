package com.ndtv.ndtvdoc.models;

import java.io.Serializable;

/**
 * Created by Shivam on 4/11/2016.
 */

public class ChatModel implements Serializable{

    private String mName;
    private String mMessage;


    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmMessage() {
        return mMessage;
    }

    public void setmMessage(String mMessage) {
        this.mMessage = mMessage;
    }


}
