package com.springapp.mvc.apicontroller;

import com.google.gson.Gson;
import com.springapp.mvc.data.TweetRepository;
import com.springapp.mvc.data.UserRepository;
import com.springapp.mvc.model.DbUser;
import com.springapp.mvc.model.Tweet;
import com.springapp.mvc.service.Md5Encryption;
import org.springframework.beans.factory.annotation.Autowired;
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

    private final UserRepository userRepository;
    private final TweetRepository tweetRepository;

    @Autowired
    public apiUserController(UserRepository repository, TweetRepository tweetRepository) {
        this.userRepository = repository;
        this.tweetRepository = tweetRepository;
    }


    @RequestMapping(value = "/api/getRequestToken", method = RequestMethod.POST)
    @ResponseBody
    public String getRequestToken(@RequestParam("username") String username) {
        Long userid = userRepository.fetchUser(username).getUserid();
        String reqToken = UUID.randomUUID().toString();
        userRepository.addRequestToken(userid, reqToken);
        return reqToken;
    }

    @RequestMapping(value = "/api/authorizeUser", method = RequestMethod.GET)
    public String authorizeUserPage(@RequestParam("requestToken") String reqToken, ModelMap modelMap) {
        modelMap.addAttribute("reqToken",reqToken);
        return "authorize";
    }


    @RequestMapping(value = "/api/authorizeUser", method = RequestMethod.POST)
    public String authorizeUserWithToken(@RequestParam("username") String username, @RequestParam("password") String password,@RequestParam("requestToken") String reqToken) throws Exception {
        DbUser user = userRepository.fetchUser(username);
        Map<String, Object> map = userRepository.fetchUseridByRequestToken(reqToken);

        System.out.println(Md5Encryption.encryptUsingMd5(password) + " " + user.getPassword());
        Long userid = (Long) map.get("userid");
        if (Md5Encryption.encryptUsingMd5(password).equals(user.getPassword()) && userid.equals(user.getUserid())){
            String accessToken = UUID.randomUUID().toString();
            userRepository.addAccessToken(reqToken, accessToken);
            return "successpage";
        }
        return "errorpage";
    }

    @RequestMapping(value = "/api/getAccessToken", method = RequestMethod.POST)
    @ResponseBody
    public String getAccessToken(@RequestParam("requestToken") String reqToken) {
        Map<String, Object> map = userRepository.fetchUseridByRequestToken(reqToken);
        String accessToken = (String) map.get("accesstoken");
        Long userid = (Long) map.get("userid");
        int ret = userRepository.AddAccessTokenInUserAccessTable(userid, accessToken);
        if (ret == 1)
            return accessToken;
        else return "Access token already Given for this request token";
    }

    @RequestMapping(value = "/api/fetchUsersNewsFeed", method = RequestMethod.POST)
    @ResponseBody
    public List<Tweet> fetchUsersNewsFeed(@RequestHeader("accessToken") String accessToken) {
        Long userid = userRepository.fetchUseridByAccessToken(accessToken);
        return userRepository.fetchUsersNewsFeed(userid);
    }

    @RequestMapping(value = "/api/fetchFollowers", method = RequestMethod.POST)
    @ResponseBody
    public String fetchFollowers(@RequestParam("username") String username) {
        DbUser user = userRepository.fetchUser(username);
        Gson gson = new Gson();
        String json;
        if (user.getUserid()!=null){
            List<DbUser> users=  userRepository.fetchFollowers(user.getUserid());
            json = gson.toJson(users);
            return json;
        }
        json = gson.toJson("Username is not valid");
        return json;
    }

    @RequestMapping(value = "/api/fetchFollowing", method = RequestMethod.POST)
    @ResponseBody
    public String fetchFollowing(@RequestParam("username") String username) {
        DbUser follower = userRepository.fetchUser(username);
        Gson gson = new Gson();
        String json;
        if (follower.getUserid()!=null){
            List<DbUser> users=  userRepository.fetchFollowing(follower.getUserid());
            json = gson.toJson(users);
            return json;
        }
        json = gson.toJson("Username is not valid");
        return json;
    }

}
