package com.example.socialnetwork.Model.Request;

import com.google.gson.annotations.SerializedName;

public class Avatar {
    @SerializedName("avatarUrl")
    private String avatarUrl;

    public Avatar(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
}
