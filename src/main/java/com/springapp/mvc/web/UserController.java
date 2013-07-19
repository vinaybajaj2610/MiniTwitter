package com.springapp.mvc.web;

import com.springapp.mvc.data.UserRepositry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import com.springapp.mvc.model.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Created with IntelliJ IDEA.
 * User: vinay
 * Date: 15/7/13
 * Time: 2:15 PM
 * To change this template use File | Settings | File Templates.
 */

@Controller
public class UserController {
    private final UserRepositry repository;

    @Autowired
    public UserController(UserRepositry repository) {
        this.repository = repository;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String DisplayHomePage(HttpServletRequest request, ModelMap modelMap) {
        HttpSession httpSession = request.getSession();
        if (httpSession != null){
            if (httpSession.getAttribute("userid")!=null){
                modelMap.addAttribute("user",request.getSession().getAttribute("userid"));
                return "home";
            }
        }
        return "/login";
    }

    @RequestMapping(value = "/users/{id}", method = RequestMethod.GET)
    @ResponseBody
    public User fetch(@PathVariable("id") long userid) {
        return repository.fetchUser(userid);
    }

    @RequestMapping(value = "/users", method = RequestMethod.POST)
    @ResponseBody
    public String CreateUser(@RequestBody final User user, HttpServletResponse response){

        long id = repository.addNewUser(user.getUsername(), user.getName(), user.getEmail(), user.getPassword());

        if (id != -1){
            response.setStatus(201);
            response.setHeader("Location", "http://localhost:8080/users/"+ id);
        } else {
            response.setStatus(409);
        }
        return "";

    }
}
