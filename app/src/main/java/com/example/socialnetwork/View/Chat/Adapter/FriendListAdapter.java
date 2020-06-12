package com.example.socialnetwork.View.Chat.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.socialnetwork.Model.Response.UserInfo;
import com.example.socialnetwork.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class FriendListAdapter extends RecyclerView.Adapter<FriendListAdapter.ViewHolder>  {
    public interface OnFriendClickListener{
        void onFriendClick();
    }
    private List<UserInfo> friendList;
    private OnFriendClickListener listener;
    public FriendListAdapter(List<UserInfo> friendList, OnFriendClickListener listener){
        this.friendList = friendList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friend_chat,parent,false);
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return friendList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.imv_avatar)
        CircleImageView imvAvatar;
        @BindView(R.id.txt_full_name)
        TextView txtFullName;
        @BindView(R.id.imv_add)
        ImageView imvAdd;
        @BindView(R.id.imv_check)
        ImageView imvCheck;

        private UserInfo friend;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    friend.setSelected(!friend.isSelected());
                    notifyDataSetChanged();
                    listener.onFriendClick();
                }
            });
        }
        private void BindData(UserInfo friend){
            this.friend = friend;
            Glide.with(itemView.getContext()).load(friend.getAvatar()).into(imvAvatar);
            txtFullName.setText(friend.getFullName());
            if (friend.isSelected()) {
                imvAdd.setVisibility(View.GONE);
                imvCheck.setVisibility(View.VISIBLE);
            } else {
                imvAdd.setVisibility(View.VISIBLE);
                imvCheck.setVisibility(View.GONE);
            }
        }
    }
}
