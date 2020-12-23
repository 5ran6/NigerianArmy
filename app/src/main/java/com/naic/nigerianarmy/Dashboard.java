package com.naic.nigerianarmy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.multidex.BuildConfig;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.naic.nigerianarmy.utils.Tools;

public class Dashboard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void verify(View view) {
        startActivity(new Intent(this, Login.class).putExtra("extra", "verify"));
    }

    public void enroll(View view) {
        startActivity(new Intent(this, BioData.class).putExtra("extra", "enroll"));
    }

    public void dashboard(View view) {
        startActivity(new Intent(this, Login.class).putExtra("extra", "profile"));
    }

    public void bippiis_web(View view) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse("https://naic.gov.ng"));
        startActivity(i);
    }

    public void about(View view) {
        showDialogAbout();
    }

    public void exitApp(View view) {
        doExitApp();
    }

    @Override
    public void onBackPressed() {
        doExitApp();
    }

    private long exitTime = 0;

    public void doExitApp() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Tools.toast("Press again to exit app", Dashboard.this);
            exitTime = System.currentTimeMillis();
        } else {
            finishAffinity();
        }
    }

    private void showDialogAbout() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_about);
        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        ((TextView) dialog.findViewById(R.id.tv_version)).setText("Version " + BuildConfig.VERSION_NAME);

        dialog.findViewById(R.id.bt_getcode).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("https://army.mil.ng"));
                startActivity(i);
            }
        });

        dialog.findViewById(R.id.bt_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

}