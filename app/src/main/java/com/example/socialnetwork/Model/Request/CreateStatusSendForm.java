package com.example.socialnetwork.Model.Request;

import com.google.gson.annotations.SerializedName;

public class CreateStatusSendForm {

    @SerializedName("userId")
    private String userId;

    @SerializedName("content")
    private String content;

    public CreateStatusSendForm(String userId, String content) {
        this.userId = userId;
        this.content = content;
    }
}
