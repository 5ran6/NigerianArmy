package com.naic.nigerianarmy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import pl.droidsonroids.gif.GifImageView;

import static java.security.AccessController.getContext;

public class SuccessActivity extends AppCompatActivity {


    private String army_number = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success);
        TextView armyNumber = findViewById(R.id.army_number);
        army_number = getIntent().getStringExtra("extra");
        armyNumber.setText(army_number);

    }

    public void copy(View view) {
        setClipboard(getApplicationContext(), army_number);
        finish();
        startActivity(new Intent(getApplicationContext(), BioData.class).putExtra("extra", "enroll"));
    }

    public void done(View view) {
        finish();
        startActivity(new Intent(getApplicationContext(), BioData.class).putExtra("extra", "enroll"));
    }

    private void setClipboard(Context context, String text) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Copied Text", text);
        Toast.makeText(getApplicationContext(), "Copied to clipboard!", Toast.LENGTH_SHORT).show();
        clipboard.setPrimaryClip(clip);
    }

}