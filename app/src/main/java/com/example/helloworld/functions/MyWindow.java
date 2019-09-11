package com.example.helloworld.functions;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;

import androidx.fragment.app.DialogFragment;

import com.example.helloworld.MainActivity;
import com.example.helloworld.R;

public class MyWindow extends PopupWindow {
    public Button yes;
    public Button cancel;
    public Button no;
    public int result;

    public MyWindow(Context context, View view) {
        super(context);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setContentView(LayoutInflater.from(context).inflate(R.layout.manager_layout, null));
        // this.setOutsideTouchable(false);
        this.setBackgroundDrawable(null);
        this.setFocusable(true);// 否则无法进行edittext输入

        this.showAsDropDown(view);
        this.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        this.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
    }
}