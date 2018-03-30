package com.sap.recruiting.assistant.controller;

import com.sap.recruiting.assistant.entity.Company;
import com.sap.recruiting.assistant.entity.User;
import com.sap.recruiting.assistant.exception.ServiceException;
import com.sap.recruiting.assistant.service.CompanyService;
import com.sap.recruiting.assistant.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
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
 * Created by Jiaye Wu on 18-3-26.
 */
@Controller
public class AdminController {

    private final UserService userService;

    private final CompanyService companyService;

    @Autowired
    public AdminController(UserService userService, CompanyService companyService) {
        this.userService = userService;
        this.companyService = companyService;
    }

    /*
     * Dashboard
     */
    @RequestMapping("/admin")
    public ModelAndView index() throws ServiceException {
        ModelAndView mav = new ModelAndView("admin/dashboard");
        User currentUser = userService.getSessionUser();
        mav.addObject("currentUser", currentUser);
        return mav;
    }

    /*
     * Company related
     */
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
                companyService.getCompanyRepository().delete(company.get());
                attributes.addFlashAttribute("success", "Company has been deleted successfully.");
            } else {
                attributes.addFlashAttribute("failure", "Not Authorized!");
            }
        }
        return "redirect:/admin/company";
    }

    /*
     * User related
     */
    @RequestMapping("/admin/user")
    public ModelAndView userList(HttpServletRequest request, RedirectAttributes attributes) throws ServiceException {
        User currentUser = userService.getSessionUser();
        if (currentUser.getCompany() == null) {  // super administrator: display user list
            ModelAndView mav = new ModelAndView("admin/user/list");
            mav.addObject("userList", userService.getUserRepository().findAll());
            mav.addObject("currentUser", currentUser);
            return mav;
        } else {  // company manager: update self information
            Map<String, ?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);
            if (inputFlashMap != null) {
                for (Map.Entry<String, ?> entry : inputFlashMap.entrySet()) {
                    attributes.addFlashAttribute(entry.getKey(), entry.getValue());
                }
            }
            return new ModelAndView("redirect:/admin/user/" + currentUser.getId());
        }
    }

    @RequestMapping(value = "/admin/user/new", method = RequestMethod.GET)
    public ModelAndView userNewGet(RedirectAttributes attributes) throws ServiceException {
        User currentUser = userService.getSessionUser();
        if (currentUser.getCompany() == null) {  // super administrator only
            List<Company> companyList = companyService.getCompanyRepository().findAll();
            ModelAndView mav = new ModelAndView("admin/user/edit");
            mav.addObject("user", new User());
            mav.addObject("companyList", companyList);
            mav.addObject("currentUser", currentUser);
            return mav;
        } else {
            attributes.addFlashAttribute("failure", "Not Authorized!");
            return new ModelAndView("redirect:/admin/user");
        }
    }

    @RequestMapping(value = "/admin/user/new", method = RequestMethod.POST)
    public String userNewPost(String username, String password, int companyId, RedirectAttributes attributes) throws ServiceException {
        User currentUser = userService.getSessionUser();
        if (currentUser.getCompany() == null) {  // super administrator only
            Optional<Company> company = companyService.getCompanyRepository().findById(companyId);
            if (!company.isPresent()) {
                attributes.addFlashAttribute("failure", "Company not found!");
            } else {
                User user = new User();
                user.setUsername(username);
                PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
                user.setPassword(encoder.encode(password));
                user.setCompany(company.get());
                userService.getUserRepository().save(user);
                attributes.addFlashAttribute("success", "User has been created successfully.");
            }
        } else {
            attributes.addFlashAttribute("failure", "Not Authorized!");
        }
        return "redirect:/admin/user";
    }

    @RequestMapping(value = "/admin/user/{id}", method = RequestMethod.GET)
    public ModelAndView userEditGet(@PathVariable int id, RedirectAttributes attributes) throws ServiceException {
        User currentUser = userService.getSessionUser();
        Optional<User> user = userService.getUserRepository().findById(id);
        if (!user.isPresent()) {
            attributes.addFlashAttribute("failure", "User not found!");
            return new ModelAndView("redirect:/admin/user");
        }
        if (currentUser.getCompany() == null || currentUser.getId() == user.get().getId()) {  // super administrator / self
            ModelAndView mav = new ModelAndView("admin/user/edit");
            mav.addObject("user", user.get());
            mav.addObject("currentUser", currentUser);
            return mav;
        } else {
            attributes.addFlashAttribute("failure", "Not Authorized!");
            return new ModelAndView("redirect:/admin/user");
        }
    }

    @RequestMapping(value = "/admin/user/{id}", method = RequestMethod.POST)
    public String userEditPost(@PathVariable int id, String password, RedirectAttributes attributes) throws ServiceException {
        User currentUser = userService.getSessionUser();
        Optional<User> user = userService.getUserRepository().findById(id);
        if (!user.isPresent()) {
            attributes.addFlashAttribute("failure", "User not found!");
        } else {
            if (currentUser.getCompany() == null || currentUser.getId() == user.get().getId()) {  // super administrator / self
                PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
                user.get().setPassword(encoder.encode(password));
                userService.getUserRepository().save(user.get());
                attributes.addFlashAttribute("success", "User has been updated successfully.");
            } else {
                attributes.addFlashAttribute("failure", "Not Authorized!");
            }
        }
        return "redirect:/admin/user";
    }

    @RequestMapping(value = "/admin/user/{id}/delete")
    public String userDelete(@PathVariable int id, RedirectAttributes attributes) throws ServiceException {
        User currentUser = userService.getSessionUser();
        Optional<User> user = userService.getUserRepository().findById(id);
        if (!user.isPresent()) {
            attributes.addFlashAttribute("failure", "User not found!");
        } else {
            if (currentUser.getCompany() == null || currentUser.getId() == user.get().getId()) {  // super administrator / the managed company
                userService.getUserRepository().delete(user.get());
                attributes.addFlashAttribute("success", "User has been deleted successfully.");
            } else {
                attributes.addFlashAttribute("failure", "Not Authorized!");
            }
        }
        return "redirect:/admin/user";
    }
}
