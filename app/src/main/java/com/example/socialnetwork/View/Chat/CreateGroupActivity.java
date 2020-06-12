package com.example.socialnetwork.View.Chat;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.socialnetwork.Base.BaseActivity;
import com.example.socialnetwork.Context.RealmContext;
import com.example.socialnetwork.Model.Request.CreateGroupChatSendForm;
import com.example.socialnetwork.Model.Response.GroupChatResponse;
import com.example.socialnetwork.Model.Response.UserInfo;
import com.example.socialnetwork.Network.RetrofitService;
import com.example.socialnetwork.Network.RetrofitUtils;
import com.example.socialnetwork.R;
import com.example.socialnetwork.View.Chat.Adapter.FriendListAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateGroupActivity extends BaseActivity implements FriendListAdapter.OnFriendClickListener {
    @BindView(R.id.imv_back)
    ImageView imvBack;
    @BindView(R.id.imv_create_group)
    ImageView imvCreateGroup;
    @BindView(R.id.rcv_friend)
    RecyclerView rcvFriend;

    private ArrayList<UserInfo> friendList;
    private FriendListAdapter friendListAdapter;

    private UserInfo userInfo;
    RetrofitService retrofitService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);
        init();
        addListener();
        getAllUser();
    }


    private void init() {
        ButterKnife.bind(this);
        userInfo = RealmContext.getInstance().getUser();
        retrofitService = RetrofitUtils.getInstance().createService();

        friendList = new ArrayList<>();
        friendListAdapter = new FriendListAdapter(friendList,this);
        rcvFriend.setAdapter(friendListAdapter);
        rcvFriend.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,false));
    }
    private void addListener() {
        imvBack.setOnClickListener(v -> onBackPressed());
        imvCreateGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createGroupChat();
            }
        });
    }
    private void getAllUser() {
        retrofitService.getAllUser(userInfo.getId()).enqueue(new Callback<ArrayList<UserInfo>>() {
            @Override
            public void onResponse(Call<ArrayList<UserInfo>> call, Response<ArrayList<UserInfo>> response) {
                ArrayList<UserInfo> list = response.body();
                if (response.code() == 200 && list != null && !list.isEmpty()) {
                    friendList.clear();
                    friendList.addAll(list);
                    friendListAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<UserInfo>> call, Throwable t) {

            }
        });
    }
    private void createGroupChat() {
        CreateGroupChatSendForm sendForm = new CreateGroupChatSendForm(getFriendSelected());
        retrofitService.createGroupChat(sendForm).enqueue(new Callback<GroupChatResponse>() {
            @Override
            public void onResponse(Call<GroupChatResponse> call, Response<GroupChatResponse> response) {
                GroupChatResponse groupChatResponse = response.body();
                if (response.code() == 200 && groupChatResponse != null) {
                    showToast("Tạo thành công!");
                    goToChatActivity(groupChatResponse.getGroupId());
                    finish();
                } else {
                    showToast("Tạo thất bại!");
                }
            }

            @Override
            public void onFailure(Call<GroupChatResponse> call, Throwable t) {
                showToast("Tạo thất bại!");
            }
        });
    }

    private ArrayList<String> getFriendSelected() {
        ArrayList<String> list = new ArrayList<>();
        for (UserInfo friend: friendList){
            if (friend.isSelected()) list.add(friend.getId());
        }
        list.add(userInfo.getId());
        return list;
    }

    @Override
    public void onFriendClick() {
        boolean visible = false;
        for (UserInfo friend : friendList) {
            if (friend.isSelected()) {
                visible = true;
                break;
            }
        }
        if (visible) imvCreateGroup.setVisibility(View.VISIBLE);
        else imvCreateGroup.setVisibility(View.GONE);
    }
    private void goToChatActivity(String groupId) {
        Intent intent = new Intent(CreateGroupActivity.this, ChatActivity.class);
        intent.putExtra(ChatActivity.GROUP_ID, groupId);
        startActivity(intent);
    }
}