package com.sap.recuriting.assistant.service;

import com.sap.recuriting.assistant.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Jiaye Wu on 18-3-26.
 */
public class CompanyService {

    private final CompanyRepository companyRepository;

    @Autowired
    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }
}
