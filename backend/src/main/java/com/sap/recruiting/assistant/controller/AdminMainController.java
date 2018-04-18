package com.sap.recruiting.assistant.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Jiaye Wu on 18-3-26.
 */
@Controller
public class AdminMainController {

    @Autowired
    public AdminMainController() {

    }

    @RequestMapping("/admin")
    public String index() {
        return "redirect:/admin/company";
    }
}
