package com.example.socialnetwork.Model.Response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Profile {
    @SerializedName("username")
    private String username;

    @SerializedName("fullName")
    private String fullName;

    @SerializedName("address")
    private String address;

    @SerializedName("phone")
    private String phone;

    @SerializedName("birthday")
    private String birthday;

    @SerializedName("avatarUrl")
    private String avatarUrl;

    @SerializedName("coverPhoto")
    private ArrayList<String> coverPhoto;

    @SerializedName("postList")
    private ArrayList<Status> postList;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public ArrayList<String> getCoverPhoto() {
        return coverPhoto;
    }

    public void setCoverPhoto(ArrayList<String> coverPhoto) {
        this.coverPhoto = coverPhoto;
    }

    public ArrayList<Status> getPostList() {
        return postList;
    }

    public void setPostList(ArrayList<Status> postList) {
        this.postList = postList;
    }
}
