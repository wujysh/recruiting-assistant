package com.sap.recruiting.assistant.controller.bean;

/**
 * Created by Jiaye Wu on 18-4-16.
 */
public class AskRequest {

    private String company;

    private String question;

    public AskRequest() {
    }

    public AskRequest(String company, String question) {
        this.company = company;
        this.question = question;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }
}
