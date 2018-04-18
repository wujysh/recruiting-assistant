package com.sap.recruiting.assistant.controller;

import com.sap.recruiting.assistant.controller.form.CompanyTagForm;
import com.sap.recruiting.assistant.entity.Company;
import com.sap.recruiting.assistant.entity.Tag;
import com.sap.recruiting.assistant.entity.User;
import com.sap.recruiting.assistant.exception.ServiceException;
import com.sap.recruiting.assistant.service.CompanyService;
import com.sap.recruiting.assistant.service.TagService;
import com.sap.recruiting.assistant.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Created by Jiaye Wu on 18-4-3.
 */
@Controller
public class AdminCompanyTagController {

    private final CompanyService companyService;

    private final TagService tagService;

    private final UserService userService;

    @Autowired
    public AdminCompanyTagController(CompanyService companyService, TagService tagService, UserService userService) {
        this.companyService = companyService;
        this.tagService = tagService;
        this.userService = userService;
    }

    @RequestMapping(value = "/admin/company/{id}/tag", method = RequestMethod.GET)
    public ModelAndView tagCompanyGet(@PathVariable int id, RedirectAttributes attributes) throws ServiceException {
        User currentUser = userService.getSessionUser();
        Optional<Company> company = companyService.getCompanyRepository().findById(id);
        if (!company.isPresent()) {
            attributes.addFlashAttribute("failure", "Company not found!");
            return new ModelAndView("redirect:/admin/company");
        }
        if (currentUser.getCompany() == null || currentUser.getCompany().getId() == company.get().getId()) {  // super administrator / the managed company
            List<Tag> tagList = tagService.getTagRepository().findAll();
            CompanyTagForm form = new CompanyTagForm();
            for (Tag tag : tagList) {
                form.getTagMap().put(tag.getName(), company.get().getProperties().getOrDefault(tag, ""));
            }
            ModelAndView mav = new ModelAndView("admin/company/tag/edit");
            mav.addObject("companyTagForm", form);
            mav.addObject("company", company.get());
            mav.addObject("currentUser", currentUser);
            return mav;
        } else {
            attributes.addFlashAttribute("failure", "Not Authorized!");
            return new ModelAndView("redirect:/admin/tag");
        }
    }

    @RequestMapping(value = "/admin/company/{id}/tag", method = RequestMethod.POST)
    public String companyEditPost(@PathVariable int id, @ModelAttribute("companyTagForm") CompanyTagForm form, RedirectAttributes attributes) throws ServiceException {
        User currentUser = userService.getSessionUser();
        Optional<Company> company = companyService.getCompanyRepository().findById(id);
        if (!company.isPresent()) {
            attributes.addFlashAttribute("failure", "Company not found!");
        } else {
            if (currentUser.getCompany() == null || currentUser.getCompany().getId() == company.get().getId()) {  // super administrator / the managed company
                Map<Tag, String> properties = new HashMap<>();
                for (Map.Entry<String, String> entry : form.getTagMap().entrySet()) {
                    Optional<Tag> tag = tagService.getTagRepository().findByName(entry.getKey());
                    if (!tag.isPresent()) {
                        attributes.addFlashAttribute("failure", "Tag " + entry.getKey() + " not found!");
                        return "redirect:/admin/company/" + company.get().getId() + "/tag";
                    }
                    properties.put(tag.get(), entry.getValue());
                }
                company.get().setProperties(properties);
                companyService.getCompanyRepository().save(company.get());
                attributes.addFlashAttribute("success", "Tag of the company has been updated successfully.");
            } else {
                attributes.addFlashAttribute("failure", "Not Authorized!");
            }
        }
        if (currentUser.getCompany() == null) {
            return "redirect:/admin/company";
        } else {
            return "redirect:/admin/tag";
        }
    }
}
