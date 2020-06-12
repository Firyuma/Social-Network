package com.example.socialnetwork.View.Home.Tab_chat;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.socialnetwork.Base.BaseFragment;
import com.example.socialnetwork.Context.RealmContext;
import com.example.socialnetwork.Model.Response.GroupChat;
import com.example.socialnetwork.Model.Response.UserInfo;
import com.example.socialnetwork.Network.RetrofitService;
import com.example.socialnetwork.Network.RetrofitUtils;
import com.example.socialnetwork.R;
import com.example.socialnetwork.View.Chat.ChatActivity;
import com.example.socialnetwork.View.Chat.CreateGroupActivity;
import com.example.socialnetwork.View.Home.Tab_chat.Adapter.GroupChatAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatFragment extends BaseFragment {
    public ChatFragment() {
    }

    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.view_flipper)
    ViewFlipper viewFlipper;
    @BindView(R.id.rcv_group_chat)
    RecyclerView rcvGroupChat;
    @BindView(R.id.fab_create_group)
    FloatingActionButton fabCreateGroup;

    private ArrayList<GroupChat> groupChatList;
    private GroupChatAdapter groupChatAdapter;

    private RetrofitService retrofitService = RetrofitUtils.getInstance().createService();
    private UserInfo userInfo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chat, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        init();

        fabCreateGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), CreateGroupActivity.class));
            }
        });

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getAllGroupChat();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getAllGroupChat();
    }

    private void init() {
        userInfo = RealmContext.getInstance().getUser();

        groupChatList = new ArrayList<>();
        groupChatAdapter = new GroupChatAdapter(groupChatList, new GroupChatAdapter.OnGroupChatClick() {
            @Override
            public void onGroupChatClick(String groupId) {
                goToChatActivity(groupId);
            }
        });
        rcvGroupChat.setAdapter(groupChatAdapter);
        rcvGroupChat.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
    }

    private void getAllGroupChat() {
        retrofitService.getAllGroupChat(userInfo.getId()).enqueue(new Callback<ArrayList<GroupChat>>() {
            @Override
            public void onResponse(Call<ArrayList<GroupChat>> call, Response<ArrayList<GroupChat>> response) {
                ArrayList<GroupChat> list = response.body();
                if (response.code() == 200 && list != null) {
                    groupChatList.clear();
                    groupChatList.addAll(list);
                    groupChatAdapter.notifyDataSetChanged();
                    viewFlipper.setDisplayedChild(3);
                } else {
                    viewFlipper.setDisplayedChild(1);
                }
                refreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<ArrayList<GroupChat>> call, Throwable t) {
                viewFlipper.setDisplayedChild(2);
                refreshLayout.setRefreshing(false);
            }
        });
    }

    private void goToChatActivity(String groupId) {
        Intent intent = new Intent(getActivity(), ChatActivity.class);
        intent.putExtra(ChatActivity.GROUP_ID, groupId);
        startActivity(intent);
    }
}
