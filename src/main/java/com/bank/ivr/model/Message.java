package com.bank.ivr.model;

public class Message {
    private String contentType;
    private String content;

    public static final String CONTENT_TYPE_PLAIN_TEXT = "PlainText";
    public static final String CONTENT_TYPE_SSML = "SSML";

    public Message() { }

    public Message(String contentType, String content) {
        this.contentType = contentType;
        this.content = content;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Message{" +
                "contentType='" + contentType + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
