package com.example.socialnetwork.View.Profile;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.socialnetwork.Base.BaseActivity;
import com.example.socialnetwork.Context.RealmContext;
import com.example.socialnetwork.Model.Request.Avatar;
import com.example.socialnetwork.Model.Request.CreateStatusSendForm;
import com.example.socialnetwork.Model.Request.LikePostSendForm;
import com.example.socialnetwork.Model.Response.Status;
import com.example.socialnetwork.Model.Response.UserInfo;
import com.example.socialnetwork.Model.Response.UserProfile;
import com.example.socialnetwork.Network.RetrofitService;
import com.example.socialnetwork.Network.RetrofitUtils;
import com.example.socialnetwork.R;
import com.example.socialnetwork.Utils.Constant;
import com.example.socialnetwork.View.Adapter.StatusAdapter;
import com.example.socialnetwork.View.Comment.CommentActivity;
import com.glide.slider.library.SliderLayout;
import com.glide.slider.library.SliderTypes.DefaultSliderView;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends BaseActivity implements StatusAdapter.OnStatusClickListener, View.OnClickListener {
    public static final String USER_NAME_KEY = "user_name_key";
    private final int REQUEST_GET_IMAGE = 1;
    private final int REQUEST_PERMISSION = 2;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.slider)
    SliderLayout sliderLayout;
    @BindView(R.id.view_flipper)
    ViewFlipper viewFlipper;

    @BindView(R.id.imv_avatar)
    CircleImageView imvAvatar;
    @BindView(R.id.layout_change_avatar)
    RelativeLayout layoutChangeAvatar;
    @BindView(R.id.tv_full_name)
    TextView tvFullName;

    @BindView(R.id.layout_address)
    LinearLayout layoutAddress;
    @BindView(R.id.tv_address)
    TextView tvAddress;

    @BindView(R.id.layout_phone)
    LinearLayout layoutPhone;
    @BindView(R.id.tv_phone)
    TextView tvPhone;

    @BindView(R.id.tv_birth_day)
    TextView tvBirthday;

    @BindView(R.id.rv_status)
    RecyclerView recyclerView;

    StatusAdapter statusAdapter;
    ArrayList<Status> statusData;

    String username;
    UserInfo userInfo;
    boolean isMyProfile;
    UserProfile userProfile;

    private Uri avatarPath;
    RetrofitService retrofitService;
    ProgressDialog progressDialog;

    // Firebase
    FirebaseStorage storage;
    StorageReference storageReference;

    private String[] listPermissions;

    private Animation animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        init();
        addListener();
        getProfile();
    }
    private void init() {
        ButterKnife.bind(this);
        setupToolBar();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Đang xử lí...");

        animation = AnimationUtils.loadAnimation(this, R.anim.zoom_out);


        username = getIntent().getStringExtra(USER_NAME_KEY);
        userInfo = RealmContext.getInstance().getUser();
        isMyProfile = userInfo.getUsername().equals(username);

        retrofitService = RetrofitUtils.getInstance().createService(RetrofitService.class);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        configMyProfile();

        statusData = new ArrayList<>();
        statusAdapter = new StatusAdapter(statusData, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(statusAdapter);
    }
    private void configMyProfile() {
        if (isMyProfile) {
            layoutChangeAvatar.setVisibility(View.VISIBLE);
        } else {
            layoutChangeAvatar.setVisibility(View.GONE);
        }
    }
    private void addListener() {
        layoutAddress.setOnClickListener(this);
        layoutPhone.setOnClickListener(this);
        layoutChangeAvatar.setOnClickListener(this);
    }

    private void setupToolBar() {
        this.setSupportActionBar(toolbar);
        this.getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("");
        this.setSupportActionBar(toolbar);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void getProfile() {
        retrofitService.getProfile(username, userInfo.getId()).enqueue(new Callback<UserProfile>() {
            @Override
            public void onResponse(Call<UserProfile> call, Response<UserProfile> response) {
                userProfile = response.body();
                if (response.code() == Constant.STATUS_OK && userProfile != null) {
                    bindData(userProfile);
                    viewFlipper.setDisplayedChild(1);
                } else {
                    showToast("Không có dữ liệu!");
                }
            }

            @Override
            public void onFailure(Call<UserProfile> call, Throwable t) {
                showToast("Lỗi kết nối!");
            }
        });
    }
    private void bindData(UserProfile userProfile) {
        if (!userProfile.getAvatarUrl().isEmpty()) {
            Glide.with(this).load(userProfile.getAvatarUrl()).into(imvAvatar);
        }

        tvFullName.setText(userProfile.getFullName());
        tvAddress.setText(userProfile.getAddress());
        tvPhone.setText(userProfile.getPhone());

        tvBirthday.setText(userProfile.getBirthday());
        configSlider(userProfile.getCoverPhoto());

        statusData.clear();
        statusData.addAll(userProfile.getStatuses());
        statusAdapter.notifyDataSetChanged();
    }
    @SuppressLint("CheckResult")
    private void configSlider(String[] images) {

        RequestOptions options = new RequestOptions();
        options.centerCrop();

        sliderLayout.removeAllSliders();
        for (String image : images) {
            DefaultSliderView sliderViewStore = new DefaultSliderView(this);
            sliderViewStore
                    .image(image)
                    .setRequestOption(options)
                    .setProgressBarVisible(true);
            sliderViewStore.bundle(new Bundle());
            sliderLayout.addSlider(sliderViewStore);
        }

        sliderLayout.setPresetTransformer(SliderLayout.Transformer.Accordion);
        sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Top);
        sliderLayout.setDuration(4000);
    }
    private void createStatus(String content) {
        CreateStatusSendForm statusSendForm = new CreateStatusSendForm(userInfo.getId(), content);
        retrofitService.createStatus(statusSendForm).enqueue(new Callback<Status>() {
            @Override
            public void onResponse(Call<Status> call, Response<Status> response) {
                Status status = response.body();
                if (response.code() == Constant.STATUS_OK && status != null) {
                    statusData.add(0, status);
                    statusAdapter.notifyDataSetChanged();
                } else {
                    showToast("Có lỗi xảy ra.Vui lòng liên hệ nhà phát triển!");
                }
            }

            @Override
            public void onFailure(Call<Status> call, Throwable t) {
                showToast("Vui lòng kiểm tra lại kết nối mạng!");
            }
        });
    }

    private void likeStatus(String userId, final Status status) {

        LikePostSendForm sendForm = new LikePostSendForm(userId, status.getPostId());
        retrofitService.likeStatus(sendForm).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() != Constant.STATUS_OK) {
                    handleLikeAction(status);
                    showToast("Có lỗi xảy ra!");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                handleLikeAction(status);
                showToast("Vui lòng kiểm tra lại kết nối mạng!");
            }
        });
    }

    private void handleLikeAction(Status status) {

        status.setLike(!status.isLike());
        if (status.isLike()) {
            status.setNumberLike(status.getNumberLike() + 1);
        } else {
            status.setNumberLike(status.getNumberLike() - 1);
        }
        statusAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_address:
                showMap(userProfile.getAddress());
                break;
            case R.id.layout_phone:
                callPhone(userProfile.getPhone());
                break;
            case R.id.layout_change_avatar:
                ensurePermission();
                break;
        }
    }

    private void ensurePermission(){
        listPermissions = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};
            if(!hasPermissions(this,listPermissions)){
                ActivityCompat.requestPermissions(this,listPermissions,REQUEST_PERMISSION);
            }
            else {
                galleryIntent();
            }
    }

    private boolean hasPermissions(Context context, String ... permissions){
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PERMISSION) {
            if (!hasPermissions(ProfileActivity.this, listPermissions)) {
                showToast("Yêu cầu bị từ chối!");
            } else {
                galleryIntent();
            }
        }
    }
    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_GET_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_GET_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    avatarPath = data.getData();
                    uploadImage();
                }
            }
        }
    }
    private void uploadImage() {

        if (avatarPath != null) {
            progressDialog.show();

            StorageReference riversRef = storageReference.child("avatar/" + userInfo.getUsername() + "/" + avatarPath.getLastPathSegment());
            riversRef.putFile(avatarPath)
                    .addOnSuccessListener(taskSnapshot -> getUploadUrl())
                    .addOnFailureListener(e -> {
                        progressDialog.dismiss();
                        showToast("Tải lên thất bại!");
                    });
        }
    }
    private void getUploadUrl() {

        StorageReference ref = storageReference.child("avatar/" + avatarPath.getLastPathSegment());
        UploadTask uploadTask = ref.putFile(avatarPath);

        uploadTask.continueWithTask(task -> {
            if (!task.isSuccessful()) {
                Log.d("tuandmabc", "throw Exception");
                throw task.getException();
            }
            return ref.getDownloadUrl();
        }).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.d("tuandmabc", task.getResult().toString());

                updateAvatar(task.getResult().toString());

            } else {
                progressDialog.dismiss();
                showToast("Tải lên thất bại!");
            }
        });
    }
    private void updateAvatar(String url) {
        Avatar sendForm = new Avatar(url);
        retrofitService.updateAvatar(userInfo.getId(), sendForm).enqueue(new Callback<Avatar>() {
            @Override
            public void onResponse(Call<Avatar> call, Response<Avatar> response) {
                Avatar avatar = response.body();
                if (response.code() == Constant.STATUS_OK && avatar != null) {
                    RealmContext.getInstance().updateAvatar(avatar.getAvatarUrl());
                    Glide.with(ProfileActivity.this).load(avatar.getAvatarUrl()).into(imvAvatar);
                    showToast("Tải lên thành công!");
                } else {
                    showToast("Tải lên thất bại!");
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<Avatar> call, Throwable t) {
                progressDialog.dismiss();
                showToast("Tải lên thất bại!");
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private void showMap(String address) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("geo:0,0?q=" + address)); //lat lng or address query
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }
        } catch (ActivityNotFoundException ex) {
            ex.printStackTrace();
        }
    }
    private void callPhone(String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phone));
        startActivity(intent);
    }


    @Override
    public void onCreateStatus(String content) {
        createStatus(content);
    }

    @Override
    public void onLikeClick(int position) {
        handleLikeAction(statusData.get(position));
        likeStatus(userInfo.getId(), statusData.get(position));
    }

    @Override
    public void onCommentClick(Status status) {
        Intent intent = new Intent(this, CommentActivity.class);
        intent.putExtra(Constant.POST_ID, status.getPostId());
        intent.putExtra(Constant.POST_NUMBER_LIKE, status.getNumberLike());
        intent.putExtra(Constant.POST_IS_LIKE, status.isLike());

        startActivity(intent);

    }

    @Override
    public void onAvatarAndNameClick(String username) {

    }


}