package com.sap.recuriting.assistant.controller;

import com.sap.recuriting.assistant.entity.User;
import com.sap.recuriting.assistant.exception.ServiceException;
import com.sap.recuriting.assistant.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by Jiaye Wu on 18-3-26.
 */
@Controller()
public class AdminController {

    @Autowired
    private UserService userService;

    @RequestMapping("/admin")
    public ModelAndView index() throws ServiceException {
        ModelAndView mav = new ModelAndView("admin/index");
        User user = userService.getSessionUser();
        mav.addObject("user", user);
        return mav;
    }
}
