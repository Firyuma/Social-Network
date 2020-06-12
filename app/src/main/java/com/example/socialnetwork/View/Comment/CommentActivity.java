package com.example.socialnetwork.View.Comment;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.socialnetwork.Base.BaseActivity;
import com.example.socialnetwork.Context.RealmContext;
import com.example.socialnetwork.Model.Request.CommentSendForm;
import com.example.socialnetwork.Model.Request.LikePostSendForm;
import com.example.socialnetwork.Model.Response.Comment;
import com.example.socialnetwork.Model.Response.UserInfo;
import com.example.socialnetwork.Network.RetrofitService;
import com.example.socialnetwork.Network.RetrofitUtils;
import com.example.socialnetwork.R;
import com.example.socialnetwork.Utils.Constant;
import com.example.socialnetwork.View.Comment.Adapter.CommentAdapter;
import com.example.socialnetwork.View.Profile.ProfileActivity;

import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentActivity extends BaseActivity implements View.OnClickListener, CommentAdapter.OnCommentClickListener {

    public static final int REQ_CODE_SPEECH_INPUT_COMMENT = 2;
    @BindView(R.id.layout_header)
    RelativeLayout layoutHeader;

    @BindView(R.id.tv_like_number)
    TextView tvLikeNumber;
    @BindView(R.id.imv_like)
    ImageView imvLike;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.edt_comment)
    EditText edtComment;
    @BindView(R.id.imv_send)
    ImageView imvSend;
    @BindView(R.id.imv_mic)
    ImageView imvMic;

    private String postId;
    private int numberLike;
    private boolean isLike;

    ArrayList<Comment> commentList;
    CommentAdapter commentAdapter;

    UserInfo userInfo;
    RetrofitService retrofitService;

    private Animation animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        init();
        addListener();
    }

    @Override
    protected void onStart() {
        super.onStart();
        getAllComment();
    }

    private void init() {
        ButterKnife.bind(this);

        retrofitService = RetrofitUtils.getInstance().createService(RetrofitService.class);

        animation = AnimationUtils.loadAnimation(this, R.anim.zoom_out);

        Intent intent = getIntent();
        postId = intent.getStringExtra(Constant.POST_ID);
        numberLike = intent.getIntExtra(Constant.POST_NUMBER_LIKE, 0);
        isLike = intent.getBooleanExtra(Constant.POST_IS_LIKE, false);

        userInfo = RealmContext.getInstance().getUser();

        tvLikeNumber.setText(String.valueOf(numberLike));
        if (isLike) {
            imvLike.setBackground(getResources().getDrawable(R.drawable.ic_like));
        } else {
            imvLike.setBackground(getResources().getDrawable(R.drawable.ic_dont_like));
        }

        commentList = new ArrayList<>();
        commentAdapter = new CommentAdapter(commentList, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(commentAdapter);
    }

    private void addListener() {
        layoutHeader.setOnClickListener(this);
        imvLike.setOnClickListener(this);
        imvSend.setOnClickListener(this);
        imvMic.setOnClickListener(this);
    }

    private void getAllComment() {
        retrofitService.getAllComment(postId).enqueue(new Callback<ArrayList<Comment>>() {
            @Override
            public void onResponse(Call<ArrayList<Comment>> call, Response<ArrayList<Comment>> response) {
                ArrayList<Comment> comments = response.body();
                if (response.code() == Constant.STATUS_OK && comments != null && !comments.isEmpty()) {
                    commentList.clear();
                    commentList.addAll(comments);
                    commentAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Comment>> call, Throwable t) {
            }
        });
    }

    private void createComment(String content) {

        CommentSendForm sendForm = new CommentSendForm(userInfo.getId(), postId, content);
        retrofitService.commentStatus(sendForm).enqueue(new Callback<Comment>() {
            @Override
            public void onResponse(Call<Comment> call, Response<Comment> response) {
                Comment comment = response.body();
                if (response.code() == Constant.STATUS_OK && comment != null) {
                    commentList.add(comment);
                    commentAdapter.notifyDataSetChanged();
                    edtComment.setText("");
                } else {
                    showToast("Có lỗi xảy ra.Vui lòng liên hệ nhà phát triển!");
                }
            }

            @Override
            public void onFailure(Call<Comment> call, Throwable t) {
                showToast("Vui lòng kiểm tra lại kết nối mạng!");
            }
        });
    }

    private void likeStatus(String userId, final String postId) {

        LikePostSendForm sendForm = new LikePostSendForm(userId, postId);
        retrofitService.likeStatus(sendForm).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() != Constant.STATUS_OK) {
                    handleLikeAction(!isLike);
                    showToast("Có lỗi xảy ra!");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                handleLikeAction(!isLike);
                showToast("Vui lòng kiểm tra lại kết nối mạng!");
            }
        });
    }

    private void handleLikeAction(boolean isLike) {
        if (isLike) {
            numberLike++;
            imvLike.setBackground(getResources().getDrawable(R.drawable.ic_like));
        } else {
            numberLike--;
            imvLike.setBackground(getResources().getDrawable(R.drawable.ic_dont_like));
        }
        tvLikeNumber.setText(String.valueOf(numberLike));
        this.isLike = isLike;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_header:
                // go to ds like
                break;
            case R.id.imv_like:
                v.startAnimation(animation);
                handleLikeAction(!isLike);
                likeStatus(userInfo.getId(), postId);
                break;
            case R.id.imv_send:
                v.startAnimation(animation);
                String content = edtComment.getText().toString();
                if (content.isEmpty()) {
                    showToast("Bạn chưa nhập nội dung!");
                } else {
                    createComment(content);
                }break;
            case R.id.imv_mic:
                startInputSpeech();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra(Constant.IS_LIKE, isLike);
        intent.putExtra(Constant.NUMBER_LIKE, numberLike);
        intent.putExtra(Constant.NUMBER_COMMENT, commentList.size());
        setResult(Activity.RESULT_OK, intent);
        super.onBackPressed();
    }

    @Override
    public void onAvatarClick(String username) {
        gotoProfileScreen(username);
    }

    public void gotoProfileScreen(String username) {
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.putExtra(Constant.USERNAME, username);
        startActivity(intent);
    }


    private void startInputSpeech() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say something...");

        startActivityForResult(intent, REQ_CODE_SPEECH_INPUT_COMMENT);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE_SPEECH_INPUT_COMMENT) {
            if (resultCode == RESULT_OK && null != data) {
                ArrayList<String> result = data
                        .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                edtComment.setText(result.get(0));
            }
        }
    }
}