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
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by Jiaye Wu on 18-4-3.
 */
@Controller
public class AdminQuestionController {

    private final TagService tagService;

    private final UserService userService;

    private final CompanyService companyService;

    private final QuestionService questionService;

    @Autowired
    public AdminQuestionController(TagService tagService, UserService userService, CompanyService companyService, QuestionService questionService) {
        this.tagService = tagService;
        this.userService = userService;
        this.companyService = companyService;
        this.questionService = questionService;
    }

    @RequestMapping("/admin/question")
    public ModelAndView questionList(@RequestParam(defaultValue = "0") int type, @RequestParam(defaultValue = "0") int tagId, Pageable pageable, RedirectAttributes attributes) throws ServiceException {
        User currentUser = userService.getSessionUser();
        ModelAndView mav = new ModelAndView("admin/question/list");
        if (tagId == 0) {
            if (currentUser.getCompany() == null) {  // super administrator: display question list
                mav.addObject("questionList", questionService.getQuestionRepository().findByTypeOrderByIdDesc(type, pageable));
            } else {  // company manager: display company question list (read only)
                mav.addObject("questionList", questionService.getQuestionRepository().findByCompanyAndTypeOrderByIdDesc(currentUser.getCompany(), type, pageable));
            }
        } else {
            Optional<Tag> tag = tagService.getTagRepository().findById(tagId);
            if (!tag.isPresent()) {
                attributes.addFlashAttribute("failure", "Tag not found!");
                return new ModelAndView("redirect:/admin/question");
            } else {
                if (currentUser.getCompany() == null) {  // super administrator: display question list
                    mav.addObject("questionList", questionService.getQuestionRepository().findByTagAndTypeOrderByIdDesc(tag.get(), type, pageable));
                } else {  // company manager: display company question list (read only)
                    mav.addObject("questionList", questionService.getQuestionRepository().findByCompanyAndTagAndTypeOrderByIdDesc(currentUser.getCompany(), tag.get(), type, pageable));
                }
                mav.addObject("tag", tag.get());
            }
        }
        mav.addObject("type", type);
        mav.addObject("tagList", tagService.getTagRepository().findAll());
        mav.addObject("page", pageable.getPageNumber());
        mav.addObject("currentUser", currentUser);
        return mav;
    }

    @RequestMapping(value = "/admin/question/import", method = RequestMethod.GET)
    public ModelAndView importGet(RedirectAttributes attributes) throws ServiceException {
        ModelAndView mav = new ModelAndView("admin/question/import");
        User currentUser = userService.getSessionUser();
        if (currentUser.getCompany() == null) {  // super administrator only
            mav.addObject("currentUser", currentUser);
            return mav;
        } else {
            attributes.addFlashAttribute("failure", "Not Authorized!");
            return new ModelAndView("redirect:/admin/question");
        }
    }

