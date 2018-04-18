package com.sap.recruiting.assistant.controller.bean;

/**
 * Created by Jiaye Wu on 18-4-17.
 */
public class OnlineInterviewRequest {

    private String wxId;

    private int problemId;

    private String answer;

    public OnlineInterviewRequest() {
    }

    public String getWxId() {
        return wxId;
    }

    public void setWxId(String wxId) {
        this.wxId = wxId;
    }

    public int getProblemId() {
        return problemId;
    }

    public void setProblemId(int problemId) {
        this.problemId = problemId;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
