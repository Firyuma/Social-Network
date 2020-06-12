package com.example.socialnetwork.View.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.socialnetwork.Context.RealmContext;
import com.example.socialnetwork.Model.Response.Status;
import com.example.socialnetwork.Model.Response.UserInfo;
import com.example.socialnetwork.R;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class StatusAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public interface OnStatusClickListener{
        void onCreateStatus(String content);
        void onLikeClick(int position);
        void onCommentClick(Status status);
        void onAvatarAndNameClick(String username);
    }

    private final int TYPE_HEADER = 1;
    private final int TYPE_STATUS = 2;
    private List<Status> statusList;
    private OnStatusClickListener listener;

    public StatusAdapter(List<Status> statusList, OnStatusClickListener listener){
        this.statusList = statusList;
        this.listener= listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == TYPE_HEADER){
            View itemview = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_status_header,parent,false);
            return new HeaderViewHolder(itemview);
        }
        else {
            View itemview = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_status,parent,false);
            return new StatusViewHolder(itemview);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof StatusViewHolder){
           ((StatusViewHolder) holder).bindData(statusList.get(position-1),position);
        }
    }

    @Override
    public int getItemCount() {
        return statusList.size()+1;
    }
    public int getItemViewType(int position){
        if(position==0){
            return TYPE_HEADER;
        }
        else return TYPE_STATUS;

    }


    class StatusViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.imv_avatar)
        CircleImageView imvAvatar;
        @BindView(R.id.tv_full_name)
        TextView txtFullName;
        @BindView(R.id.tv_create_date)
        TextView txtCreateDate;
        @BindView(R.id.tv_content)
        TextView txtContent;
        @BindView(R.id.tv_like_number)
        TextView txtLikeNumber;
        @BindView(R.id.tv_comment_number)
        TextView txtCommentNumber;

        @BindView(R.id.imv_like)
        ImageView imvLike;
        @BindView(R.id.tv_like)
        TextView txtLike;
        @BindView(R.id.item_like)
        LinearLayout itemLike;


        @BindView(R.id.item_comment)
        LinearLayout itemComment;

        private int position;
        public StatusViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            itemLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onLikeClick(position);
                }
            });

            txtFullName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onAvatarAndNameClick(statusList.get(position).getAuthor());
                }
            });

            imvAvatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onAvatarAndNameClick(statusList.get(position).getAuthor());
                }
            });

            itemComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onCommentClick(statusList.get(position));
                }
            });
        }

        private void bindData(Status status, int position){
            this.position= position;

            Glide.with(itemView.getContext()).load(status.getAuthorAvatar()).into(imvAvatar);
            txtFullName.setText(status.getAuthorName());
            txtCreateDate.setText(status.getCreateDate());
            txtContent.setText(status.getContent());
            txtLikeNumber.setText(String.valueOf(status.getNumberLike()));
            txtCommentNumber.setText(String.format(Locale.getDefault(),"%d bình luận",status.getNumberComment()));

            if(status.isLike()){
                imvLike.setBackground(itemView.getResources().getDrawable(R.drawable.ic_like));
                txtLike.setText("Bỏ thích");
                txtLike.setTextColor(itemView.getResources().getColor(R.color.colorRed400));
            }
            else{
                imvLike.setBackground(itemView.getResources().getDrawable(R.drawable.ic_dont_like));
                txtLike.setText("Thích");
                txtLike.setTextColor(itemView.getResources().getColor(R.color.colorGray700));
            }
        }
    }

    class HeaderViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.imv_avatar)
        ImageView imvAvatar;
        @BindView(R.id.imv_send)
        ImageView imvSend;
        @BindView(R.id.edt_status_content)
        EditText edtStatusContent;

        public HeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

            imvSend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String content = edtStatusContent.getText().toString();
                    if(!content.isEmpty()){
                        listener.onCreateStatus(content);
                        edtStatusContent.setText("");
                    }
                }
            });
            UserInfo userInfo= RealmContext.getInstance().getUser();
            Glide.with(itemView.getContext()).load(userInfo.getAvatar()).into(imvAvatar);
        }
    }
}
