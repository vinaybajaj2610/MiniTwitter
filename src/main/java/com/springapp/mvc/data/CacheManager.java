package com.springapp.mvc.data;

import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;

/**
 * Created with IntelliJ IDEA.
 * User: vinay
 * Date: 16/8/13
 * Time: 2:14 PM
 * To change this template use File | Settings | File Templates.
 */
public class CacheManager {
    Jedis jedis;

    @Autowired
    public CacheManager(Jedis jedis) {
        this.jedis = jedis;
    }

    public String getValue(String key){
        return jedis.get(key);
        //jedis.lrange(key,0,jedis.llen(key));
    }

    public void setValue(String key, String value){
        jedis.set(key, value);
    }

    public boolean exists(String key){
        return jedis.exists(key);
    }

}
