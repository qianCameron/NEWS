package com.example.NEWS.model;

import java.util.Date;

public class Message {
    private int id;
    private int fromId;
    private int toId;
    private int hasRead;
    private String  content;
    private String  conversationId;
    private Date createdDate;

    public void setId(int id) {
        this.id = id;
    }

    public void setFromId(int fromId) {
        this.fromId = fromId;
    }

    public void setToId(int toId) {
        this.toId = toId;
    }

    public void setHasRead(int hasRead) {
        this.hasRead = hasRead;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public int getId() {
        return id;
    }

    public int getFromId() {
        return fromId;
    }

    public int getToId() {
        return toId;
    }

    public int getHasRead() {
        return hasRead;
    }

    public String getContent() {
        return content;
    }

    public String getConversationId() {
        return conversationId;
    }

    public Date getCreatedDate() {
        return createdDate;
    }
}
