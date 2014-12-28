package com.galy.lostandfound.database;

/**
 * Created by liuyuchen on 14/12/28.
 */
public class UserToken {
    public int _id;
    public String username;
    public String token;

    public UserToken(){

    }

    public UserToken(String username, String token){
        this.username = username;
        this.token = token;
    }
}
