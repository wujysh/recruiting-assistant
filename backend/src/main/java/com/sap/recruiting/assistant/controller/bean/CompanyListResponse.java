package com.sap.recruiting.assistant.controller.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jiaye Wu on 18-4-17.
 */
public class CompanyListResponse {

    private List<String> companies;

    public CompanyListResponse() {
        companies = new ArrayList<>();
    }

    public CompanyListResponse(List<String> companies) {
        this.companies = companies;
    }

    public List<String> getCompanies() {
        return companies;
    }

    public void setCompanies(List<String> companies) {
        this.companies = companies;
    }
}
