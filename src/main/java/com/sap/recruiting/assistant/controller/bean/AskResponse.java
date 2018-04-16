package com.sap.recruiting.assistant.controller.bean;

/**
 * Created by Jiaye Wu on 18-4-16.
 */
public class AskResponse {

    private boolean success;

    private String answer;

    public AskResponse() {
    }

    public AskResponse(boolean success, String answer) {
        this.success = success;
        this.answer = answer;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
