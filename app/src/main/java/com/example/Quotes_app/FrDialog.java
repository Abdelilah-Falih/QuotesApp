package com.example.Quotes_app;

import android.annotation.SuppressLint;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;

@SuppressLint("ValidFragment")
public class FrDialog extends DialogFragment {

    boolean isliked;
    boolean showProgress;

    @SuppressLint("ValidFragment")
    public FrDialog(boolean isliked, boolean showProgress){
        this.isliked = isliked;
        this.showProgress = showProgress;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.item_dialog, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ImageView iv_icon = view.findViewById(R.id.iv_icon);
        ProgressBar progressBar = view.findViewById(R.id.progressBar);
        if(showProgress){
            iv_icon.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
        }else{
            progressBar.setVisibility(View.GONE);
            iv_icon.setVisibility(View.VISIBLE);
            iv_icon.setImageResource(isliked? R.drawable.ic_unlike_big: R.drawable.ic_like_big);
        }

    }

}