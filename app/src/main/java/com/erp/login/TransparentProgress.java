package com.erp.login;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;


/**
 * Created by Eswar on 28/06/15.
 */
public class TransparentProgress extends Dialog {

    // private ImageView iv;

    private Context context;
    public TransparentProgress(Context context) {
        super(context, R.style.TransparentProgressDialog);
        this.context = context;
        WindowManager.LayoutParams wlmp = getWindow().getAttributes();
        wlmp.gravity = Gravity.CENTER_HORIZONTAL;
        getWindow().setAttributes(wlmp);
        setTitle(null);
        setCancelable(false);
        setOnCancelListener(null);
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        // iv = new ImageView(context);
        // iv.setImageResource(resourceIdOfImage);


        ProgressBar progressBar = new ProgressBar(context, null, android.R.attr.progressBarStyleLarge);

        progressBar.setIndeterminate(true);
        progressBar.setVisibility(View.VISIBLE);
        progressBar.getIndeterminateDrawable().setColorFilter(Color.BLUE, android.graphics.PorterDuff.Mode.MULTIPLY);
        RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(80, 80);
        params1.addRule(RelativeLayout.CENTER_IN_PARENT);
        // int color = 0xFF00FF00;
        // progressBar.getIndeterminateDrawable().setColorFilter(Color.parseColor("#00ff00"), PorterDuff.Mode.MULTIPLY);
        // progressBar.getProgressDrawable().setColorFilter(Color.parseColor("#00ff00"), PorterDuff.Mode.MULTIPLY);
//        progressBar.getProgressDrawable().setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_IN);
        layout.addView(progressBar, params1);


        //TextView textView = new TextView(context);
        //textView.setText("Please wait...");
        //layout.addView(textView, params);


        addContentView(layout, params);
    }



    @Override
    public void show() {
        super.show();

    }
}
