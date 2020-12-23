package com.naic.nigerianarmy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.android.material.textfield.TextInputEditText;

public class BioData extends AppCompatActivity {

    ProgressBar progressBar;
    ImageView passport;
    TextInputEditText fullname, age, phone, height, weight, eye_color,
            hair_color, tatoo, gender, marital_status, blood_group,
            genotype, nok, state_origin, lga, hometown, nin, school_in, school_out;
    String extra = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bio_data);
        initializer();

    }

    private void initializer() {
        passport = findViewById(R.id.passport);
        progressBar = findViewById(R.id.progress);
        fullname = findViewById(R.id.fullname);
        age = findViewById(R.id.age);
        phone = findViewById(R.id.phone);
        height = findViewById(R.id.height);
        weight = findViewById(R.id.weight);
        eye_color = findViewById(R.id.eye_colour);
        hair_color = findViewById(R.id.hair_colour);
        tatoo = findViewById(R.id.tatoo);
        gender = findViewById(R.id.gender);
        marital_status = findViewById(R.id.marital);
        blood_group = findViewById(R.id.blood);
        genotype = findViewById(R.id.genotype);
        nok = findViewById(R.id.nok);
        state_origin = findViewById(R.id.state_of_origin);
        lga = findViewById(R.id.lga);
        hometown = findViewById(R.id.hometown);
        nin = findViewById(R.id.nin);
        school_in = findViewById(R.id.school_in);
        school_out = findViewById(R.id.school_out);
        extra = getIntent().getStringExtra("extra");

    }

    public void proceed(View view) {
        if (extra.equalsIgnoreCase("profile")) {
            //just go back
            startActivity(new Intent(getApplicationContext(), Dashboard.class));
        } else {
            //after validation
            progressBar.setVisibility(View.VISIBLE);
            //send to server
        }
    }
}