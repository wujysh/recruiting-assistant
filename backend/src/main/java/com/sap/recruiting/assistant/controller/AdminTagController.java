package com.sap.recruiting.assistant.controller;

import com.sap.recruiting.assistant.entity.Company;
import com.sap.recruiting.assistant.entity.Question;
import com.sap.recruiting.assistant.entity.Tag;
import com.sap.recruiting.assistant.entity.User;
import com.sap.recruiting.assistant.exception.ServiceException;
import com.sap.recruiting.assistant.service.CompanyService;
import com.sap.recruiting.assistant.service.QuestionService;
import com.sap.recruiting.assistant.service.TagService;
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
public class AdminTagController {

    private final TagService tagService;

    private final UserService userService;

    private final CompanyService companyService;

    private final QuestionService questionService;

    @Autowired
    public AdminTagController(TagService tagService, UserService userService, CompanyService companyService, QuestionService questionService) {
        this.tagService = tagService;
        this.userService = userService;
        this.companyService = companyService;
        this.questionService = questionService;
    }

    @RequestMapping("/admin/tag")
    public ModelAndView tagList(HttpServletRequest request, RedirectAttributes attributes) throws ServiceException {
        User currentUser = userService.getSessionUser();
        if (currentUser.getCompany() == null) {  // super administrator: display tag list
            ModelAndView mav = new ModelAndView("admin/tag/list");
            mav.addObject("tagList", tagService.getTagRepository().findAll());
            mav.addObject("currentUser", currentUser);
            return mav;
        } else {  // company manager: update company tag content
            Map<String, ?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);
            if (inputFlashMap != null) {
                for (Map.Entry<String, ?> entry : inputFlashMap.entrySet()) {
                    attributes.addFlashAttribute(entry.getKey(), entry.getValue());
                }
            }
            return new ModelAndView("redirect:/admin/company/" + currentUser.getCompany().getId() + "/tag");
        }
    }

    @RequestMapping(value = "/admin/tag/new", method = RequestMethod.GET)
    public ModelAndView tagNewGet(RedirectAttributes attributes) throws ServiceException {
        User currentUser = userService.getSessionUser();
        if (currentUser.getCompany() == null) {  // super administrator only
            ModelAndView mav = new ModelAndView("admin/tag/edit");
            mav.addObject("tag", new Tag());
            mav.addObject("currentUser", currentUser);
            return mav;
        } else {
            attributes.addFlashAttribute("failure", "Not Authorized!");
            return new ModelAndView("redirect:/admin/tag");
        }
    }

    @RequestMapping(value = "/admin/tag/new", method = RequestMethod.POST)
    public String tagNewPost(String name, String description, RedirectAttributes attributes) throws ServiceException {
        User currentUser = userService.getSessionUser();
        if (currentUser.getCompany() == null) {  // super administrator only
            Tag tag = new Tag();
            tag.setName(name);
            tag.setDescription(description);
            tagService.getTagRepository().save(tag);
            attributes.addFlashAttribute("success", "Tag has been created successfully.");
        } else {
            attributes.addFlashAttribute("failure", "Not Authorized!");
        }
        return "redirect:/admin/tag";
    }

    @RequestMapping(value = "/admin/tag/{id}", method = RequestMethod.GET)
    public ModelAndView tagEditGet(@PathVariable int id, RedirectAttributes attributes) throws ServiceException {
        User currentUser = userService.getSessionUser();
        Optional<Tag> tag = tagService.getTagRepository().findById(id);
        if (!tag.isPresent()) {
            attributes.addFlashAttribute("failure", "Tag not found!");
            return new ModelAndView("redirect:/admin/tag");
        }
        if (currentUser.getCompany() == null) {  // super administrator only
            ModelAndView mav = new ModelAndView("admin/tag/edit");
            mav.addObject("tag", tag.get());
            mav.addObject("currentUser", currentUser);
            return mav;
        } else {
            attributes.addFlashAttribute("failure", "Not Authorized!");
            return new ModelAndView("redirect:/admin/tag");
        }
    }

    @RequestMapping(value = "/admin/tag/{id}", method = RequestMethod.POST)
    public String tagEditPost(@PathVariable int id, String name, String description, RedirectAttributes attributes) throws ServiceException {
        User currentUser = userService.getSessionUser();
        Optional<Tag> tag = tagService.getTagRepository().findById(id);
        if (!tag.isPresent()) {
            attributes.addFlashAttribute("failure", "Tag not found!");
        } else {
            if (currentUser.getCompany() == null) {  // super administrator only
                tag.get().setName(name);
                tag.get().setDescription(description);
                tagService.getTagRepository().save(tag.get());
                attributes.addFlashAttribute("success", "Tag has been updated successfully.");
            } else {
                attributes.addFlashAttribute("failure", "Not Authorized!");
            }
        }
        return "redirect:/admin/tag";
    }

    @RequestMapping(value = "/admin/tag/{id}/delete")
    public String tagDelete(@PathVariable int id, RedirectAttributes attributes) throws ServiceException {
        User currentUser = userService.getSessionUser();
        Optional<Tag> tag = tagService.getTagRepository().findById(id);
        if (!tag.isPresent()) {
            attributes.addFlashAttribute("failure", "Tag not found!");
        } else {
            if (currentUser.getCompany() == null) {  // super administrator only
                List<Company> companyList = companyService.getCompanyRepository().findAll();
                for (Company company : companyList) {
                    if (company.getProperties().containsKey(tag.get())) {
                        company.getProperties().remove(tag.get());
                        companyService.getCompanyRepository().save(company);
                    }
                }
                List<Question> questionList = questionService.getQuestionRepository().findByTag(tag.get());
                for (Question question : questionList) {
                    question.setTag(null);
                    questionService.getQuestionRepository().save(question);
                }
                tagService.getTagRepository().delete(tag.get());
                attributes.addFlashAttribute("success", "Tag has been deleted successfully.");
            } else {
                attributes.addFlashAttribute("failure", "Not Authorized!");
            }
        }
        return "redirect:/admin/tag";
    }
}
