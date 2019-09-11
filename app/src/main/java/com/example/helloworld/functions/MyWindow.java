package com.example.helloworld.functions;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.DialogFragment;

import com.example.helloworld.R;

public class MyWindow extends DialogFragment {
    public Button yes;
    public Button cancel;
    public Button no;
    public int result;

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.save_layout, container);
//        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(0x00000000));// 背景透明
//
//        // 绑定按钮事件
//        initButton(view);
//        return view;
//    }
//
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_FRAME, android.R.style.Theme);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        return builder.create();
    }

    private void initButton(View view) {
        yes = view.findViewById(R.id.yes_button);
        cancel = view.findViewById(R.id.cancel_button);
        no = view.findViewById(R.id.no_button);

        // 初始化`保存`按钮
        yes.setOnClickListener(new View.OnClickListener() {//
            @Override
            public void onClick(View view) {
                result = 1;
                dismiss();
            }
        });

        // 初始化`取消`按钮
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                result = 0;
                dismiss();
            }
        });

        // 初始化`删除`按钮
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                result = -1;
                dismiss();
            }
        });
    }

//    @Override
//    public void onDismiss(final DialogInterface dialog) {
//        super.onDismiss(dialog);
//
//        Activity activity = getActivity();
//        Log.i("fuck instance", activity + "");
//    }
}