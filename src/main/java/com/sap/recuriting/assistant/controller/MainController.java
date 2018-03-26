package com.sap.recuriting.assistant.controller;

import com.sap.recuriting.assistant.entity.Company;
import com.sap.recuriting.assistant.entity.User;
import com.sap.recuriting.assistant.repository.CompanyRepository;
import com.sap.recuriting.assistant.repository.UserRepository;
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

    private final CompanyRepository companyRepository;

    private final UserRepository userRepository;

    @Autowired
    public MainController(CompanyRepository companyRepository, UserRepository userRepository) {
        this.companyRepository = companyRepository;
        this.userRepository = userRepository;
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
        Company company = new Company("SAP");
        if (companyRepository.findByName("SAP") == null) {
            companyRepository.save(company);
        }
        if (userRepository.findByUsername("admin") == null) {
            User user = new User(company, "admin");
            PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
            user.setPassword(encoder.encode("admin"));
            userRepository.save(user);
        }
        return "redirect:/";
    }
}
