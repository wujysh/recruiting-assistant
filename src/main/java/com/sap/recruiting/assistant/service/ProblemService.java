package com.sap.recruiting.assistant.service;

import com.sap.recruiting.assistant.repository.ProblemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Jiaye Wu on 18-4-17.
 */
@Service
public class ProblemService {

    private final ProblemRepository problemRepository;

    @Autowired
    public ProblemService(ProblemRepository problemRepository) {
        this.problemRepository = problemRepository;
    }

    public ProblemRepository getProblemRepository() {
        return problemRepository;
    }
}
