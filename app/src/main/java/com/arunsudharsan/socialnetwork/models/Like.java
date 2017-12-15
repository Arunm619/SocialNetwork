package com.arunsudharsan.socialnetwork.models;

/**
 * Created by root on 15/12/17.
 */

public class Like {

private String userid;

    public Like(String userid) {
        this.userid = userid;
    }

    public Like() {
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    @Override
    public String toString() {
        return "Like{" +
                "userid='" + userid + '\'' +
                '}';
    }
}
