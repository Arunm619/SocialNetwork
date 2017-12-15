package com.arunsudharsan.socialnetwork.models;

import java.util.List;

/**
 * Created by root on 16/12/17.
 */

public class Comment {
    private String comment;
    private String userid;
    private List<Like> likes;
    private String datecreated;

    public Comment() {
    }

    @Override
    public String toString() {
        return "Comment{" +
                "comment='" + comment + '\'' +
                ", userid='" + userid + '\'' +
                ", likes=" + likes +
                ", datecreated='" + datecreated + '\'' +
                '}';
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public List<Like> getLikes() {
        return likes;
    }

    public void setLikes(List<Like> likes) {
        this.likes = likes;
    }

    public String getDatecreated() {
        return datecreated;
    }

    public void setDatecreated(String datecreated) {
        this.datecreated = datecreated;
    }

    public String getComment() {

        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Comment(String comment, String userid, List<Like> likes, String datecreated) {
        this.comment = comment;
        this.userid = userid;
        this.likes = likes;
        this.datecreated = datecreated;
    }
}
