package com.arunsudharsan.socialnetwork.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by root on 14/12/17.
 */

public class User  implements Parcelable{
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

    protected User(Parcel in) {
        userid = in.readString();
        phonenumber = in.readLong();
        email = in.readString();
        username = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(userid);
        parcel.writeLong(phonenumber);
        parcel.writeString(email);
        parcel.writeString(username);
    }
}
//data class User(val name: String, val email: String)