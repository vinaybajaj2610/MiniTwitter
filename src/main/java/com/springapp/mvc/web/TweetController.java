package com.springapp.mvc.web;

import com.springapp.mvc.data.TweetRepository;
import com.springapp.mvc.model.Tweet;
import com.springapp.mvc.model.DbUser;
import com.springapp.mvc.model.Tweet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import com.google.gson.Gson;



import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: prateek
 * Date: 22/7/13
 * Time: 12:46 PM
 * To change this template use File | Settings | File Templates.
 */

@Controller
public class TweetController {

    private final TweetRepository repository;

    @Autowired
    public TweetController(TweetRepository repository) {
        this.repository = repository;
    }

    @RequestMapping(value = "/tweets", method = RequestMethod.POST)
    @ResponseBody
    public String createTweet(@RequestBody final Tweet tweet, HttpServletResponse response){

        long id = repository.addTweet(tweet.getUserid(), tweet.getUsername(), tweet.getDetails());
        return "";
    }

    @RequestMapping(value = "/users/tweets/{id}", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public List<Tweet> fetchTweets(@PathVariable("id") long userid) {
        return repository.getTweets(userid);
    }


    @RequestMapping(value = "/homepagetweets", method = RequestMethod.GET)
    @ResponseBody
    public String showHomePageTweets(@RequestParam(value="page") long page ,HttpServletRequest request){
        Long userid = (Long) request.getSession().getAttribute("userid");
        List<Tweet> tweets = repository.showHomePageTweets(userid, page);
        Gson gson = new Gson();
        String json = gson.toJson(tweets);
        return json;
    }

    @RequestMapping(value = "/tweets/{userid}", method = RequestMethod.GET)
    @ResponseBody
    public String showUserTweets(@PathVariable("userid") Long userid){
//        Long userid = (Long) request.getSession().getAttribute("userid");
        List<Tweet> tweets = repository.getTweets(userid);
        Gson gson = new Gson();
        String json = gson.toJson(tweets);
        return json;
    }


    @RequestMapping(value = "/addtweet", method = RequestMethod.POST)
    @ResponseBody
    public void addTweet(@RequestBody Tweet tweet, HttpServletRequest request) throws Exception {
        HttpSession httpSession = request.getSession();
        Long userid = (Long)httpSession.getAttribute("userid");
        String username = (String) httpSession.getAttribute("username");
        long id = repository.addTweet(userid, username, tweet.getDetails());
    }

}
