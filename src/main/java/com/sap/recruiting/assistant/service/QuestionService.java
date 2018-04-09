package com.sap.recruiting.assistant.service;

import com.sap.recruiting.assistant.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Jiaye Wu on 18-4-9.
 */
@Service
public class QuestionService {

    private final QuestionRepository questionRepository;

    @Autowired
    public QuestionService(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    public QuestionRepository getQuestionRepository() {
        return questionRepository;
    }
}
