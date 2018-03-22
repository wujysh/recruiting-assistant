package com.sap.recuriting.assistant.service;

import com.sap.recuriting.assistant.dao.BasicDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QueryService {

    private final BasicDao basicDao;

    @Autowired
    public QueryService(BasicDao basicDao) {
        this.basicDao = basicDao;
    }
}
