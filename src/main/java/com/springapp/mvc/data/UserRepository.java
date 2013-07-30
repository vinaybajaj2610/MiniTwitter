package com.springapp.mvc.data;

import com.springapp.mvc.model.DbUser;
import com.springapp.mvc.model.Tweet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

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

    @Autowired
    public UserRepository(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
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
            return  (Long) insert.executeAndReturnKey(param);
        }
        catch( DuplicateKeyException e){
            return -1;
        }

    }

    public DbUser fetchUser(String username) {
        return jdbcTemplate.queryForObject("select * from users where username = ?",
                new Object[]{username}, new BeanPropertyRowMapper<>(DbUser.class));
    }

    public DbUser fetchUserByUsername(String username) {
        return jdbcTemplate.queryForObject("select users.userid, users.username, users.email from users where username = ?",
                new Object[]{username}, new BeanPropertyRowMapper<>(DbUser.class));
    }

    public List<DbUser> fetchFollowers(long userid) {
        java.util.Date date= new java.util.Date();
        Timestamp timestamp = new Timestamp(date.getTime());
        return jdbcTemplate.query("select users.userid, users.username from users, followers where followers.userid = ? and users.userid=followers.followerid and timestamp > ?", new Object[]{userid, timestamp}, new BeanPropertyRowMapper<>(DbUser.class));
    }

    public List<DbUser> fetchFollowing(long followerid) {
        java.util.Date date= new java.util.Date();
        Timestamp timestamp = new Timestamp(date.getTime());
        return jdbcTemplate.query("select users.userid, users.username from users, followers where followers.followerid = ? and users.userid=followers.userid and timestamp > ?", new Object[]{followerid, timestamp}, new BeanPropertyRowMapper<>(DbUser.class));
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
    }

    public int unfollow(Long userid, Long followerid) {
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


}
