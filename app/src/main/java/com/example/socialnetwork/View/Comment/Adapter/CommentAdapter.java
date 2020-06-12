package com.example.socialnetwork.View.Comment.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.socialnetwork.Model.Response.Comment;
import com.example.socialnetwork.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.MyViewHolder> {

    public interface OnCommentClickListener {
        void onAvatarClick(String username);
    }

    private ArrayList<Comment> commentList;
    private OnCommentClickListener listener;

    public CommentAdapter(ArrayList<Comment> commentList, OnCommentClickListener listener) {
        this.commentList = commentList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_item_comment, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        if (i < commentList.size()) {
            myViewHolder.bindView(commentList.get(i));
        }
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        Context context;
        Comment comment;

        @BindView(R.id.imv_avatar)
        ImageView imvAvatar;
        @BindView(R.id.tv_full_name)
        TextView tvFullName;
        @BindView(R.id.tv_comment)
        TextView tvComment;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();
            ButterKnife.bind(this, itemView);
        }

        private void bindView(Comment comment) {
            this.comment = comment;
            Glide.with(context).load(comment.getUserAvatar()).into(imvAvatar);
            tvFullName.setText(comment.getFullName());
            tvComment.setText(comment.getContent());
        }
    }
}