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
import org.springframework.web.servlet.ModelAndView;
import com.springapp.mvc.service.Md5Encryption;
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


    @RequestMapping(value = "/users/{username}", method = RequestMethod.GET)
    @ResponseBody
    public DbUser fetchUserInformation(@PathVariable("username") String username) {
        return repository.fetchUserByUsername(username);
    }

    @RequestMapping(value="/{profile_username}", method = RequestMethod.GET)
    public ModelAndView showProfile(@PathVariable("profile_username") String profile_username, HttpSession httpSession) {
        ModelAndView model = new ModelAndView();
        DbUser user =  repository.fetchUserByUsername(profile_username);
        model.addObject("profile_id",user.getUserid()+"");
        model.addObject("profile_name",user.getName());
        model.addObject("profile_username",user.getUsername());
        //model.addObject("isfollowing", repository.fetchFollowing(repository.getSessionUserId(httpSession), user.getUserid()) + "");
        model.setViewName("/profile");
        return model;
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    public String CreateUser(@ModelAttribute("user") DbUser user, BindingResult result, HttpServletResponse response) throws Exception {
        if (result.hasErrors()){
            System.out.println("Error in binding!!");
        }

        long id = repository.addNewUser(user.getUsername(), user.getName(), user.getEmail(), Md5Encryption.encryptUsingMd5(user.getPassword()));

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

    @RequestMapping(value = "/followers/{userid}", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public String fetchFollowers(@PathVariable("userid") Long userid){
//        Long userid = (Long) request.getSession().getAttribute("userid");
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

    @RequestMapping(value = "/following/{userid}", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public String fetchFollowing(@PathVariable("userid") Long userid){
//        Long userid = (Long) request.getSession().getAttribute("userid");
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


    @RequestMapping(value = "/unfollow/{userid}", method = RequestMethod.GET)
    @ResponseBody
    public void unfollow(@PathVariable("userid") Long userid, HttpServletRequest request){
        Long followerid = (Long) request.getSession().getAttribute("userid");
        repository.unfollow(userid, followerid);
    }

    @RequestMapping(value = "/follow/{userid}", method = RequestMethod.GET)
    @ResponseBody
    public void follow(@PathVariable("userid") Long userid, HttpServletRequest request){
        Long followerid = (Long) request.getSession().getAttribute("userid");
        System.out.println(userid + " " + followerid);
        repository.follow(userid, followerid);
    }

    @RequestMapping(value = "/checkfollow/{userid}", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public String isFollower(@PathVariable("userid") Long userid, HttpServletRequest request){
        Long followerid = (Long) request.getSession().getAttribute("userid");
        Gson gson = new Gson();
        String json;
        if(followerid != userid) {
            json = gson.toJson(repository.isFollower(userid, followerid));
        }
        else {
            json = gson.toJson(2);
        }
        return json;
    }

}
