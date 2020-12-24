package com.example.cpsc475_project4;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

public class WebImageView_TP extends  WebImageView {
    public MainActivity myActivity;
    public WebImageView_TP(Context context, AttributeSet set) {
        super(context,set);
    }
    public WebImageView_TP(Context context) {
        super(context);
    }

    void detach() {
        myActivity = null;
    }

    void attach(MainActivity activity) {
        this.myActivity = activity;
    }

    public void setPlaceholderImage(Drawable drawable) {
        if (drawable != null) {
            mPlaceholder = drawable;
            setImageDrawable(mPlaceholder);
        }
    }

    public void setPlaceholderImage(int resid) {
        mPlaceholder = getResources().getDrawable(resid);
        if (mImage == null) {
            setImageDrawable(mPlaceholder);
        }
    }
}
