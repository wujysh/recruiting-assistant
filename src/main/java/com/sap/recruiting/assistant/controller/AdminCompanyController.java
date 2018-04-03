package com.sap.recruiting.assistant.controller;

import com.sap.recruiting.assistant.entity.Company;
import com.sap.recruiting.assistant.entity.User;
import com.sap.recruiting.assistant.exception.ServiceException;
import com.sap.recruiting.assistant.service.CompanyService;
import com.sap.recruiting.assistant.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Created by Jiaye Wu on 18-4-3.
 */
@Controller
public class AdminCompanyController {

    private final CompanyService companyService;

    private final UserService userService;

    @Autowired
    public AdminCompanyController(CompanyService companyService, UserService userService) {
        this.companyService = companyService;
        this.userService = userService;
    }

    @RequestMapping("/admin/company")
    public ModelAndView companyList(HttpServletRequest request, RedirectAttributes attributes) throws ServiceException {
        User currentUser = userService.getSessionUser();
        if (currentUser.getCompany() == null) {  // super administrator: display company list
            ModelAndView mav = new ModelAndView("admin/company/list");
            mav.addObject("companyList", companyService.getCompanyRepository().findAll());
            mav.addObject("currentUser", currentUser);
            return mav;
        } else {  // company manager: update company information
            Map<String, ?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);
            if (inputFlashMap != null) {
                for (Map.Entry<String, ?> entry : inputFlashMap.entrySet()) {
                    attributes.addFlashAttribute(entry.getKey(), entry.getValue());
                }
            }
            return new ModelAndView("redirect:/admin/company/" + currentUser.getCompany().getId());
        }
    }

    @RequestMapping(value = "/admin/company/new", method = RequestMethod.GET)
    public ModelAndView companyNewGet(RedirectAttributes attributes) throws ServiceException {
        User currentUser = userService.getSessionUser();
        if (currentUser.getCompany() == null) {  // super administrator only
            ModelAndView mav = new ModelAndView("admin/company/edit");
            mav.addObject("company", new Company());
            mav.addObject("currentUser", currentUser);
            return mav;
        } else {
            attributes.addFlashAttribute("failure", "Not Authorized!");
            return new ModelAndView("redirect:/admin/company");
        }
    }

    @RequestMapping(value = "/admin/company/new", method = RequestMethod.POST)
    public String companyNewPost(String name, RedirectAttributes attributes) throws ServiceException {
        User currentUser = userService.getSessionUser();
        if (currentUser.getCompany() == null) {  // super administrator only
            Company company = new Company();
            company.setName(name);
            companyService.getCompanyRepository().save(company);
            attributes.addFlashAttribute("success", "Company has been created successfully.");
        } else {
            attributes.addFlashAttribute("failure", "Not Authorized!");
        }
        return "redirect:/admin/company";
    }

    @RequestMapping(value = "/admin/company/{id}", method = RequestMethod.GET)
    public ModelAndView companyEditGet(@PathVariable int id, RedirectAttributes attributes) throws ServiceException {
        User currentUser = userService.getSessionUser();
        Optional<Company> company = companyService.getCompanyRepository().findById(id);
        if (!company.isPresent()) {
            attributes.addFlashAttribute("failure", "Company not found!");
            return new ModelAndView("redirect:/admin/company");
        }
        if (currentUser.getCompany() == null || currentUser.getCompany().getId() == company.get().getId()) {  // super administrator / the managed company
            ModelAndView mav = new ModelAndView("admin/company/edit");
            mav.addObject("company", company.get());
            mav.addObject("currentUser", currentUser);
            return mav;
        } else {
            attributes.addFlashAttribute("failure", "Not Authorized!");
            return new ModelAndView("redirect:/admin/company");
        }
    }

    @RequestMapping(value = "/admin/company/{id}", method = RequestMethod.POST)
    public String companyEditPost(@PathVariable int id, String name, RedirectAttributes attributes) throws ServiceException {
        User currentUser = userService.getSessionUser();
        Optional<Company> company = companyService.getCompanyRepository().findById(id);
        if (!company.isPresent()) {
            attributes.addFlashAttribute("failure", "Company not found!");
        } else {
            if (currentUser.getCompany() == null || currentUser.getCompany().getId() == company.get().getId()) {  // super administrator / the managed company
                company.get().setName(name);
                companyService.getCompanyRepository().save(company.get());
                attributes.addFlashAttribute("success", "Company has been updated successfully.");
            } else {
                attributes.addFlashAttribute("failure", "Not Authorized!");
            }
        }
        return "redirect:/admin/company";
    }

    @RequestMapping(value = "/admin/company/{id}/delete")
    public String companyDelete(@PathVariable int id, RedirectAttributes attributes) throws ServiceException {
        User currentUser = userService.getSessionUser();
        Optional<Company> company = companyService.getCompanyRepository().findById(id);
        if (!company.isPresent()) {
            attributes.addFlashAttribute("failure", "Company not found!");
        } else {
            if (currentUser.getCompany() == null || currentUser.getCompany().getId() == company.get().getId()) {  // super administrator / the managed company
                List<User> userList = userService.getUserRepository().findByCompany(company.get());
                for (User user : userList) {
                    userService.getUserRepository().delete(user);
                }
                companyService.getCompanyRepository().delete(company.get());
                attributes.addFlashAttribute("success", "Company has been deleted successfully.");
            } else {
                attributes.addFlashAttribute("failure", "Not Authorized!");
            }
        }
        return "redirect:/admin/company";
    }
}
