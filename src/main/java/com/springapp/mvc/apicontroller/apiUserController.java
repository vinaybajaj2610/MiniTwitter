package com.springapp.mvc.apicontroller;

import com.springapp.mvc.data.UserRepository;
import com.springapp.mvc.model.DbUser;
import com.springapp.mvc.model.Tweet;
import com.springapp.mvc.service.Md5Encryption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created with IntelliJ IDEA.
 * User: vinay
 * Date: 1/8/13
 * Time: 6:51 PM
 * To change this template use File | Settings | File Templates.
 */

@Controller
public class apiUserController {

    private final UserRepository repository;

    @Autowired
    public apiUserController(UserRepository repository) {
        this.repository = repository;
    }


    @RequestMapping(value = "/api/getRequestToken", method = RequestMethod.POST)
    @ResponseBody
    public String getRequestToken(@RequestParam("username") String username) {
        Long userid = repository.fetchUser(username).getUserid();
        String reqToken = UUID.randomUUID().toString();
        repository.addRequestToken(userid, reqToken);
        return reqToken;
    }

    @RequestMapping(value = "/api/authorizeUser", method = RequestMethod.GET)
    public String authorizeUserPage(@RequestParam("requestToken") String reqToken, ModelMap modelMap) {
        modelMap.addAttribute("reqToken",reqToken);
        return "authorize";
    }


    @RequestMapping(value = "/api/authorizeUser", method = RequestMethod.POST)
    public String authorizeUserWithToken(@RequestParam("username") String username, @RequestParam("password") String password,@RequestParam("requestToken") String reqToken) throws Exception {
        DbUser user = repository.fetchUser(username);
        Map<String, Object> map = repository.fetchUseridByRequestToken(reqToken);

        System.out.println(Md5Encryption.encryptUsingMd5(password) + " " + user.getPassword());
        Long userid = (Long) map.get("userid");
        if (Md5Encryption.encryptUsingMd5(password).equals(user.getPassword()) && userid.equals(user.getUserid())){
            String accessToken = UUID.randomUUID().toString();
            repository.addAccessToken(reqToken, accessToken);
            return "successpage";
        }
        return "errorpage";
    }

    @RequestMapping(value = "/api/getAccessToken", method = RequestMethod.POST)
    @ResponseBody
    public String getAccessToken(@RequestParam("requestToken") String reqToken) {
        Map<String, Object> map = repository.fetchUseridByRequestToken(reqToken);
        String accessToken = (String) map.get("accesstoken");
        Long userid = (Long) map.get("userid");
        repository.AddAccessTokenInUserAccessTable(userid, accessToken);
        return accessToken;
    }

    @RequestMapping(value = "/api/fetchUsersNewsFeed", method = RequestMethod.POST)
    @ResponseBody
    public List<Tweet> fetchUsersNewsFeed(@RequestHeader("accessToken") String accessToken) {
        Long userid = repository.fetchUseridByAccessToken(accessToken);
        return repository.fetchUsersNewsFeed(userid);
    }
}
