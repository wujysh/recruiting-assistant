package com.sap.recruiting.assistant.controller;

import com.sap.recruiting.assistant.service.UserService;
import com.sap.recruiting.assistant.entity.Company;
import com.sap.recruiting.assistant.entity.User;
import com.sap.recruiting.assistant.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Jiaye Wu on 18-3-23.
 */
@Controller
public class MainController {

    private final UserService userService;

    private final CompanyService companyService;

    @Autowired
    public MainController(UserService userService, CompanyService companyService) {
        this.userService = userService;
        this.companyService = companyService;
    }

    @RequestMapping("/")
    public ModelAndView index(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("index");
        return mav;
    }

    @RequestMapping("/about")
    public String about() {
        return "about";
    }

    @RequestMapping("/contact")
    public String contact() {
        return "contact";
    }

    @RequestMapping("/init")
    public String init() {
        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        if (!userService.getUserRepository().findByUsername("admin").isPresent()) {
            User user = new User("admin");
            user.setPassword(encoder.encode("admin"));
            userService.getUserRepository().save(user);
        }
        if (!companyService.getCompanyRepository().findByName("SAP").isPresent()) {
            Company company = new Company("SAP");
            companyService.getCompanyRepository().save(company);
            if (!userService.getUserRepository().findByUsername("sap").isPresent()) {
                User user = new User(company, "sap");
                user.setPassword(encoder.encode("sap"));
                userService.getUserRepository().save(user);
            }
        }
        return "redirect:/";
    }
}
