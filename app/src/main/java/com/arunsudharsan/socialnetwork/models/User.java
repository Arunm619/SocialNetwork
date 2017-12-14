package com.arunsudharsan.socialnetwork.models;

/**
 * Created by root on 14/12/17.
 */

public class User {
    private String userid;
    private long phonenumber;
    private String email;
    private String username;

    public User(String userid, long phonenumber, String email, String username) {
        this.userid = userid;
        this.phonenumber = phonenumber;
        this.email = email;
        this.username = username;
    }

    @Override
    public String toString() {
        return "User{" +
                "userid='" + userid + '\'' +
                ", phonenumber='" + phonenumber + '\'' +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                '}';
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public long getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(long phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public User() {
    }
}
