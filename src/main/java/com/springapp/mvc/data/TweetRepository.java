package com.springapp.mvc.data;

import com.google.gson.Gson;
import com.springapp.mvc.model.Tweet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.Jedis;

import javax.swing.tree.RowMapper;
import java.security.Timestamp;
import java.sql.ResultSet;
import java.sql.SQLException;
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
    Jedis jedis;

    @Autowired
    public TweetRepository(JdbcTemplate jdbcTemplate, Jedis jedis) {
        this.jdbcTemplate = jdbcTemplate;
        this.jedis = jedis;
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
        return jdbcTemplate.query("SELECT * FROM tweets WHERE userid=? order by timestamp desc", new Object[]{userid}, new BeanPropertyRowMapper<Tweet>(Tweet.class));
    }

    public List<Tweet> getProfileTweets(Long userid, Long tweetid) {
        String cacheKey = userid + "newProfileTweetCount";
        String key = "getProfileTweets#" + userid + "#" + tweetid;
        Gson gson = new Gson();
        if(jedis.exists(key) && (!jedis.exists(cacheKey) || jedis.get(cacheKey).equals("0"))) {
            List<Tweet> tweets = new ArrayList<>();
            long length = jedis.llen(key);
            System.out.println(length);
            for(long i=0; i<length; i++) {
                String val = jedis.lindex(key, i);
                tweets.add((int) i, gson.fromJson(val, Tweet.class));
            }
            return tweets;
        }
        jedis.set(cacheKey, "0");
        List<Tweet> tweets;
        if(tweetid.equals((Long.valueOf(0)))) {
            tweets = jdbcTemplate.query("SELECT * FROM tweets WHERE userid=? order by tweetid desc limit 15", new Object[]{userid}, new BeanPropertyRowMapper<Tweet>(Tweet.class));
        }
        else {
            tweets =  jdbcTemplate.query("SELECT * FROM tweets WHERE userid=? and tweetid < ? order by tweetid desc limit 15", new Object[]{userid, tweetid}, new BeanPropertyRowMapper<Tweet>(Tweet.class));
        }
        for(Tweet tweet : tweets) {
            String val = gson.toJson(tweet);
            jedis.rpush(key, val);
        }
        jedis.expire(key, 60*10);
        return tweets;
    }



    public List<Tweet> showHomePageTweets(long userid, Long tweetid) {
        String cacheKey = userid + "newHomeTweetCount";
        String key = "showHomePageTweets#" + userid + "#" + tweetid;

        Gson gson = new Gson();
        if(jedis.exists(key) && (!jedis.exists(cacheKey) || jedis.get(cacheKey).equals("0"))) {
            System.out.println("cacheing working ... Tweets cached");
            List<Tweet> tweets = new ArrayList<>();
            long length = jedis.llen(key);
            System.out.println(length);
            for(long i=0; i<length; i++) {
                String val = jedis.lindex(key, i);
                tweets.add((int) i, gson.fromJson(val, Tweet.class));
            }
            return tweets;
        }
        jedis.set(cacheKey, "0");
        List<Tweet> tweets;
        if(tweetid.equals(Long.valueOf(0)))
        {
            tweets = jdbcTemplate.query("select tweets.userid, tweets.username, tweets.timestamp, tweets.details, tweets.tweetid from tweets, followers where followers.followerid = ? and tweets.userid=followers.userid and tweets.timestamp < followers.timestamp order by tweets.tweetid desc limit 15 /*offset ?*/", new Object[]{userid}, new BeanPropertyRowMapper<>(Tweet.class));
        }
        else {
            tweets = jdbcTemplate.query("select tweets.userid, tweets.username, tweets.timestamp, tweets.details, tweets.tweetid from tweets, followers where followers.followerid = ? and tweets.tweetid < ? and tweets.userid=followers.userid and tweets.timestamp < followers.timestamp order by tweets.tweetid desc limit 10/*offset ?*/", new Object[]{userid, tweetid}, new BeanPropertyRowMapper<>(Tweet.class));
        }

        for(Tweet tweet : tweets) {
            String val = gson.toJson(tweet);
            jedis.rpush(key, val);
        }
        return tweets;
    }

   /* public void jedisUpdateCountZero(Long userid) {
        String cacheKey = userid + "newHomeTweetCount";
        jedis.set(cacheKey, "0");
    }*/

    public List<Tweet> fetchNewFreshTweets(Long userid, Long tweetid) {
        // panga new tweets fetch hui then update cache
        List<Tweet> tweets = jdbcTemplate.query("select tweets.userid, tweets.username, tweets.timestamp, tweets.details, tweets.tweetid from tweets, followers where followers.followerid = ? and tweets.tweetid > ? and tweets.userid=followers.userid and tweets.timestamp < followers.timestamp order by tweets.tweetid desc", new Object[]{userid, tweetid}, new BeanPropertyRowMapper<>(Tweet.class));
        String key = "showHomePageTweets#" + userid + "#" + 0;
        //String profileKey = "showHomePageTweets#" + userid + "#" + 0;
        jedis.del(key);
        return tweets;
    }


    public void jedisUpdateOnNewTweet(Long userid) {
        List<Long> ids = null;
        String key = userid + "newProfileTweetCount";
        jedis.incr(key);
        try {
            ids = jdbcTemplate.queryForList("select followerid from followers where userid = ?", new Object[]{userid}, Long.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        for(Long id: ids) {
            key = id+"newHomeTweetCount";
            jedis.incr(key);
        }
    }

    // api for fetching user tweets
    public List<Tweet> getUserPosts(long userid)
    {
        return jdbcTemplate.query("SELECT * FROM tweets WHERE userid=? order by timestamp desc limit 50", new Object[]{userid}, new BeanPropertyRowMapper<Tweet>(Tweet.class));
    }

    public String getNewTweetsCount(Long userid) {
        String key = userid + "newHomeTweetCount";
        if(jedis.exists(key)) {
            return jedis.get(key);
        }
        else return "0";
    }

}
