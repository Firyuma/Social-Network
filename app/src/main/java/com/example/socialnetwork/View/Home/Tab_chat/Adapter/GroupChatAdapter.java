package com.example.socialnetwork.View.Home.Tab_chat.Adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.socialnetwork.Context.RealmContext;
import com.example.socialnetwork.Model.Response.GroupChat;
import com.example.socialnetwork.Model.Response.UserInfo;
import com.example.socialnetwork.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class GroupChatAdapter extends RecyclerView.Adapter<GroupChatAdapter.ViewHolder> {

    public interface OnGroupChatClick {
        void onGroupChatClick(String groupId);
    }

    private ArrayList<GroupChat> groupChatList;
    private UserInfo userInfo;
    private OnGroupChatClick listener;

    public GroupChatAdapter(ArrayList<GroupChat> groupChatList, OnGroupChatClick listener) {
        this.groupChatList = groupChatList;
        this.listener = listener;
        userInfo = RealmContext.getInstance().getUser();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_group_chat, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindData(groupChatList.get(position));
    }

    @Override
    public int getItemCount() {
        return groupChatList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.view_flipper)
        ViewFlipper viewFlipper;
        @BindView(R.id.imv_avatar)
        CircleImageView imvAvatar;
        @BindView(R.id.imv_avatar_1)
        CircleImageView imvAvatar1;
        @BindView(R.id.imv_avatar_2)
        CircleImageView imvAvatar2;
        @BindView(R.id.txt_group_name)
        TextView txtGroupName;
        @BindView(R.id.txt_last_message)
        TextView txtLastMessage;

        private GroupChat groupChat;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onGroupChatClick(groupChat.getGroupId());
                }
            });
        }

        private void bindData(GroupChat groupChat) {
            this.groupChat = groupChat;

            String groupName = "";

            ArrayList<UserInfo> users = groupChat.getUsers();
            if (users.size() > 2) {
                viewFlipper.setDisplayedChild(1);
                boolean loadImv1Done = false;
                for (UserInfo user : users) {
                    if (!user.getId().equals(userInfo.getId())) {
                        if (!loadImv1Done) {
                            Glide.with(itemView.getContext()).load(user.getAvatar()).into(imvAvatar1);

                            String[] names = user.getFullName().split(" ");
                            groupName += names[names.length - 1];
                            loadImv1Done = true;
                        } else {
                            Glide.with(itemView.getContext()).load(user.getAvatar()).into(imvAvatar2);
                            String[] names = user.getFullName().split(" ");
                            groupName += ", " + names[names.length - 1];
                            break;
                        }
                    }
                }
                if (users.size() > 3) {
                    groupName += ", +" + (users.size() - 3) + " others";
                }
            } else {
                viewFlipper.setDisplayedChild(0);
                for (UserInfo user : users) {
                    if (!user.getId().equals(userInfo.getId())) {
                        Glide.with(itemView.getContext()).load(user.getAvatar()).into(imvAvatar);
                        groupName = user.getFullName();
                        break;
                    }
                }
            }
            if (TextUtils.isEmpty(groupChat.getGroupName())) {
                txtGroupName.setText(groupName);
            } else {
                txtGroupName.setText(groupChat.getGroupName());
            }
            txtLastMessage.setText(groupChat.getLastMessage());
        }
    }
}