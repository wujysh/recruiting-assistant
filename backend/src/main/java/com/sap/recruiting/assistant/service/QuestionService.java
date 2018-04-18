package com.sap.recruiting.assistant.service;

import com.sap.recruiting.assistant.entity.Question;
import com.sap.recruiting.assistant.repository.QuestionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Optional;

/**
 * Created by Jiaye Wu on 18-4-9.
 */
@Service
public class QuestionService {

    private static final Logger logger = LoggerFactory.getLogger(QuestionService.class);

    private final QuestionRepository questionRepository;

    @Autowired
    public QuestionService(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    public QuestionRepository getQuestionRepository() {
        return questionRepository;
    }

    public String getQuestionTagFromModel(String question) {
        try {
            // execute the model in python
            ProcessBuilder pb = new ProcessBuilder("python", "model.py", question);
            pb.directory(new File("../model"));
            Process p = pb.start();

            // collect the output stream
            BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String code = in.readLine().split(":")[1];
            if (code.equals("200")) {
                return in.readLine().split(":")[1];
            } else {
                Optional<Question> question1 = questionRepository.findFirstByTypeAndContentIsLike(1, "%" + question.substring(0, question.length()-1) + "%");
                if (question1.isPresent()) {
                    return question1.get().getTag().getName();
                } else {
                    return code;
                }
            }
        } catch (IOException e) {
            logger.error(e.getMessage(), e.getCause());
            return "400";
        }
    }
}
