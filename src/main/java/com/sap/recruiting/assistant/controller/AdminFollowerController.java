package com.sap.recruiting.assistant.controller;

import com.sap.recruiting.assistant.entity.User;
import com.sap.recruiting.assistant.exception.ServiceException;
import com.sap.recruiting.assistant.service.FollowerService;
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
public class AdminFollowerController {

    private final FollowerService followerService;

    private final UserService userService;

    @Autowired
    public AdminFollowerController(FollowerService followerService, UserService userService) {
        this.followerService = followerService;
        this.userService = userService;
    }

    @RequestMapping("/admin/follower")
    public ModelAndView followerList(Pageable pageable) throws ServiceException {
        User currentUser = userService.getSessionUser();
        ModelAndView mav = new ModelAndView("admin/follower/list");
        if (currentUser.getCompany() == null) {  // super administrator: display follower list
            mav.addObject("followerList", followerService.getFollowerRepository().findAll(pageable));
        } else {  // company manager: display company follower list
            mav.addObject("followerList", followerService.getFollowerRepository().findByCompany(currentUser.getCompany(), pageable));
        }
        mav.addObject("page", pageable.getPageNumber());
        mav.addObject("currentUser", currentUser);
        return mav;
    }
}
