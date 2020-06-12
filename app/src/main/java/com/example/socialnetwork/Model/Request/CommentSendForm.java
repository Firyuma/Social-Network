package com.example.socialnetwork.Model.Request;

import com.google.gson.annotations.SerializedName;

public class CommentSendForm {
    @SerializedName("postId")
    private String postId;

    @SerializedName("userId")
    private String userId;

    @SerializedName("content")
    private String content;

    public CommentSendForm(String userId, String postId, String content) {
        this.postId = postId;
        this.userId = userId;
        this.content = content;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
