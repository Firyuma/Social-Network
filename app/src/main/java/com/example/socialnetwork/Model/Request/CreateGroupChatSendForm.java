package com.example.socialnetwork.Model.Request;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class CreateGroupChatSendForm {
    @SerializedName("users")
    private ArrayList<String> users;

    public CreateGroupChatSendForm(ArrayList<String> users) {
        this.users = users;
    }
}
