package com.springapp.mvc.model;

import java.security.Timestamp;

/**
 * Created with IntelliJ IDEA.
 * User: root
 * Date: 18/7/13
 * Time: 2:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class Tweet {
    public long tweetid;
    public long userid;
    public String username;
    public Timestamp timestamp;
    public String details;


    public long getTweetid() {
        return tweetid;
    }

    public long getUserid() {
        return userid;
    }

    public String getUsername() {
        return username;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public String getDetails() {
        return details;
    }

    public void setTweetid(long tweetid) {
        this.tweetid = tweetid;
    }

    public void setUserid(long userid) {
        this.userid = userid;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
