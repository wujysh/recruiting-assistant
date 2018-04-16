package com.sap.recruiting.assistant.controller;

import com.sap.recruiting.assistant.controller.bean.AskRequest;
import com.sap.recruiting.assistant.controller.bean.AskResponse;
import com.sap.recruiting.assistant.entity.Company;
import com.sap.recruiting.assistant.entity.Question;
import com.sap.recruiting.assistant.entity.Tag;
import com.sap.recruiting.assistant.service.CompanyService;
import com.sap.recruiting.assistant.service.QuestionService;
import com.sap.recruiting.assistant.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

/**
 * Created by Jiaye Wu on 18-4-16.
 */
@RequestMapping("/api")
@RestController
public class ApiController {

    private final CompanyService companyService;

    private final QuestionService questionService;

    private final TagService tagService;

    @Autowired
    public ApiController(CompanyService companyService, QuestionService questionService, TagService tagService) {
        this.companyService = companyService;
        this.questionService = questionService;
        this.tagService = tagService;
    }

    @RequestMapping("/ask")
    @ResponseBody
    public AskResponse ask(@RequestBody AskRequest request) {
        Optional<Company> company = companyService.getCompanyRepository().findByName(request.getCompany());
        Question question = new Question(request.getQuestion(), 0);
        question.setCompany(company.orElse(null));

        // get tag from model calculation
        String tagString = questionService.getQuestionTagFromModel(question.getContent());
        Optional<Tag> tag;
        if (tagString.startsWith("40")) {
            tag = Optional.empty();
        } else {
            tag = tagService.getTagRepository().findByName(tagString);
        }
        question.setTag(tag.orElse(null));

        // record the question for future model improvement
        questionService.getQuestionRepository().save(question);

        // construct the response structure
        AskResponse response = new AskResponse();
        if (tag.isPresent() && company.isPresent() && company.get().getProperties().containsKey(tag.get())) {
            response.setAnswer(company.get().getProperties().get(tag.get()));
            response.setSuccess(true);
        } else {
            response.setAnswer(tagString);
            response.setSuccess(false);
        }
        return response;
    }
}
