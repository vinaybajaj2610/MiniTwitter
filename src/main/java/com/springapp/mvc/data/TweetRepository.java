package com.springapp.mvc.data;

import com.springapp.mvc.model.Tweet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.security.Timestamp;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: prateek
 * Date: 22/7/13
 * Time: 12:47 PM
 * To change this template use File | Settings | File Templates.
 */

@Repository
public class TweetRepository {
    JdbcTemplate jdbcTemplate;

    @Autowired
    public TweetRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public long addTweet(long userid, String username, String details) {

        final SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate);
        insert.setTableName("tweets");
        insert.setColumnNames(Arrays.asList("userid", "username", "details"));
        insert.setGeneratedKeyName("tweetid");
        Map<String, Object> param = new HashMap<>();
        param.put("userid", userid);
        param.put("username", username);
        param.put("details", details);
        try{
            return  (Long) insert.executeAndReturnKey(param);
        }
        catch( DuplicateKeyException e){
            return -1;
        }
    }

    public List<Tweet> getTweets(long userid)
    {
        return jdbcTemplate.query("SELECT * FROM tweets WHERE userid=?", new Object[]{userid}, new BeanPropertyRowMapper<Tweet>(Tweet.class));
    }

    public List<Tweet> showHomePageTweets(long userid) {
        return jdbcTemplate.query("select tweets.username, tweets.timestamp, tweets.details from tweets where userid in (select userid from followers where followers.followerid = ?)", new Object[]{userid}, new BeanPropertyRowMapper<>(Tweet.class));
    }
}
