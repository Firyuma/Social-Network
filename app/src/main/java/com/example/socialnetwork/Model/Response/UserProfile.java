package com.example.socialnetwork.Model.Response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class UserProfile {
    @SerializedName("username")
    private String username;

    @SerializedName("fullName")
    private String fullName;
    @SerializedName("avatarUrl")
    private String avatarUrl;

    @SerializedName("address")
    private String address;
    @SerializedName("phone")
    private String phone;
    @SerializedName("birthday")
    private String birthday;

    @SerializedName("coverPhoto")
    private String[] coverPhoto;

    @SerializedName("postList")
    private ArrayList<Status> statuses;

    public UserProfile(String username, String fullName, String avatarUrl, String address, String phone,
                       String birthday, String[] coverPhoto, ArrayList<Status> statuses) {
        this.username = username;
        this.fullName = fullName;
        this.avatarUrl = avatarUrl;
        this.address = address;
        this.phone = phone;
        this.birthday = birthday;
        this.coverPhoto = coverPhoto;
        this.statuses = statuses;
    }

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

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
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

    public String[] getCoverPhoto() {
        return coverPhoto;
    }

    public void setCoverPhoto(String[] coverPhoto) {
        this.coverPhoto = coverPhoto;
    }

    public ArrayList<Status> getStatuses() {
        return statuses;
    }

    public void setStatuses(ArrayList<Status> statuses) {
        this.statuses = statuses;
    }
}
