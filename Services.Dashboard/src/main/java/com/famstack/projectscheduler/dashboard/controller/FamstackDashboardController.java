package com.famstack.projectscheduler.dashboard.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.PathParam;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.famstack.projectscheduler.BaseFamstackService;
import com.famstack.projectscheduler.dashboard.manager.FamstackDashboardManager;
import com.famstack.projectscheduler.employees.bean.EmployeeDetails;
import com.famstack.projectscheduler.security.FamstackAuthenticationToken;
import com.famstack.projectscheduler.security.login.UserSecurityContextBinder;

@Controller
@SessionAttributes
public class FamstackDashboardController extends BaseFamstackService {

    @Resource
    FamstackDashboardManager FamstackDashboardManager;

    /** The authentication manager. */
    @Resource
    private AuthenticationManager authenticationManager;

    /** The user security context binder. */
    @Resource
    private UserSecurityContextBinder userSecurityContextBinder;

    /**
     * Request response login.
     *
     * @param request the request
     * @param reponses the reponses
     * @param model the model
     * @return the string
     */
    @RequestMapping("/{path}")
    public String login(@PathParam("path") String path, Model model) {
    	logDebug("Request path :" +  path);
        return path;
    }
    
    @RequestMapping(value = "/loginAjax", method = RequestMethod.POST)
    @ResponseBody
    public String loginAjax(@RequestParam("email") String username, @RequestParam("password") String password) {

        FamstackAuthenticationToken token = new FamstackAuthenticationToken(username, password);
        Authentication authentication = authenticationManager.authenticate(token);
        if (authentication.isAuthenticated()) {
            userSecurityContextBinder.bindUserAuthentication(authentication);
            return "{\"status\": true}";
        }

        return "{\"status\": false, \"error\": \"Bad Credentials\"}";
    }
    
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logOut(HttpServletRequest request) {
        userSecurityContextBinder.unbindUserAuthentication();
        request.getSession().invalidate();
        return "login";
    }
    
    @RequestMapping(value = "/newEmployee", method = RequestMethod.GET)
    public ModelAndView newEmployee() {
       return new ModelAndView("registerEmployee", "command", new EmployeeDetails());
    }
    
	@RequestMapping(value = "/createEmployee", method = RequestMethod.POST)
	public String createEmployee(@ModelAttribute("employeeDetails")
							EmployeeDetails employeeDetails, BindingResult result,  Model model) {
		
		System.out.println("First Name:" + employeeDetails.getFirstName() + 
					"Email:" + employeeDetails.getEmail());
		
		return "redirect:employees";
	}
    
}
