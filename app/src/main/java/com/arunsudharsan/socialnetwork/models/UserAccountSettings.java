package com.arunsudharsan.socialnetwork.models;

/**
 * Created by root on 14/12/17.
 */

public class UserAccountSettings {

    private String description;
    private String displayname;
    private long followers;
    private long following;
    private long posts;
    private String profilephoto;
    private String username;
    private String website;
    private String userid;

    public UserAccountSettings(String description, String displayname, long followers, long following, long posts, String profilephoto, String username, String website, String userid) {
        this.description = description;
        this.displayname = displayname;
        this.followers = followers;
        this.following = following;
        this.posts = posts;
        this.profilephoto = profilephoto;
        this.username = username;
        this.website = website;
        this.userid = userid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public UserAccountSettings() {
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDisplayname() {
        return displayname;
    }

    public void setDisplayname(String displayname) {
        this.displayname = displayname;
    }

    public long getFollowers() {
        return followers;
    }

    public void setFollowers(long followers) {
        this.followers = followers;
    }

    public long getFollowing() {
        return following;
    }

    public void setFollowing(long following) {
        this.following = following;
    }

    public long getPosts() {
        return posts;
    }

    public void setPosts(long posts) {
        this.posts = posts;
    }

    public String getProfilephoto() {
        return profilephoto;
    }

    public void setProfilephoto(String profilephoto) {
        this.profilephoto = profilephoto;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }
}
