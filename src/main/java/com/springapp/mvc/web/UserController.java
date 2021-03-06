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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import com.springapp.mvc.service.Md5Encryption;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
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
    public String CreateUser(@ModelAttribute("user") DbUser user, ModelMap modelMap, BindingResult result, HttpServletResponse response) throws Exception {
        if (result.hasErrors()){
            System.out.println("Error in binding!!");
        }

        long id = repository.addNewUser(user.getUsername(), user.getName(), user.getEmail(), Md5Encryption.encryptUsingMd5(user.getPassword()));

        if (id != -1){
            response.setStatus(201);
            modelMap.addAttribute("msg", "Successfuly registered. You can now login!");
        } else {
            response.setStatus(409);
            String error = "<font color=\"red\">" + "username " + user.getUsername() + " has already been taken. Choose a different username." + "</font>";
            modelMap.addAttribute("errormsg", error);
        }
        return "loginpage";
    }

    @RequestMapping(value = "/saveImage", method = RequestMethod.POST)
    public String handleUpload(
            @RequestParam(value = "file", required = false) MultipartFile multipartFile,
            HttpServletRequest request, ModelMap modelMap) {
        DbUser user =  repository.fetchUserByUsername((String) request.getSession().getAttribute("username"));
        String orgName = multipartFile.getOriginalFilename();

        String filePath = "/home/vinay/Downloads/twimages/" + request.getSession().getAttribute("userid") + ".jpg";
        File dest = new File(filePath);
        try {
            multipartFile.transferTo(dest);
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        modelMap.addAttribute("profile_id",user.getUserid());
        modelMap.addAttribute("profile_name", user.getName());
        modelMap.addAttribute("profile_username", user.getUsername());

        return "profile";
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
    public String homePage(ModelMap modelMap, HttpServletRequest request){
        modelMap.addAttribute("username", SecurityContextHolder.getContext().getAuthentication().getName());
        System.out.println( request.getSession().getAttribute("name"));
        modelMap.addAttribute("name", request.getSession().getAttribute("name"));
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
        if(!followerid.equals(userid)) {
            json = gson.toJson(repository.isFollower(userid, followerid));
        }
        else {
            System.out.println("own profile page");
            json = gson.toJson(2);
        }
        return json;
    }

    @RequestMapping(value = "/isFollowing", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public String isFollowing(@RequestParam(value="tagName") String tagName, HttpServletRequest request){
        Long followerid = (Long) request.getSession().getAttribute("userid");

        DbUser user =  repository.fetchUserByUsername(tagName);
        Gson gson = new Gson();
        if (user != null){
            String json = "";
            if(followerid != user.getUserid()) {
                Integer t = repository.isFollower(user.getUserid(), followerid);
                if(t.equals(1)){
                    json = gson.toJson(1);
                }
                else
                    json = gson.toJson(0);
            }
            else {
                json = gson.toJson(0);
            }
           return json;
        }
        return gson.toJson(0);
    }

    /////////Update profile///////////
    @RequestMapping(value = "/updateProfile", method = RequestMethod.POST)
    public String saveChanges(ModelMap modelMap, HttpServletRequest request) throws Exception {
        Long userid = (Long) request.getSession().getAttribute("userid");
        List<DbUser> users = repository.getPassword(userid);
        String userPassword = users.get(0).getPassword();
        String oldPassword = request.getParameter("oldPassword");
        String newPassword = request.getParameter("newPassword");
        String newPassword1 = request.getParameter("newPassword1");
        String email = request.getParameter("email");
        String name = request.getParameter("name");
        modelMap.addAttribute("name", name);
        modelMap.addAttribute("email", email);

        System.out.println(oldPassword);

        if(oldPassword==null || oldPassword.equals("")) {
            modelMap.addAttribute("msg", "Please Enter your password");
        }
        else if(!userPassword.equals(Md5Encryption.encryptUsingMd5(oldPassword)))  {
            modelMap.addAttribute("msg", "The old password entered is incorrect");
        }
        else if(!newPassword.equals(newPassword1)){
            modelMap.addAttribute("msg", "The new passwords do not match");
        }
        else if(newPassword==null || newPassword.equals("")) {
            request.getSession().setAttribute("name",name);
            repository.updateUser(userid, name, email);
            modelMap.addAttribute("msg", "The changes have been made");
        }
        else {
            request.getSession().setAttribute("name",name);
            repository.updateUser(userid, name, email, Md5Encryption.encryptUsingMd5(newPassword));
            modelMap.addAttribute("msg", "The changes have been made");
        }
        return "edit";
    }

    @RequestMapping(value = "/editProfile", method = RequestMethod.GET)
    public String editProfile(ModelMap modelMap, HttpServletRequest request){
        Long userid = (Long) request.getSession().getAttribute("userid");
        DbUser user = repository.fetchUserByUserid(userid);
        modelMap.addAttribute("name", user.getName());
        modelMap.addAttribute("email", user.getEmail());
        return "edit";
    }
    ///////////


    // auto complete stuff////////////
    @RequestMapping(value = "/loadUsernames", method = RequestMethod.GET)
    @ResponseBody
    public String loadUsernames(@RequestParam("term") String prefix){
        List<String> users = repository.loadUsernames(prefix);
        Gson gson = new Gson();
        String json = gson.toJson(users);
        //System.out.println(json);
        return json;
    }
    /////////////////

}
