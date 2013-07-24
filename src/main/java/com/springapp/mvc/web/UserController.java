package com.springapp.mvc.web;

import com.springapp.mvc.data.UserRepository;
import com.springapp.mvc.model.DbUser;
import com.springapp.mvc.model.Tweet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.codec.Base64;
import org.springframework.security.core.codec.Hex;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
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

    @RequestMapping(value = "/users", method = RequestMethod.POST)
    @ResponseBody
    public String CreateUser(@RequestBody final DbUser user, HttpServletResponse response) throws Exception {

        long id = repository.addNewUser(user.getUsername(), user.getName(), user.getEmail(), encryptUsingMd5(user.getPassword()));

        if (id != -1){
            response.setStatus(201);
            response.setHeader("Location", "http://localhost:8080/users/"+ id);
        } else {
            response.setStatus(409);
        }
        return "";
    }

    @RequestMapping(value = "/followers/{id}", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public List<DbUser> fetchFollowers(@PathVariable("id") long userid){

       return repository.fetchFollowers(userid);
    }

    @RequestMapping(value = "/following/{id}", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public List<DbUser> fetchFollowing(@PathVariable("id") long userid){
        return repository.fetchFollowing(userid);
    }


}
