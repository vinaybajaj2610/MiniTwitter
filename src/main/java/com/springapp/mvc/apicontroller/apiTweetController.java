package com.springapp.mvc.apicontroller;

import com.google.gson.Gson;
import com.springapp.mvc.data.TweetRepository;
import com.springapp.mvc.data.UserRepository;
import com.springapp.mvc.model.DbUser;
import com.springapp.mvc.model.Tweet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: vinay
 * Date: 5/8/13
 * Time: 7:30 PM
 * To change this template use File | Settings | File Templates.
 */
@Controller
public class apiTweetController {

    private final TweetRepository repository;
    private final UserRepository userRepository;

    @Autowired
    public apiTweetController(TweetRepository repository, UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    @RequestMapping(value = "/api/createPost", method = RequestMethod.POST)
    @ResponseBody
    public String createPost(@RequestBody Tweet tweet, HttpServletRequest request, HttpServletResponse response){
        response.setContentType("application/json");
        DbUser user = userRepository.fetchUser(tweet.getUsername());
        Long userid = userRepository.fetchUseridByAccessToken(request.getHeader("accessToken"));
        if ( user.getUserid()!= null && user.getUserid().equals(userid)){
            long newTweet = repository.addTweet(userid, tweet.getUsername(), tweet.getDetails());
            repository.jedisUpdateOnNewTweet(userid);
            response.setStatus(HttpServletResponse.SC_CREATED);
            return "Tweet Added successfully";
        }
        return "Tweet not Added, username is not valid";
    }

    @RequestMapping(value = "/api/fetchUsersPosts", method = RequestMethod.POST)
    @ResponseBody
    public String fetchUserPosts(@RequestParam("username") String username){
        DbUser user = userRepository.fetchUser(username);
        Gson gson = new Gson();
        String json;
        if (user.getUserid()!=null){
            List<Tweet> tweets=  repository.getTweets(user.getUserid());
            json = gson.toJson(tweets);
            return json;
        }
        json = gson.toJson("Username is not valid");
        return json;
    }

}
