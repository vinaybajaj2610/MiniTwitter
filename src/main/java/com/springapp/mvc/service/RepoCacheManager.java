package com.springapp.mvc.service;

import com.google.gson.Gson;
import com.springapp.mvc.model.DbUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: prateek
 * Date: 22/8/13
 * Time: 6:46 PM
 * To change this template use File | Settings | File Templates.
 */
@Service
public class RepoCacheManager {
    Jedis jedis;

    @Autowired
    public RepoCacheManager(Jedis jedis) {
        this.jedis = jedis;
    }

    public boolean exists(String key) {
        return jedis.exists(key);
    }

    public void delete(String key) {
        jedis.del(key);
    }

    public void setUsers(List<DbUser> users, String key) {
        Gson gson = new Gson();
        for(DbUser user : users) {
            String val = gson.toJson(user);
            jedis.rpush(key, val);
        }
    }

    public List<DbUser> fetchUsers(String key) {
        Gson gson = new Gson();
        List<DbUser> users = new ArrayList<>();
        long length = jedis.llen(key);
        for(long i=0; i<length; i++) {
            String val = jedis.lindex(key, i);
            users.add((int) i, gson.fromJson(val, DbUser.class));
        }
        return users;
    }

    public void addUser(DbUser user, String key) {
        Gson gson = new Gson();
        String val = gson.toJson(user);
        jedis.rpush(key, val);
    }
}
