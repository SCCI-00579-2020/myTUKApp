package com.example.tuktradecircle.models;


//represents a chat message structure
public class Message {
    private String content;
    private String senderName;
    private long timestamp;

    public Message() {
        // Default constructor required for Firebase
    }
    public Message(String content, String senderName, long timestamp) {
        this.content = content;
        this.senderName = senderName;
        this.timestamp = timestamp;
    }

    public String getContent() {
        return content;
    }

    public String getSenderName() {
        return senderName;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
