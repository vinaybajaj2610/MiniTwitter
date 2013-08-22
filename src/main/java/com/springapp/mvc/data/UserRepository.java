package com.springapp.mvc.data;

import com.springapp.mvc.autocomplete.Trie;
import com.springapp.mvc.model.DbUser;
import com.springapp.mvc.model.Tweet;
import org.springframework.beans.factory.annotation.Autowired;
import com.springapp.mvc.service.RepoCacheManager;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.Jedis;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: vinay
 * Date: 15/7/13
 * Time: 2:10 PM
 * To change this template use File | Settings | File Templates.
 */

@Repository
public class UserRepository {
    JdbcTemplate jdbcTemplate;
    RepoCacheManager cacheManager;
    Jedis jedis;
    Trie tree;

    @Autowired
    public UserRepository(JdbcTemplate jdbcTemplate, Jedis jedis){
        this.jdbcTemplate = jdbcTemplate;
        tree = new Trie();
        this.jedis = jedis;
        List<DbUser> users = this.jdbcTemplate.query("select * from triedata()", new BeanPropertyRowMapper<>(DbUser.class));
        for (int i = 0; i < users.size(); i++){
            tree.insertWord(tree.root, users.get(i).getUsername());
        }
    }

    @Autowired
    public void setCacheManager(RepoCacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }


