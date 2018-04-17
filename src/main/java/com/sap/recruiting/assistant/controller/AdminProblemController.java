package com.sap.recruiting.assistant.controller;

import com.sap.recruiting.assistant.entity.Company;
import com.sap.recruiting.assistant.entity.Problem;
import com.sap.recruiting.assistant.entity.User;
import com.sap.recruiting.assistant.exception.ServiceException;
import com.sap.recruiting.assistant.service.CompanyService;
import com.sap.recruiting.assistant.service.ProblemService;
import com.sap.recruiting.assistant.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * Created by Jiaye Wu on 18-4-17.
 */
@Controller
public class AdminProblemController {

    private final ProblemService problemService;

    private final UserService userService;

    private final CompanyService companyService;

    @Autowired
    public AdminProblemController(ProblemService problemService, UserService userService, CompanyService companyService) {
        this.problemService = problemService;
        this.userService = userService;
        this.companyService = companyService;
    }

    @RequestMapping("/admin/problem")
    public ModelAndView problemList(Pageable pageable) throws ServiceException {
        User currentUser = userService.getSessionUser();
        ModelAndView mav = new ModelAndView("admin/problem/list");
        if (currentUser.getCompany() == null) {  // super administrator: display problem list
            mav.addObject("problemList", problemService.getProblemRepository().findAll(pageable));
        } else {  // company manager: display company problem list
            mav.addObject("problemList", problemService.getProblemRepository().findByCompany(currentUser.getCompany(), pageable));
        }
        mav.addObject("page", pageable.getPageNumber());
        mav.addObject("currentUser", currentUser);
        return mav;
    }

    @RequestMapping(value = "/admin/problem/new", method = RequestMethod.GET)
    public ModelAndView problemNewGet() throws ServiceException {
        User currentUser = userService.getSessionUser();
        ModelAndView mav = new ModelAndView("admin/problem/edit");
        mav.addObject("problem", new Problem());
        mav.addObject("companyList", companyService.getCompanyRepository().findAll());
        mav.addObject("currentUser", currentUser);
        return mav;
    }

    @RequestMapping(value = "/admin/problem/new", method = RequestMethod.POST)
    public String problemNewPost(String content, String answer, int companyId, RedirectAttributes attributes) throws ServiceException {
        User currentUser = userService.getSessionUser();
        Optional<Company> company = companyService.getCompanyRepository().findById(companyId);
        if (!company.isPresent()) {
            attributes.addFlashAttribute("failure", "Company not found!");
        } else {
            Problem problem = new Problem();
            problem.setContent(content);
            problem.setAnswer(answer);
            if (currentUser.getCompany() == null) {
                problem.setCompany(company.get());
            } else {
                problem.setCompany(currentUser.getCompany());
            }
            problemService.getProblemRepository().save(problem);
            attributes.addFlashAttribute("success", "Problem has been created successfully.");
        }
        return "redirect:/admin/problem";
    }

    @RequestMapping(value = "/admin/problem/{id}", method = RequestMethod.GET)
    public ModelAndView problemEditGet(@PathVariable int id, RedirectAttributes attributes) throws ServiceException {
        User currentUser = userService.getSessionUser();
        Optional<Problem> problem = problemService.getProblemRepository().findById(id);
        if (!problem.isPresent()) {
            attributes.addFlashAttribute("failure", "Problem not found!");
            return new ModelAndView("redirect:/admin/problem");
        }
        if (currentUser.getCompany() == null || problem.get().getCompany().getId() == currentUser.getCompany().getId()) {
            ModelAndView mav = new ModelAndView("admin/problem/edit");
            mav.addObject("problem", problem.get());
            mav.addObject("companyList", companyService.getCompanyRepository().findAll());
            mav.addObject("currentUser", currentUser);
            return mav;
        } else {
            attributes.addFlashAttribute("failure", "Not Authorized!");
            return new ModelAndView("redirect:/admin/problem");
        }
    }

    @RequestMapping(value = "/admin/problem/{id}", method = RequestMethod.POST)
    public String problemEditPost(@PathVariable int id, String content, String answer, int companyId, RedirectAttributes attributes) throws ServiceException {
        User currentUser = userService.getSessionUser();
        Optional<Problem> problem = problemService.getProblemRepository().findById(id);
        if (!problem.isPresent()) {
            attributes.addFlashAttribute("failure", "Problem not found!");
        } else {
            if (currentUser.getCompany() == null || problem.get().getCompany().getId() == currentUser.getCompany().getId()) {
                Optional<Company> company = companyService.getCompanyRepository().findById(companyId);
                if (!company.isPresent()) {
                    attributes.addFlashAttribute("failure", "Company not found!");
                } else {
                    problem.get().setContent(content);
                    if (currentUser.getCompany() == null) {
                        problem.get().setCompany(company.get());
                    }
                    problem.get().setAnswer(answer);
                    problemService.getProblemRepository().save(problem.get());
                    attributes.addFlashAttribute("success", "Problem has been updated successfully.");
                }
            } else {
                attributes.addFlashAttribute("failure", "Not Authorized!");
            }
        }
        return "redirect:/admin/problem";
    }

    @RequestMapping(value = "/admin/problem/{id}/delete")
    public String problemDelete(@PathVariable int id, HttpServletRequest request, RedirectAttributes attributes) throws ServiceException {
        User currentUser = userService.getSessionUser();
        Optional<Problem> problem = problemService.getProblemRepository().findById(id);
        if (!problem.isPresent()) {
            attributes.addFlashAttribute("failure", "Problem not found!");
        } else {
            if (currentUser.getCompany() == null || problem.get().getCompany().getId() == currentUser.getCompany().getId()) {
                problemService.getProblemRepository().delete(problem.get());
                attributes.addFlashAttribute("success", "Problem has been deleted successfully.");
            } else {
                attributes.addFlashAttribute("failure", "Not Authorized!");
            }
        }
        return "redirect:" + request.getHeader("Referer");
    }
}
