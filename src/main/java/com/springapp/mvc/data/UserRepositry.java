package com.springapp.mvc.data;

import com.springapp.mvc.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: vinay
 * Date: 15/7/13
 * Time: 2:10 PM
 * To change this template use File | Settings | File Templates.
 */

@Repository
public class UserRepositry {
    JdbcTemplate jdbcTemplate;

    @Autowired
    public UserRepositry(JdbcTemplate jdbcTemplate){
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

    public User fetchUser(long userid) {
        return jdbcTemplate.queryForObject("select * from users where userid = ?",
                new Object[]{userid}, new BeanPropertyRowMapper<>(User.class));

    }

    public User fetchUserByUsername(String username) {
        return jdbcTemplate.queryForObject("select * from users where username = ?",
                new Object[]{username}, new BeanPropertyRowMapper<>(User.class));
    }
}
