package com.example.helloworld.functions;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;

import com.example.helloworld.MainActivity;
import com.example.helloworld.R;

public class MyDialog extends PopupWindow {
    public Button yes;
    public Button cancel;
    public Button no;
    public int result;

    public MyDialog(Context context, View view) {
        super(context);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setContentView(LayoutInflater.from(context).inflate(R.layout.manager_layout, null));
        this.setOutsideTouchable(false);
        this.setBackgroundDrawable(new ColorDrawable(0x00000000));
        this.showAsDropDown(view);
    }
}