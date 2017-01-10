package com.kejiang.yuandl.mylibrary.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kejiang.yuandl.mylibrary.CustomActivityOnCrash;
import com.kejiang.yuandl.mylibrary.email.ReportByEmail;

import cat.ereza.customactivityoncrash.R;


public final class DefaultErrorActivity extends Activity {
    private View CustomView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.customactivityoncrash_default_error_activity);
        Button restartButton = (Button) findViewById(R.id.customactivityoncrash_error_activity_restart_button);

        final Class<? extends Activity> restartActivityClass = CustomActivityOnCrash.getRestartActivityClassFromIntent(getIntent());

        if (restartActivityClass != null) {
            restartButton.setText(R.string.customactivityoncrash_error_activity_restart_app);
            restartButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent(DefaultErrorActivity.this, restartActivityClass);
                    CustomActivityOnCrash.restartApplicationWithIntent(DefaultErrorActivity.this, intent);
                }
            });
        } else {
            restartButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    CustomActivityOnCrash.closeApplication(DefaultErrorActivity.this);
                }
            });
        }

        Button moreInfoButton = (Button) findViewById(R.id.customactivityoncrash_error_activity_more_info_button);

        if (CustomActivityOnCrash.isShowErrorDetailsFromIntent(getIntent())) {
            moreInfoButton.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {

                    AlertDialog dialog = new Builder(DefaultErrorActivity.this).
                            setTitle(R.string.customactivityoncrash_error_activity_error_details_title).
                            setMessage(CustomActivityOnCrash.getAllErrorDetailsFromIntent(DefaultErrorActivity.this, DefaultErrorActivity.this.getIntent())).
                            setPositiveButton(R.string.customactivityoncrash_error_activity_error_details_close, null).
                            setNegativeButton(R.string.report_to_developer, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            new Thread() {
                                                public void run() {
                                                    super.run();
                                                    ReportByEmail.sendEmail2(CustomActivityOnCrash.getApplicationName(DefaultErrorActivity.this), CustomActivityOnCrash.getAllErrorDetailsFromIntent(DefaultErrorActivity.this, DefaultErrorActivity.this.getIntent()));
                                                }
                                            }.start();
                                        }
                                    }

                            ).setNeutralButton(R.string.customactivityoncrash_error_activity_error_details_copy, new DialogInterface.OnClickListener() {


                        public void onClick(DialogInterface dialog, int which) {

                            DefaultErrorActivity.this.copyErrorToClipboard();
                            Toast.makeText(DefaultErrorActivity.this, R.string.customactivityoncrash_error_activity_error_details_copied, Toast.LENGTH_SHORT).show();
                        }


                    }).show();
                    TextView textView = (TextView) dialog.findViewById(android.R.id.message);
                    textView.setTextSize(0, DefaultErrorActivity.this.getResources().getDimension(R.dimen.customactivityoncrash_error_activity_error_details_text_size));


                }


            });


        } else {


            moreInfoButton.setVisibility(8);
        }

        int defaultErrorActivityDrawableId = CustomActivityOnCrash.getDefaultErrorActivityDrawableIdFromIntent(getIntent());
        ImageView errorImageView = (ImageView) findViewById(R.id.customactivityoncrash_error_activity_image);
        if (Build.VERSION.SDK_INT >= 21) {
            errorImageView.setImageDrawable(getResources().getDrawable(defaultErrorActivityDrawableId, getTheme()));
        } else {
            errorImageView.setImageDrawable(getResources().getDrawable(defaultErrorActivityDrawableId));
        }
    }

    protected Builder myBuilder(Context context) {
        LayoutInflater inflater = getLayoutInflater();
        Builder builder = new Builder(context);
        this.CustomView = inflater.inflate(R.layout.customview, null);
        return builder.setView(this.CustomView);
    }

    private void copyErrorToClipboard() {
        String errorInformation = CustomActivityOnCrash.getAllErrorDetailsFromIntent(this, getIntent());

        if (Build.VERSION.SDK_INT >= 11) {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService("clipboard");
            ClipData clip = ClipData.newPlainText(getString(R.string.customactivityoncrash_error_activity_error_details_clipboard_label), errorInformation);
            clipboard.setPrimaryClip(clip);
        } else {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) getSystemService("clipboard");
            clipboard.setText(errorInformation);
        }
    }
}


/* Location:              E:\kejiang\AndroidStudioProjects\inspection\app\libs\UncaughtExceptionSendEmail\classes.jar!\cat\ereza\customactivityoncrash\activity\DefaultErrorActivity.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */