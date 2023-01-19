package com.betplay.dpboss_off;

import android.app.Dialog;

import android.view.Window;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.DrawableImageViewTarget;

public class ViewDialog {

    AppCompatActivity activity;
    Dialog dialog;
    public ViewDialog(AppCompatActivity activity) {
        this.activity = activity;
    }

    public void showDialog() {

        dialog  = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.progress);
        ImageView gifImageView = dialog.findViewById(R.id.custom_loading_imageView);
        DrawableImageViewTarget imageViewTarget = new DrawableImageViewTarget(gifImageView);
        Glide.with(activity)
                .load(R.drawable.loadgid)
                .placeholder(R.drawable.loadgid)
                .centerCrop()
                .into(imageViewTarget);
        dialog.show();
    }

    public void hideDialog(){
        dialog.dismiss();
    }

}