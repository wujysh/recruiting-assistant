package com.sap.recruiting.assistant.service;

import com.sap.recruiting.assistant.entity.Question;
import com.sap.recruiting.assistant.entity.Tag;
import com.sap.recruiting.assistant.repository.QuestionRepository;
import com.sap.recruiting.assistant.repository.TagRepository;
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

    private final TagRepository tagRepository;

    @Autowired
    public QuestionService(QuestionRepository questionRepository, TagRepository tagRepository) {
        this.questionRepository = questionRepository;
        this.tagRepository = tagRepository;
    }

    public QuestionRepository getQuestionRepository() {
        return questionRepository;
    }

    public Tag getQuestionTagFromModel(Question question) {
        try {
            ProcessBuilder pb = new ProcessBuilder("python", "model.py", question.getContent());
            pb.directory(new File("model"));
            Process p = pb.start();

            BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
            System.out.println(in.readLine());

            Optional<Tag> tag = tagRepository.findByName("");
            return tag.orElse(null);
        } catch (IOException e) {
            logger.error(e.getMessage(), e.getCause());
            return null;
        }
    }
}
