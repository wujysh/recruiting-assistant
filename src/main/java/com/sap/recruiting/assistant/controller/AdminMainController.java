package com.sap.recruiting.assistant.controller;

import com.sap.recruiting.assistant.entity.User;
import com.sap.recruiting.assistant.exception.ServiceException;
import com.sap.recruiting.assistant.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by Jiaye Wu on 18-3-26.
 */
@Controller
public class AdminMainController {

    private final UserService userService;

    @Autowired
    public AdminMainController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping("/admin")
    public ModelAndView index() throws ServiceException {
        ModelAndView mav = new ModelAndView("admin/company/list");
        User currentUser = userService.getSessionUser();
        mav.addObject("currentUser", currentUser);
        return mav;
    }
}
