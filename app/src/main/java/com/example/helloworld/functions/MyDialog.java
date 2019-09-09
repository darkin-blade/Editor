package com.example.helloworld.functions;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.helloworld.R;

public class MyDialog extends Dialog {
    private Button yes;
    private Button cancel;
    private Button no;

    public MyDialog(Context context, int style) {
        super(context, style);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.save_layout);

        initButton();// 初始化按钮和监听
        // TODO TODO TODO
    }

    private void initButton() {
        // 初始化按钮
        yes = findViewById(R.id.yes_button);
        cancel = findViewById(R.id.cancel_button);
        no = findViewById(R.id.no_button);
    }
}