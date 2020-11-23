package com.cgg.ghmcpollingapp.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.cgg.ghmcpollingapp.R;

public class CustomProgressDialog extends Dialog {
    private CustomProgressDialog mDialog;

    public CustomProgressDialog(Context context) {
        super(context);
        try {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            View view = LayoutInflater.from(context).inflate(R.layout.custom_progress_layout, null);
            ImageView imageprogress = view.findViewById(R.id.imageprogress);
            GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(imageprogress);
            Glide.with(context).load(R.drawable.loader_black1).into(imageViewTarget);
            //  customProgressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            setContentView(view);
            if (getWindow() != null)
                this.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            setCancelable(false);
            setCanceledOnTouchOutside(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
