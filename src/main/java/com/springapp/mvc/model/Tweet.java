package com.springapp.mvc.model;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.sql.Timestamp;

/**
 * Created with IntelliJ IDEA.
 * User: root
 * Date: 18/7/13
 * Time: 2:10 PM
 * To change this template use File | Settings | File Templates.
 */

@JsonSerialize(include= JsonSerialize.Inclusion.NON_NULL)
public class Tweet {
    public Long tweetid;
    public Long userid;
    public String username;
    public Timestamp timestamp;
    public String details;


    public Long getTweetid() {
        return tweetid;
    }

    public Long getUserid() {
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

    public void setTweetid(Long tweetid) {
        this.tweetid = tweetid;
    }

    public void setUserid(Long userid) {
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
