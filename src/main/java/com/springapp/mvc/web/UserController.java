package com.springapp.mvc.web;

import com.google.gson.Gson;
import com.springapp.mvc.data.UserRepository;
import com.springapp.mvc.model.DbUser;
import com.springapp.mvc.model.Tweet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.codec.Base64;
import org.springframework.security.core.codec.Hex;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.Principal;
import java.util.List;

/**
* Created with IntelliJ IDEA.
* User: vinay
* Date: 15/7/13
* Time: 2:15 PM
* To change this template use File | Settings | File Templates.
*/

@Controller
public class UserController {
    private final UserRepository repository;

    @Autowired
    public UserController(UserRepository repository) {
        this.repository = repository;
    }



    public String encryptUsingMd5(String password) throws Exception {
        String hash = null;
        try{
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(password.getBytes("UTF8"));
            byte[] raw = messageDigest.digest();
            hash = new String(Hex.encode(raw));
        }
        catch (Exception e){

        }
        return hash;
    }


    @RequestMapping(value = "/users/{username}", method = RequestMethod.GET)
    @ResponseBody
    public DbUser fetchUserInformation(@PathVariable("username") String username) {
        return repository.fetchUserByUsername(username);
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    public String CreateUser(@ModelAttribute("user") DbUser user, BindingResult result, HttpServletResponse response) throws Exception {
        if (result.hasErrors()){
            System.out.println("Error in binding!!");
        }

        long id = repository.addNewUser(user.getUsername(), user.getName(), user.getEmail(), encryptUsingMd5(user.getPassword()));

        if (id != -1){
            response.setStatus(201);
            response.setHeader("Location", "http://localhost:8080/users/"+ id);
        } else {
            response.setStatus(409);
        }
        return "";
    }

    @RequestMapping(value = "/followers", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public String fetchFollowers(HttpServletRequest request){
        Long userid = (Long) request.getSession().getAttribute("userid");
        List<DbUser> users = repository.fetchFollowers(userid);
        Gson gson = new Gson();
        String json = gson.toJson(users);
        System.out.println(json);
        return json;
    }

    @RequestMapping(value = "/following", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public String fetchFollowing(HttpServletRequest request){
        Long userid = (Long) request.getSession().getAttribute("userid");
        List<DbUser> users = repository.fetchFollowing(userid);
        Gson gson = new Gson();
        String json = gson.toJson(users);
        System.out.println(json);
        return json;
    }

    @RequestMapping(value = "/homepage", method = RequestMethod.GET)
    public String homePage(ModelMap modelMap){
        modelMap.addAttribute("username", SecurityContextHolder.getContext().getAuthentication().getName());
        return "homepage";
    }


}