    public long addNewUser(String username, String name, String email, String password) {

        final SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate);
        insert.setTableName("Users");
        insert.setColumnNames(Arrays.asList("username", "name", "email", "password"));
        insert.setGeneratedKeyName("userid");
        Map<String, Object> param = new HashMap<>();
        param.put("username", username);
        param.put("name", name);
        param.put("email", email);
        param.put("password", password);
        try{
            tree.insertWord(tree.root, username);
            return  (Long) insert.executeAndReturnKey(param);
        }
        catch( DuplicateKeyException e){
            return -1;
        }

    }

    public DbUser fetchUser(String username) {
        try{
        return jdbcTemplate.queryForObject("select * from fetchuser(?)",
                new Object[]{username}, new BeanPropertyRowMapper<>(DbUser.class));
        }
        catch (Exception e){
            //System.out.println(e.getMessage());
             return null;
        }
    }

    public DbUser fetchUserByUsername(String username) {
        try {
            return jdbcTemplate.queryForObject("select * from fetch_user_by_username (?)",
                new Object[]{username}, new BeanPropertyRowMapper<>(DbUser.class));
        } catch (Exception e) {
           // System.out.println(e.getMessage());
            return null;
        }
    }

    public DbUser fetchUserByUserid(Long userid) {
        return jdbcTemplate.queryForObject("select * from fetch_user_by_userid(?)",
                new Object[]{userid}, new BeanPropertyRowMapper<>(DbUser.class));
    }

    public List<DbUser> fetchFollowers(long userid) {
        String key = "fetchFollowers#" + userid;
        if(cacheManager.exists(key)) {
            System.out.println("followers cache working");
            return cacheManager.fetchUsers(key);
        }
        System.out.println("From  db followers");
        java.util.Date date= new java.util.Date();
        Timestamp timestamp = new Timestamp(date.getTime());
        List<DbUser> users = jdbcTemplate.query("select users.userid, users.username, users.email from users, followers where followers.userid = ? and users.userid=followers.followerid and timestamp > ?", new Object[]{userid, timestamp}, new BeanPropertyRowMapper<>(DbUser.class));
        cacheManager.setUsers(users, key);
        return users;
    }

    public List<DbUser> fetchFollowing(long followerid) {
        String key = "fetchFollowing#" + followerid;
        if(cacheManager.exists(key)) {
            System.out.println("following cache working");
            return cacheManager.fetchUsers(key);
        }
        System.out.println("From db following");
        java.util.Date date= new java.util.Date();
        Timestamp timestamp = new Timestamp(date.getTime());
        List<DbUser> users = jdbcTemplate.query("select users.userid, users.username, users.email from users, followers where followers.followerid = ? and users.userid=followers.userid and timestamp > ?", new Object[]{followerid, timestamp}, new BeanPropertyRowMapper<>(DbUser.class));
        cacheManager.setUsers(users, key);
        return users;
    }
    public void follow(Long userid, Long followerid) {
        jdbcTemplate.update("DELETE from followers  WHERE userid=? and followerid=?", new Object[]{userid, followerid});
        final SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate);
        insert.setTableName("followers");
        insert.setColumnNames(Arrays.asList("userid", "followerid"));
        Map<String, Object> param = new HashMap<>();
        param.put("userid", userid);
        param.put("followerid",followerid);
        insert.execute(param);
        String key = "fetchFollowers#" + userid;
        DbUser user;
        try {
            if(cacheManager.exists(key)) {
                user = jdbcTemplate.queryForObject("select users.userid, users.username, users.email from users where userid = ?",new Object[]{followerid}, new BeanPropertyRowMapper<>(DbUser.class));
                cacheManager.addUser(user, key);
            }
            key = "fetchFollowing#" + followerid;
            if(cacheManager.exists(key)) {
                user = jdbcTemplate.queryForObject("select users.userid, users.username, users.email from users where userid = ?",new Object[]{userid}, new BeanPropertyRowMapper<>(DbUser.class));
                cacheManager.addUser(user, key);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        key = "showHomePageTweets#" + followerid + "#" + 0;
        System.out.println(key + "by follow");
        jedis.del(key);
    }

    public int unfollow(Long userid, Long followerid) {
        String key = "fetchFollowers#" + userid;
        cacheManager.delete(key);
        key = "fetchFollowing#" + followerid;
        cacheManager.delete(key);
        java.util.Date date= new java.util.Date();
        Timestamp timestamp = new Timestamp(date.getTime());
        return jdbcTemplate.update("UPDATE followers SET timestamp = ? WHERE userid=? and followerid=?", new Object[]{timestamp, userid, followerid});
    }


    public Integer isFollower(Long userid, Long followerid) {
        java.util.Date date= new java.util.Date();
        Timestamp timestamp = new Timestamp(date.getTime());
        int count = jdbcTemplate.queryForInt("select count(*) from followers where userid = ? and followerid=? and timestamp > ?", new Object[]{userid, followerid, timestamp});
        if(count>0) {
            return 1;
        }
        return 0;
    }


    public void addRequestToken(Long userid, String reqToken) {
        final SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate);
        insert.setTableName("token");
        insert.setColumnNames(Arrays.asList("userid", "requesttoken"));
        Map<String, Object> param = new HashMap<>();
        param.put("userid", userid);
        param.put("requesttoken",reqToken);
        insert.execute(param);
    }



    public Map<String,Object> fetchUseridByRequestToken(String reqToken) {
        return jdbcTemplate.queryForMap("select * from token where requesttoken = ?", new Object[]{reqToken});
    }

    public String fetchAccessToken(String reqToken) {
        return (String)jdbcTemplate.queryForObject("select accesstoken from token where requesttoken = ?",
                new Object[]{reqToken}, String.class);
    }

    public Long fetchUseridByAccessToken(String token) {
        try {
            return jdbcTemplate.queryForObject("select userid from useraccesstoken where accesstoken = ?",new Object[]{token}, Long.class);
        }
        catch (Exception e) {
            return null;
        }
    }

    public void addAccessToken(String reqToken, String accessToken) {
        try{
            jdbcTemplate.update("UPDATE token SET accesstoken = ? WHERE requesttoken = ?", new Object[]{accessToken, reqToken});
        }
        catch (Exception e){

        }
    }

    public int AddAccessTokenInUserAccessTable(Long userid, String accessToken) {
        final SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate);
        insert.setTableName("useraccesstoken");
        insert.setColumnNames(Arrays.asList("userid", "accesstoken"));
        Map<String, Object> param = new HashMap<>();
        param.put("userid", userid);
        param.put("accesstoken",accessToken);
        try{
            insert.execute(param);
            return 1;
        }
        catch (Exception e){
              return -1;
        }
    }


    // api method which fetch top 50 tweets from news feed
    public List<Tweet> fetchUsersNewsFeed(Long userid) {
        return jdbcTemplate.query("select tweets.username, tweets.timestamp, tweets.details from tweets, followers where followers.followerid = ? and tweets.userid=followers.userid and tweets.timestamp < followers.timestamp order by tweets.timestamp desc limit 50", new Object[]{userid}, new BeanPropertyRowMapper<>(Tweet.class));
    }


    ////Update User/////
    public void updateUser(Long userid, String name, String email) {
        jdbcTemplate.update("UPDATE users SET name = ?, email = ? WHERE userid=?", new Object[]{name, email, userid});
    }

    public void updateUser(Long userid, String name, String email, String newPassword) {
        jdbcTemplate.update("UPDATE users SET name = ?, email = ?, password = ? WHERE userid=?", new Object[]{name, email, newPassword, userid});
    }

    public List<DbUser> getPassword(Long userid) {
        return jdbcTemplate.query("select userid, password from users where userid = ?", new Object[]{userid}, new BeanPropertyRowMapper<>(DbUser.class));
    }

    ///auto complete stuff////////////
    public List<String> loadUsernames(String prefix) {
        return tree.getAllPrefixMatches(prefix);


    }
    //////////////

}
