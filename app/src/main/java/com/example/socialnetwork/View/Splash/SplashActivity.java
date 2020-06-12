package com.example.socialnetwork.View.Splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.socialnetwork.Base.BaseActivity;
import com.example.socialnetwork.Context.RealmContext;
import com.example.socialnetwork.Model.Response.UserInfo;
import com.example.socialnetwork.R;
import com.example.socialnetwork.View.Authen.AuthenActivity;
import com.example.socialnetwork.View.Home.HomeActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SplashActivity extends BaseActivity {
@BindView(R.id.image_view)
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    init();
    }

    private void init() {
        ButterKnife.bind(this);
        Glide.with(this).load(R.drawable.logo_social_network).into(imageView);

    }

    @Override
    protected void onResume() {
        super.onResume();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
            checkLogin();
            }
        },1000);
    }

    private void checkLogin() {
        UserInfo userInfo = RealmContext.getInstance().getUser();
        if (userInfo!= null){
            gotoHomeScreen();
        } else{
            gotoAuthenScreen();
        }
    }

    private void gotoAuthenScreen() {
        Intent intent = new Intent(this, AuthenActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        }

    private void gotoHomeScreen() {
        Intent intent = new Intent(this,HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }
}