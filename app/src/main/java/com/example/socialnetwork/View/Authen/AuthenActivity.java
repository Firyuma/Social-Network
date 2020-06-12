package com.example.socialnetwork.View.Authen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.socialnetwork.Base.BaseActivity;
import com.example.socialnetwork.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AuthenActivity extends BaseActivity implements View.OnClickListener{
    public final static int RC_SIGN_IN = 1;

    @BindView(R.id.btn_login_with_account)
    Button btnLogin;
    @BindView(R.id.layout_register)
    RelativeLayout layoutRegister;
    @BindView(R.id.btn_login_with_google)
    Button btnLoginGoogle;

    private GoogleSignInClient mGoogleSignInClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authen);
        ButterKnife.bind(this);
        btnLogin.setOnClickListener(this);
        layoutRegister.setOnClickListener(this);
        btnLoginGoogle.setOnClickListener(this);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login_with_account:
                goToLoginScreen();
                break;
            case R.id.layout_register:
                goToRegisterScreen();
                break;
            case R.id.btn_login_with_google:
                signIn();
                break;
            default:
                break;
        }
    }

    protected void goToRegisterScreen() {
        Intent intentToLoginWithAccount = new Intent(this, RegisterActivity.class);
        startActivity(intentToLoginWithAccount);
    }

    protected void goToLoginScreen() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.

        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("signin", "signInResult:failed code=" + e.getStatusCode());

        }
    }
}
