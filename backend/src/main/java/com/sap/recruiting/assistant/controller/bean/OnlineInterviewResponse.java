package com.sap.recruiting.assistant.controller.bean;

/**
 * Created by Jiaye Wu on 18-4-17.
 */
public class OnlineInterviewResponse {

    private boolean success;

    private int problemId;

    private String problem;

    public OnlineInterviewResponse() {
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getProblemId() {
        return problemId;
    }

    public void setProblemId(int problemId) {
        this.problemId = problemId;
    }

    public String getProblem() {
        return problem;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }
}