    @RequestMapping(value = "/admin/question/import", method = RequestMethod.POST)
    public String importPost(@RequestParam("file") MultipartFile file, boolean tagged, RedirectAttributes attributes) {
        if (!file.isEmpty()) {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
                List<Question> result = new ArrayList<>();
                String line;
                while ((line = br.readLine()) != null) {
                    Question question;
                    if (!tagged) {
                        question = new Question(line, 0);
                    } else {
                        String[] strings = line.split("\t");
                        if (strings.length < 2) continue;
                        String tagName = strings[0];
                        Optional<Tag> tag = tagService.getTagRepository().findByName(tagName);
                        if (!tag.isPresent() && !tagName.equals("未知")) {
                            Tag tag1 = new Tag(tagName, tagName);
                            tagService.getTagRepository().save(tag1);
                            tag = tagService.getTagRepository().findByName(tagName);
                        }
                        question = new Question(strings[1], 0);
                        question.setTag(tag.orElse(null));
                    }
                    result.add(question);
                }
                questionService.getQuestionRepository().saveAll(result);
                attributes.addFlashAttribute("success", "Tags have been imported successfully.");
            } catch (Exception e) {
                attributes.addFlashAttribute("failure", "Error occurred during import!");
            }
        } else {
            attributes.addFlashAttribute("failure", "Error occurred during import!");
        }
        return "redirect:/admin/question";
    }

    @RequestMapping(value = "/admin/question/new", method = RequestMethod.GET)
    public ModelAndView questionNewGet(RedirectAttributes attributes) throws ServiceException {
        User currentUser = userService.getSessionUser();
        if (currentUser.getCompany() == null) {  // super administrator only
            ModelAndView mav = new ModelAndView("admin/question/edit");
            mav.addObject("question", new Question());
            mav.addObject("companyList", companyService.getCompanyRepository().findAll());
            mav.addObject("tagList", tagService.getTagRepository().findAll());
            mav.addObject("currentUser", currentUser);
            return mav;
        } else {
            attributes.addFlashAttribute("failure", "Not Authorized!");
            return new ModelAndView("redirect:/admin/question");
        }
    }

    @RequestMapping(value = "/admin/question/new", method = RequestMethod.POST)
    public String questionNewPost(String content, int type, int companyId, int tagId, RedirectAttributes attributes) throws ServiceException {
        User currentUser = userService.getSessionUser();
        if (currentUser.getCompany() == null) {  // super administrator only
            Optional<Company> company = Optional.empty();
            if (companyId != 0) {
                company = companyService.getCompanyRepository().findById(companyId);
            }
            if (companyId != 0 && !company.isPresent()) {
                attributes.addFlashAttribute("failure", "Company not found!");
            } else {
                Optional<Tag> tag = Optional.empty();
                if (tagId != 0) {
                    tag = tagService.getTagRepository().findById(tagId);
                }
                if (tagId != 0 && !tag.isPresent()) {
                    attributes.addFlashAttribute("failure", "Tag not found!");
                } else {
                    Question question = new Question(content, type);
                    question.setCompany(company.orElse(null));
                    question.setTag(tag.orElse(null));
                    questionService.getQuestionRepository().save(question);
                    attributes.addFlashAttribute("success", "Question has been created successfully.");
                }
            }
        } else {
            attributes.addFlashAttribute("failure", "Not Authorized!");
        }
        return "redirect:/admin/question";
    }

    @RequestMapping(value = "/admin/question/{id}", method = RequestMethod.GET)
    public ModelAndView questionEditGet(@PathVariable int id, RedirectAttributes attributes) throws ServiceException {
        User currentUser = userService.getSessionUser();
        Optional<Question> question = questionService.getQuestionRepository().findById(id);
        if (!question.isPresent()) {
            attributes.addFlashAttribute("failure", "Question not found!");
            return new ModelAndView("redirect:/admin/question");
        }
        if (currentUser.getCompany() == null || question.get().getCompany() != null) {  // super administrator only
            ModelAndView mav = new ModelAndView("admin/question/edit");
            mav.addObject("question", question.get());
            mav.addObject("companyList", companyService.getCompanyRepository().findAll());
            mav.addObject("tagList", tagService.getTagRepository().findAll());
            mav.addObject("currentUser", currentUser);
            return mav;
        } else {
            attributes.addFlashAttribute("failure", "Not Authorized!");
            return new ModelAndView("redirect:/admin/question");
        }
    }

    @RequestMapping(value = "/admin/question/{id}", method = RequestMethod.POST)
    public String questionEditPost(@PathVariable int id, String content, int type, int companyId, int tagId, RedirectAttributes attributes) throws ServiceException {
        User currentUser = userService.getSessionUser();
        Optional<Question> question = questionService.getQuestionRepository().findById(id);
        if (!question.isPresent()) {
            attributes.addFlashAttribute("failure", "Question not found!");
        } else {
            if (currentUser.getCompany() == null) {  // super administrator only
                Optional<Company> company = Optional.empty();
                if (companyId != 0) {
                    company = companyService.getCompanyRepository().findById(companyId);
                }
                if (companyId != 0 && !company.isPresent()) {
                    attributes.addFlashAttribute("failure", "Company not found!");
                } else {
                    Optional<Tag> tag = Optional.empty();
                    if (tagId != 0) {
                        tag = tagService.getTagRepository().findById(tagId);
                    }
                    if (tagId != 0 && !tag.isPresent()) {
                        attributes.addFlashAttribute("failure", "Tag not found!");
                    } else {
                        question.get().setContent(content);
                        question.get().setType(type);
                        question.get().setCompany(company.orElse(null));
                        question.get().setTag(tag.orElse(null));
                        questionService.getQuestionRepository().save(question.get());
                        attributes.addFlashAttribute("success", "Question has been updated successfully.");
                    }
                }
            } else {
                attributes.addFlashAttribute("failure", "Not Authorized!");
            }
        }
        return "redirect:/admin/question";
    }

    @RequestMapping(value = "/admin/question/{id}/delete")
    public String questionDelete(@PathVariable int id, HttpServletRequest request, RedirectAttributes attributes) throws ServiceException {
        User currentUser = userService.getSessionUser();
        Optional<Question> question = questionService.getQuestionRepository().findById(id);
        if (!question.isPresent()) {
            attributes.addFlashAttribute("failure", "Question not found!");
        } else {
            if (currentUser.getCompany() == null) {  // super administrator only
                questionService.getQuestionRepository().delete(question.get());
                attributes.addFlashAttribute("success", "Question has been deleted successfully.");
            } else {
                attributes.addFlashAttribute("failure", "Not Authorized!");
            }
        }
        return "redirect:" + request.getHeader("Referer");
    }

    @RequestMapping(value = "/admin/question/{id}/approve")
    public String questionApprove(@PathVariable int id, HttpServletRequest request, RedirectAttributes attributes) throws ServiceException {
        User currentUser = userService.getSessionUser();
        Optional<Question> question = questionService.getQuestionRepository().findById(id);
        if (!question.isPresent()) {
            attributes.addFlashAttribute("failure", "Question not found!");
        } else {
            if (currentUser.getCompany() == null) {  // super administrator only
                question.get().setType(1 - question.get().getType());
                questionService.getQuestionRepository().save(question.get());
                attributes.addFlashAttribute("success", "Question has been approved successfully.");
            } else {
                attributes.addFlashAttribute("failure", "Not Authorized!");
            }
        }
        return "redirect:" + request.getHeader("Referer");
    }

    @ResponseBody
    @RequestMapping(value = "/admin/question/{id}/tag")
    public String questionTag(@PathVariable int id, int tagId) throws ServiceException {
        User currentUser = userService.getSessionUser();
        Optional<Question> question = questionService.getQuestionRepository().findById(id);
        if (!question.isPresent()) {
            return "{'success': 'false', 'message': 'Question not found!'}";
        } else {
            if (currentUser.getCompany() == null) {  // super administrator only
                Optional<Tag> tag = Optional.empty();
                if (tagId != 0) {
                    tag = tagService.getTagRepository().findById(tagId);
                }
                if (tagId != 0 && !tag.isPresent()) {
                    return "{'success': 'false', 'message': 'Tag not found!'}";
                } else {
                    question.get().setTag(tag.orElse(null));
                    questionService.getQuestionRepository().save(question.get());
                    return "{'success': 'true'}";
                }
            } else {
                return "{'success': 'false', 'message': 'Not Authorized!'}";
            }
        }
    }
}
