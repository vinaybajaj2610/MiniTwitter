package com.springapp.mvc.web;

import com.springapp.mvc.data.UserRepositry;
import com.springapp.mvc.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
* Created with IntelliJ IDEA.
* User: root
* Date: 18/7/13
* Time: 5:24 PM
* To change this template use File | Settings | File Templates.
*/
@Controller
public class Sessioncontroller {

    private final UserRepositry repositry;

    @Autowired
    public Sessioncontroller(UserRepositry repositry) {
        this.repositry = repositry;
    }


    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String checkUser(HttpServletRequest request, ModelMap modelMap) {
        HttpSession session = request.getSession(false);
        if (session!=null){
            if (session.getAttribute("userid")!=null){
                return "redirect:/";
            }
        }
        modelMap.addAttribute("message","Login Page");
        return "login";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String loginPerson(HttpServletRequest request, HttpSession httpSession, ModelMap modelMap) {

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        if (username.isEmpty() || password.isEmpty()){
            modelMap.addAttribute("message","Put right information and not empty");
            return "login";
        }
        User user = repositry.fetchUserByUsername(username);
        if (user.getPassword().equals(password)){
            httpSession.setAttribute("userid", user.getUserid());
            return "redirect:/";
        }
        else {
            modelMap.addAttribute("message","login Fail");
            return "login";
        }

    }

    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public String logoutPerson(HttpServletRequest request, ModelMap modelMap) {
        HttpSession httpSession;
        httpSession = request.getSession(false);
        httpSession.invalidate();

        modelMap.addAttribute("message","logout Successful");
        return "login";

    }


}
