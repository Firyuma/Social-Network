package com.example.socialnetwork.View.Home.Tab_home;

import android.app.Activity;
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
import com.example.socialnetwork.Model.Request.CreateStatusSendForm;
import com.example.socialnetwork.Model.Request.LikePostSendForm;
import com.example.socialnetwork.Model.Response.Status;
import com.example.socialnetwork.Model.Response.UserInfo;
import com.example.socialnetwork.Network.RetrofitService;
import com.example.socialnetwork.Network.RetrofitUtils;
import com.example.socialnetwork.R;
import com.example.socialnetwork.Utils.Constant;
import com.example.socialnetwork.View.Adapter.StatusAdapter;
import com.example.socialnetwork.View.Comment.CommentActivity;
import com.example.socialnetwork.View.Profile.ProfileActivity;
import com.example.socialnetwork.common.LoadingDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends BaseFragment implements StatusAdapter.OnStatusClickListener {
    private final int REQUEST_CODE_COMMENT = 1;
    public HomeFragment(){}
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.view_flipper)
    ViewFlipper viewFlipper;
    @BindView(R.id.rcv_status)
    RecyclerView rcvStatus;

    private UserInfo userInfo;
    private RetrofitService retrofitService;

    private Status currentStatus;
    private List<Status> statuses;
    private StatusAdapter statusAdapter;

    private LoadingDialog loadingDialog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        init();
        addListener();
        getAllStatus();
    }


    private void init() {
        loadingDialog = new LoadingDialog(getActivity());
        userInfo = RealmContext.getInstance().getUser();
        retrofitService = RetrofitUtils.getInstance().createService();
        statuses = new ArrayList<>();
        statusAdapter = new StatusAdapter(statuses,this);
        rcvStatus.setAdapter(statusAdapter);
        rcvStatus.setLayoutManager(new LinearLayoutManager(
                getContext(),RecyclerView.VERTICAL,false));
    }

    private void addListener() {
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getAllStatus();
            }
        });
    }

    private void getAllStatus() {
        retrofitService.getAllStatus(userInfo.getId()).enqueue(new Callback<ArrayList<Status>>() {
            @Override
            public void onResponse(Call<ArrayList<Status>> call, Response<ArrayList<Status>> response) {
                ArrayList<Status> list = response.body();
                if (response.code() == 200 && list != null && !list.isEmpty()) {
                    statuses.clear();
                    statuses.addAll(list);
                    statusAdapter.notifyDataSetChanged();
                    viewFlipper.setDisplayedChild(3);
                } else {
                    viewFlipper.setDisplayedChild(1);
                }
                refreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<ArrayList<Status>> call, Throwable t) {
                viewFlipper.setDisplayedChild(2);
                refreshLayout.setRefreshing(false);
            }
        });
    }

    private void createStatus(String content) {
        CreateStatusSendForm statusSendForm = new CreateStatusSendForm(userInfo.getId(), content);

        retrofitService.createStatus(statusSendForm).enqueue(new Callback<Status>() {
            @Override
            public void onResponse(Call<Status> call, Response<Status> response) {
                Status status = response.body();
                if (response.code() == 200 && status != null) {
                    statuses.add(0, status);
                    statusAdapter.notifyDataSetChanged();
                    showToast("Đăng bài thành công!");
                } else {
                    showToast("Đăng bài thất bại! Vui lòng thử lại sau!");
                }
                loadingDialog.dismiss();
            }

            @Override
            public void onFailure(Call<Status> call, Throwable t) {
                showToast("Đăng bài thất bại! Vui lòng thử lại sau!");
                loadingDialog.dismiss();
            }
        });
    }

    private void likeStatus(int position) {
        LikePostSendForm sendForm = new LikePostSendForm(userInfo.getId(), statuses.get(position).getPostId());
        retrofitService.likeStatus(sendForm).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() == 200) {
                    showToast("Thành công");
                } else {
                    handleLikeAction(position);
                    showToast("Thất bại");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                handleLikeAction(position);
                showToast("Thất bại");
            }
        });
    }

    private void handleLikeAction(int position) {
        statuses.get(position).setLike(!statuses.get(position).isLike());
        if (statuses.get(position).isLike()) {
            statuses.get(position).setNumberLike(statuses.get(position).getNumberLike() + 1);
        } else {
            statuses.get(position).setNumberLike(statuses.get(position).getNumberLike() - 1);
        }
        statusAdapter.notifyItemChanged(position); // thay đổi giao diện
    }

    @Override
    public void onCreateStatus(String content) {
        loadingDialog.show();
        createStatus(content);
    }

    @Override
    public void onLikeClick(int position) {
        handleLikeAction(position);
        likeStatus(position);
    }

    @Override
    public void onCommentClick(Status status) {
        currentStatus = status;

        Intent intent = new Intent(getActivity(), CommentActivity.class);
        intent.putExtra(Constant.POST_ID, status.getPostId());
        intent.putExtra(Constant.POST_NUMBER_LIKE, status.getNumberLike());
        intent.putExtra(Constant.POST_IS_LIKE, status.isLike());

        startActivityForResult(intent, REQUEST_CODE_COMMENT);
        getActivity().overridePendingTransition(R.anim.slide_bottom_to_top, R.anim.slide_top_to_bottom);
    }

    @Override
    public void onAvatarAndNameClick(String username) {
        Intent intent = new Intent(getContext(), ProfileActivity.class);
        intent.putExtra(ProfileActivity.USER_NAME_KEY, username);

        startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_COMMENT) {

            boolean isLike = data.getBooleanExtra(Constant.IS_LIKE, false);
            int numberLike = data.getIntExtra(Constant.NUMBER_LIKE, 0);
            int numberComment = data.getIntExtra(Constant.NUMBER_COMMENT, 0);

            currentStatus.setLike(isLike);
            currentStatus.setNumberComment(numberComment);
            currentStatus.setNumberLike(numberLike);

            statusAdapter.notifyDataSetChanged();
        }
    }
}
