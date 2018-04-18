package com.sap.recruiting.assistant.service;

import com.sap.recruiting.assistant.repository.InterviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Jiaye Wu on 18-4-17.
 */
@Service
public class InterviewService {

    private final InterviewRepository interviewRepository;

    @Autowired
    public InterviewService(InterviewRepository interviewRepository) {
        this.interviewRepository = interviewRepository;
    }

    public InterviewRepository getInterviewRepository() {
        return interviewRepository;
    }
}
