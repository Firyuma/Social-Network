package com.example.socialnetwork.View.Authen;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.socialnetwork.Base.BaseActivity;
import com.example.socialnetwork.Context.RealmContext;
import com.example.socialnetwork.Model.Request.LoginSendForm;
import com.example.socialnetwork.Model.Response.UserInfo;
import com.example.socialnetwork.Network.RetrofitService;
import com.example.socialnetwork.Network.RetrofitUtils;
import com.example.socialnetwork.R;
import com.example.socialnetwork.View.Home.HomeActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends BaseActivity {
@BindView(R.id.edt_username)
    EditText edtUsername;
@BindView(R.id.edt_password)
    EditText edtPassword;
@BindView(R.id.btn_sign_in)
    Button btnLogin;

RetrofitService retrofitService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
        addListener();
    }

    private void addListener() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            String username = edtUsername.getText().toString();
            String password = edtPassword.getText().toString();
            Login(username,password);
            }
        });
    }

    private void Login(String username, String password) {
        LoginSendForm sendForm = new LoginSendForm(username,password);
        retrofitService.login(sendForm).enqueue(new Callback<UserInfo>() {
            @Override
            public void onResponse(Call<UserInfo> call, Response<UserInfo> response) {
                UserInfo userInfo = response.body();
                if (response.code() == 200 && userInfo!= null ){
                    RealmContext.getInstance().addUser(userInfo);
                    gotoHome();
                }
                else{
                    showToast("Tên đăng nhập hoặc mật khẩu không đúng");
                }
            }

            @Override
            public void onFailure(Call<UserInfo> call, Throwable t) {

            }
        });
}



    private void init() {
        ButterKnife.bind(this);
        retrofitService = RetrofitUtils.getInstance().createService();
    }
    private void gotoHome() {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    protected void goToRegisterScreen() {
        Intent intentToLoginWithAccount = new Intent(this, RegisterActivity.class);
        startActivity(intentToLoginWithAccount);
    }


}
