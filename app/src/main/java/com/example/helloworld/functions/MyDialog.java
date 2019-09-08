package com.example.helloworld.functions;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import com.example.helloworld.R;

public class MyDialog extends Dialog {
    public MyDialog(Context context, int style) {
        super(context, style);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.save_layout);
    }
}