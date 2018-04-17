package com.sap.recruiting.assistant.controller;

import com.sap.recruiting.assistant.entity.User;
import com.sap.recruiting.assistant.exception.ServiceException;
import com.sap.recruiting.assistant.service.InterviewService;
import com.sap.recruiting.assistant.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by Jiaye Wu on 18-4-17.
 */
@Controller
public class AdminInterviewController {

    private final InterviewService interviewService;

    private final UserService userService;

    @Autowired
    public AdminInterviewController(InterviewService interviewService, UserService userService) {
        this.interviewService = interviewService;
        this.userService = userService;
    }

    @RequestMapping("/admin/interview")
    public ModelAndView interviewList(Pageable pageable) throws ServiceException {
        User currentUser = userService.getSessionUser();
        ModelAndView mav = new ModelAndView("admin/interview/list");
        if (currentUser.getCompany() == null) {  // super administrator: display interview list
            mav.addObject("interviewList", interviewService.getInterviewRepository().findAll(pageable));
        } else {  // company manager: display company interview list
            mav.addObject("interviewList", interviewService.getInterviewRepository().findByCompany(currentUser.getCompany(), pageable));
        }
        mav.addObject("page", pageable.getPageNumber());
        mav.addObject("currentUser", currentUser);
        return mav;
    }
}
