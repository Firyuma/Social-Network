package com.example.socialnetwork.Network;

public class API {
    public static final String ROOT = "https://hubbook.herokuapp.com/";


    // Router
    public static final String LOGIN = "api/user/login";
    public static final String REGISTER = "api/user/register";

    public static final String GET_ALL_POST = "api/post/get-all";
    public static final String LIKE_STATUS = "api/post/like";
    public static final String CREATE_STATUS = "api/post";

    public static final String GET_PROFILE = "api/user/get-detail";
    public static final String UPDATE_AVATAR = "/api/user/update-avatar";

    public static final String GET_MY_GROUP = "api/chat/all-group";
    public static final String GET_ALL_USER = "api/user/get-all";
    public static final String CREATE_GROUP_CHAT = "api/chat/create-group";
    public static final String GET_ALL_MESSAGE = "api/chat/all-message";

    public static final String GET_ALL_COMMENT = "/api/post/get-comment";
    public static final String COMMENT_STATUS = "/api/post/comment";



}
