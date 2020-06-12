package com.example.socialnetwork.Model.Response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class GroupChatResponse {
    @SerializedName("_id")
    private String groupId;

    @SerializedName("name")
    private String groupName;

    @SerializedName("messages")
    private ArrayList<Message> messages;

    @SerializedName("users")
    private ArrayList<String> users;

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<Message> messages) {
        this.messages = messages;
    }

    public ArrayList<String> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<String> users) {
        this.users = users;
    }
}
