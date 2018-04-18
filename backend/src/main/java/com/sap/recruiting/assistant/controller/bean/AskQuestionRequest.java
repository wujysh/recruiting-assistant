package com.sap.recruiting.assistant.controller.bean;

/**
 * Created by Jiaye Wu on 18-4-16.
 */
public class AskQuestionRequest {

    private String wxId;

    private String question;

    public AskQuestionRequest() {
    }

    public String getWxId() {
        return wxId;
    }

    public void setWxId(String wxId) {
        this.wxId = wxId;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }
}
