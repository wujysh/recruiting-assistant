package com.sap.recruiting.assistant.controller;

import com.sap.recruiting.assistant.controller.bean.*;
import com.sap.recruiting.assistant.entity.*;
import com.sap.recruiting.assistant.service.*;
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

    private final FollowerService followerService;

    private final InterviewService interviewService;

    private final ProblemService problemService;

    @Autowired
    public ApiController(CompanyService companyService, QuestionService questionService, TagService tagService,
                         FollowerService followerService, InterviewService interviewService, ProblemService problemService) {
        this.companyService = companyService;
        this.questionService = questionService;
        this.tagService = tagService;
        this.followerService = followerService;
        this.interviewService = interviewService;
        this.problemService = problemService;
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
        SelectCompanyResponse response = new SelectCompanyResponse();
        Optional<Company> company = companyService.getCompanyRepository().findByName(request.getCompany());
        if (company.isPresent()) {
            Optional<Follower> follower = followerService.getFollowerRepository().findByWxId(request.getWxId());
            if (!follower.isPresent()) {
                Follower follower1 = new Follower();
                follower1.setWxId(request.getWxId());
                follower1.setCompany(company.get());
                followerService.getFollowerRepository().save(follower1);
            } else {
                follower.get().setCompany(company.get());
                followerService.getFollowerRepository().save(follower.get());
            }
            response.setSuccess(true);
        } else {
            response.setSuccess(false);
        }
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
        ApplyInterviewResponse response = new ApplyInterviewResponse();
        Optional<Follower> follower = followerService.getFollowerRepository().findByWxId(request.getWxId());
        if (follower.isPresent()) {
            Optional<Interview> interview = interviewService.getInterviewRepository().findByWxIdAndCompany(request.getWxId(), follower.get().getCompany());
            if (!interview.isPresent()) {
                Interview interview1 = new Interview();
                interview1.setWxId(request.getWxId());
                interview1.setName(request.getName());
                interview1.setPhone(request.getPhone());
                interview1.setCompany(follower.get().getCompany());
                interview1.setFinished(false);
                interview1.setScore(0);
                interviewService.getInterviewRepository().save(interview1);
                response.setSuccess(true);
            } else {
                response.setSuccess(false);
            }
        } else {
            response.setSuccess(false);
        }
        return response;
    }

    @RequestMapping("/interview")
    @ResponseBody
    public OnlineInterviewResponse onlineInterview(@RequestBody OnlineInterviewRequest request) {
        OnlineInterviewResponse response = new OnlineInterviewResponse();
        Optional<Follower> follower = followerService.getFollowerRepository().findByWxId(request.getWxId());
        if (follower.isPresent()) {
            Optional<Interview> interview = interviewService.getInterviewRepository().findByWxIdAndCompany(request.getWxId(), follower.get().getCompany());
            if (interview.isPresent()) {
                List<Problem> problems = problemService.getProblemRepository().findByCompany(follower.get().getCompany());
                if (request.getProblemId() != -1) {  // not the first problem, store the result of previous problem
                    Optional<Problem> problem = problemService.getProblemRepository().findById(request.getProblemId());
                    if (problem.isPresent()) {
                        if (request.getAnswer().trim().toUpperCase().equals(problem.get().getAnswer().trim().toUpperCase())) {
                            interview.get().setScore(interview.get().getScore() + 1);
                            interviewService.getInterviewRepository().save(interview.get());
                        }
                    } else {
                        response.setSuccess(false);
                        response.setProblemId(-1);
                        response.setProblem("题目不存在");
                        return response;
                    }
                }
                if (request.getProblemId() == problems.get(problems.size()-1).getId()) {
                    interview.get().setFinished(true);
                    interviewService.getInterviewRepository().save(interview.get());
                    response.setSuccess(true);
                    response.setProblemId(-1);
                    response.setProblem("您已完成在线测试，请耐心等待后续通知，谢谢！");
                } else {
                    int i = 0;
                    while (i < problems.size()) {
                        if (problems.get(i).getId() == request.getProblemId()) {
                            break;
                        }
                        i++;
                    }
                    response.setSuccess(true);
                    response.setProblemId(problems.get(i+1).getId());
                    response.setProblem(problems.get(i+1).getContent());
                }
            } else {
                response.setSuccess(false);
                response.setProblemId(-1);
                response.setProblem("请先投递简历");
            }
        } else {
            response.setSuccess(false);
            response.setProblemId(-1);
            response.setProblem("未选择公司");
        }
        return response;
    }
}
