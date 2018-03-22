package com.sap.recuriting.assistant.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class BasicDao {

    private static final Logger logger = LoggerFactory.getLogger(BasicDao.class);

    @Autowired
    public BasicDao() {

    }
}
