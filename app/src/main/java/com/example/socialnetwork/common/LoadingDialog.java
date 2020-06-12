package com.example.socialnetwork.common;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Context;
import android.text.Layout;
import android.view.Window;

import androidx.annotation.NonNull;

import com.example.socialnetwork.R;

public class LoadingDialog extends Dialog {
    public LoadingDialog(@NonNull Context context){
        super(context);
        init();
    }

    private void init() {
        setContentView(R.layout.dialog_loading);
        Window window =this.getWindow();
        if (window != null){
        window.setLayout(ActionBar.LayoutParams.WRAP_CONTENT,ActionBar.LayoutParams.MATCH_PARENT);
        window.setBackgroundDrawableResource(android.R.color.transparent);
        }
    }
}
