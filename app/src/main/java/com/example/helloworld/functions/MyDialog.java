package com.example.helloworld.functions;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.helloworld.MainActivity;
import com.example.helloworld.R;

public class MyDialog extends Dialog {
    public Button yes;
    public Button cancel;
    public Button no;
    public int result;

    public MyDialog(Context context, int style) {
        super(context, style);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.save_layout);

        initButton();// 初始化按钮和监听
        result = -999;
        yes.setOnClickListener(new View.OnClickListener() {//
            @Override
            public void onClick(View view) {
                result = 1;
                dismiss();
                Log.i("fuck result", result + "");
            }
        });
        // TODO
    }

    private void initButton() {
        // 初始化按钮
        yes = findViewById(R.id.yes_button);
        cancel = findViewById(R.id.cancel_button);
        no = findViewById(R.id.no_button);
    }
}