package com.example.socialnetwork.Network;

import com.example.socialnetwork.Model.Request.Avatar;
import com.example.socialnetwork.Model.Request.CommentSendForm;
import com.example.socialnetwork.Model.Request.CreateGroupChatSendForm;
import com.example.socialnetwork.Model.Request.CreateStatusSendForm;
import com.example.socialnetwork.Model.Request.LikePostSendForm;
import com.example.socialnetwork.Model.Request.LoginSendForm;
import com.example.socialnetwork.Model.Request.RegisterSendForm;
import com.example.socialnetwork.Model.Response.Comment;
import com.example.socialnetwork.Model.Response.GroupChat;
import com.example.socialnetwork.Model.Response.GroupChatResponse;
import com.example.socialnetwork.Model.Response.Message;
import com.example.socialnetwork.Model.Response.Status;
import com.example.socialnetwork.Model.Response.UserInfo;
import com.example.socialnetwork.Model.Response.UserProfile;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface RetrofitService {
    @POST(API.LOGIN)
    Call<UserInfo> login(@Body LoginSendForm sendForm);
    @POST(API.REGISTER)
    Call<UserInfo> register(@Body RegisterSendForm sendForm);

    @POST(API.CREATE_STATUS)
    Call<Status> createStatus(@Body CreateStatusSendForm sendForm);
    @GET(API.GET_ALL_POST)
    Call<ArrayList<Status>> getAllStatus (@Query("userId") String userId);
    @POST(API.LIKE_STATUS)
    Call<Void> likeStatus (@Body LikePostSendForm sendForm);

    @GET(API.GET_ALL_COMMENT)
    Call<ArrayList<Comment>> getAllComment(@Query("postId") String postId);
    @POST(API.COMMENT_STATUS)
    Call<Comment> commentStatus(@Body CommentSendForm sendForm);

    @POST(API.CREATE_GROUP_CHAT)
    Call<GroupChatResponse> createGroupChat (@Body CreateGroupChatSendForm sendform);
    @GET(API.GET_MY_GROUP)
    Call<ArrayList<GroupChat>> getAllGroupChat (@Query("userId") String userId);
    @GET(API.GET_ALL_MESSAGE)
    Call<ArrayList<Message>> getAllMessage (@Query("groupId") String groupId);
    @GET(API.GET_ALL_USER)
    Call<ArrayList<UserInfo>> getAllUser(@Query("userId") String userId);

    @GET(API.GET_PROFILE)
    Call<UserProfile> getProfile(@Query("username") String user, @Header("userId") String userId);


    @PUT(API.UPDATE_AVATAR)
    Call<Avatar> updateAvatar(@Query("userId") String userId, @Body Avatar sendForm);
}
