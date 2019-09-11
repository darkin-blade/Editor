package com.example.helloworld.functions;

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
    public View view;// TODO

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.save_layout, container);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(0x00000000));
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_FRAME, android.R.style.Theme);
    }
}