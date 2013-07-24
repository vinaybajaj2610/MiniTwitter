package com.springapp.mvc.model;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 * Created with IntelliJ IDEA.
 * User: vinay
 * Date: 15/7/13
 * Time: 2:06 PM
 * To change this template use File | Settings | File Templates.
 */

@JsonSerialize(include= JsonSerialize.Inclusion.NON_NULL)
public class DbUser {


    public Long userid;
    public String username;
    public String name;
    public String email;
    public String password;


    public Long getUserid() {
        return userid;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public void setUserid(Long userid) {
        this.userid = userid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
