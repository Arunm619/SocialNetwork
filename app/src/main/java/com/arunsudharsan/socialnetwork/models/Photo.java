package com.arunsudharsan.socialnetwork.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by root on 14/12/17.
 */

public class Photo implements Parcelable {
    private String caption;
    private String datecreated;
    private String imgpath;
    private String photoid;
    private String userid;
    private String tags;
    private List<Like> likes;

    public Photo(String caption, String datecreated, String imgpath, String photoid, String userid, String tags, List<Like> likes) {
        this.caption = caption;
        this.datecreated = datecreated;
        this.imgpath = imgpath;
        this.photoid = photoid;
        this.userid = userid;
        this.tags = tags;
        this.likes = likes;


    }

    public Photo() {
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getDatecreated() {
        return datecreated;
    }

    public void setDatecreated(String datecreated) {
        this.datecreated = datecreated;
    }

    public String getImgpath() {
        return imgpath;
    }

    public void setImgpath(String imgpath) {
        this.imgpath = imgpath;
    }

    public String getPhotoid() {
        return photoid;
    }

    public void setPhotoid(String photoid) {
        this.photoid = photoid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public List<Like> getLikes() {
        return likes;
    }

    public void setLikes(List<Like> likes) {
        this.likes = likes;
    }

    @Override
    public String toString() {
        return "Photo{" +
                "caption='" + caption + '\'' +
                ", datecreated='" + datecreated + '\'' +
                ", imgpath='" + imgpath + '\'' +
                ", photoid='" + photoid + '\'' +
                ", userid='" + userid + '\'' +
                ", tags='" + tags + '\'' +
                ", likes=" + likes +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

    }
}


