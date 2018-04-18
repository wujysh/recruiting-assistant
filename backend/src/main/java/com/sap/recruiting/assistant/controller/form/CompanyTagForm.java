package com.sap.recruiting.assistant.controller.form;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jiaye Wu on 18-4-3.
 */
public class CompanyTagForm {

    private Map<String, String> tagMap = new HashMap<>();

    public Map<String, String> getTagMap() {
        return tagMap;
    }

    public void setTagMap(Map<String, String> tagMap) {
        this.tagMap = tagMap;
    }
}
