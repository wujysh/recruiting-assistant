package com.sap.recruiting.assistant.controller;

import com.sap.recruiting.assistant.controller.bean.*;
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

import java.util.List;
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

    @RequestMapping("/companies")
    @ResponseBody
    public CompanyListResponse companyList() {
        List<Company> companyList = companyService.getCompanyRepository().findAll();
        CompanyListResponse response = new CompanyListResponse();
        for (Company company : companyList) {
            response.getCompanies().add(company.getName());
        }
        return response;
    }

    @RequestMapping("/select")
    @ResponseBody
    public SelectCompanyResponse selectCompany(@RequestBody SelectCompanyRequest request) {
        // TODO
        SelectCompanyResponse response = new SelectCompanyResponse();
        response.setSuccess(true);
        return response;
    }

    @RequestMapping("/ask")
    @ResponseBody
    public AskQuestionResponse ask(@RequestBody AskQuestionRequest request) {
        String companyName = "SAP";  // TODO
        Optional<Company> company = companyService.getCompanyRepository().findByName(companyName);
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
        AskQuestionResponse response = new AskQuestionResponse();
        if (tag.isPresent() && company.isPresent() && company.get().getProperties().containsKey(tag.get())) {
            response.setAnswer(company.get().getProperties().get(tag.get()));
            response.setSuccess(true);
        } else {
            response.setAnswer(tagString);
            response.setSuccess(false);
        }
        return response;
    }

    @RequestMapping("/apply")
    @ResponseBody
    public ApplyInterviewResponse applyInterview(@RequestBody ApplyInterviewRequest request) {
        // TODO
        ApplyInterviewResponse response = new ApplyInterviewResponse();
        response.setSuccess(true);
        return response;
    }

    @RequestMapping("/interview")
    @ResponseBody
    public OnlineInterviewResponse onlineInterview(@RequestBody OnlineInterviewRequest request) {
        // TODO
        OnlineInterviewResponse response = new OnlineInterviewResponse();
        response.setSuccess(true);
        response.setProblemId(1);
        response.setProblem("测试题目");
        return response;
    }
}
