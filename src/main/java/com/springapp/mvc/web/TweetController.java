package com.springapp.mvc.web;

import com.springapp.mvc.data.TweetRepository;
import com.springapp.mvc.model.Tweet;
import com.springapp.mvc.model.DbUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

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

    @RequestMapping(value = "/tweets/{id}", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public List<Tweet> fetchTweets(@PathVariable("id") long userid) {
        return repository.getTweets(userid);
    }


    @RequestMapping(value = "/homepagetweets", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public List<Tweet> showHomePageTweets(HttpServletRequest request){
        long userid = (long) request.getSession().getAttribute("userid");

        return repository.showHomePageTweets(userid);
    }

}
