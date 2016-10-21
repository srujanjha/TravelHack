package com.codetroopers.makemytrip;

/**
 * Created by Srujan Jha on 9/24/2016.
 */
public class ChatMessage {

    private String content;
    private boolean isMine;

    public ChatMessage(String content, boolean isMine) {
        this.content = content;
        this.isMine = isMine;
    }

    public String getContent() {
        return content;
    }

    public boolean isMine() {
        return isMine;
    }
}