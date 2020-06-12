package com.example.socialnetwork.View.Home.Tab_menu;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.socialnetwork.Base.BaseFragment;
import com.example.socialnetwork.Context.RealmContext;
import com.example.socialnetwork.Model.Response.UserInfo;
import com.example.socialnetwork.R;
import com.example.socialnetwork.View.Authen.AuthenActivity;
import com.example.socialnetwork.View.Profile.ProfileActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MenuFragment extends Fragment implements View.OnClickListener {

    @BindView(R.id.item_profile)
    RelativeLayout itemProfile;
    @BindView(R.id.imv_avatar)
    ImageView imvAvatar;
    @BindView(R.id.tv_full_name)
    TextView tvFullName;
    @BindView(R.id.item_friends)
    RelativeLayout itemFriends;
    @BindView(R.id.item_update_profile)
    RelativeLayout itemUpdateProfile;
    @BindView(R.id.item_log_out)
    RelativeLayout itemLogout;

    private UserInfo userInfo;

    public MenuFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, container, false);
        init(view);
        addListener();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        userInfo = RealmContext.getInstance().getUser();
        Glide.with(this).load(userInfo.getAvatar()).into(imvAvatar);
        tvFullName.setText(userInfo.getFullName());
    }

    private void init(View view) {
        ButterKnife.bind(this, view);
    }

    private void addListener() {
        itemProfile.setOnClickListener(this);
        itemFriends.setOnClickListener(this);
        itemUpdateProfile.setOnClickListener(this);
        itemLogout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.item_profile:
                Log.d("tuandm", "go to profile");
                goToProfile();
                break;
            case R.id.item_friends:
                Log.d("tuandm", "go to friend");
                goToFriendActivity();
                break;
            case R.id.item_update_profile:
                Log.d("tuandm", "go to update");
                goToUpdateProfile();
                break;
            case R.id.item_log_out:
                RealmContext.getInstance().deleteUser();
                goToLogin();
                break;
        }
    }

    private void goToProfile() {
        Intent intent = new Intent(getContext(), ProfileActivity.class);
        intent.putExtra(ProfileActivity.USER_NAME_KEY, userInfo.getUsername());
        startActivity(intent);
    }

    private void goToFriendActivity() {
    }

    private void goToUpdateProfile() {
    }

    private void goToLogin() {
        Intent intent = new Intent(getContext(), AuthenActivity.class);
        startActivity(intent);
        getActivity().finish();
    }
}
