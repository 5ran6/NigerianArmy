package com.naic.nigerianarmy;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.naic.nigerianarmy.interfaces.NAIC;
import com.naic.nigerianarmy.models.UserRegisterRequest;
import com.naic.nigerianarmy.models.UserResponse;

import org.apache.commons.io.IOUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.InputStream;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BioData extends AppCompatActivity {

    ProgressBar progressBar;
    ImageView passport;
    TextInputEditText fullname,email, age, phone, height, weight, eye_color,
            hair_color, tatoo, gender, marital_status, blood_group,
            genotype, nok, nok_phone, state_origin, lga, hometown, nin, school_in, school_out;
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
        email = findViewById(R.id.email);
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
        nok_phone = findViewById(R.id.nok_phone);
        state_origin = findViewById(R.id.state_of_origin);
        lga = findViewById(R.id.lga);
        hometown = findViewById(R.id.hometown);
        nin = findViewById(R.id.nin);
        school_in = findViewById(R.id.school_in);
        school_out = findViewById(R.id.school_out);
        extra = getIntent().getStringExtra("extra");

        assert extra != null;
        if (extra.equalsIgnoreCase("profile")) {
            //getIntents and set to views
            fullname.setText(getIntent().getStringExtra("name"));
            fullname.setEnabled(false);

            age.setText(getIntent().getStringExtra("age"));
            age.setEnabled(false);

            height.setText(getIntent().getStringExtra("height"));
            height.setEnabled(false);

            email.setText(getIntent().getStringExtra("email"));
            email.setEnabled(false);

            eye_color.setText(getIntent().getStringExtra("eye_color"));
            eye_color.setEnabled(false);

            hair_color.setText(getIntent().getStringExtra("hair_color"));
            hair_color.setEnabled(false);

            tatoo.setText(getIntent().getStringExtra("tattoo"));
            tatoo.setEnabled(false);

            gender.setText(getIntent().getStringExtra("sex"));
            gender.setEnabled(false);

            phone.setText(getIntent().getStringExtra("phone"));
            phone.setEnabled(false);

            marital_status.setText(getIntent().getStringExtra("marital_status"));
            marital_status.setEnabled(false);

            blood_group.setText(getIntent().getStringExtra("blood_group"));
            blood_group.setEnabled(false);

            state_origin.setText(getIntent().getStringExtra("state_of_origin"));
            state_origin.setEnabled(false);


            lga.setText(getIntent().getStringExtra("lga"));
            lga.setEnabled(false);


            hometown.setText(getIntent().getStringExtra("hometown"));
            hometown.setEnabled(false);


            nin.setText(getIntent().getStringExtra("nin"));
            nin.setEnabled(false);


            school_in.setText(getIntent().getStringExtra("sec_sch_year_in"));
            school_in.setEnabled(false);


            school_out.setText(getIntent().getStringExtra("sec_sch_year_out"));
            school_out.setEnabled(false);


            nok.setText(getIntent().getStringExtra("nok_name"));
            nok.setEnabled(false);


            nok_phone.setText(getIntent().getStringExtra("nok_phone"));
            nok_phone.setEnabled(false);

        }

    }

    public void proceed(View view) {
        if (extra.equalsIgnoreCase("profile")) {
            //just go back
            startActivity(new Intent(getApplicationContext(), Dashboard.class));
        } else {
            //after validation

            //send to server
            sendToServer();
        }
    }

    private void sendToServer() {

        //retrofit_auth and get token
        progressBar.setVisibility(View.VISIBLE);

        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request newRequest = chain.request().newBuilder()
                        .build();
                return chain.proceed(newRequest);
            }
        }).build();

        Retrofit retrofit = new Retrofit.Builder().client(client)
                .baseUrl(getString(R.string.base_url))
                .addConverterFactory(GsonConverterFactory.create()).build();
        NAIC service = retrofit.create(NAIC.class);

        UserRegisterRequest userRequest = new UserRegisterRequest();
        userRequest.setName(fullname.getText().toString());
        userRequest.setAge(age.getText().toString());
        userRequest.setPhone(phone.getText().toString());
        userRequest.setEmail(email.getText().toString());
        userRequest.setHeight(height.getText().toString());
        userRequest.setWeight(weight.getText().toString());
        userRequest.setEye_color(eye_color.getText().toString());
        userRequest.setHair_color(hair_color.getText().toString());
        userRequest.setTattoo(tatoo.getText().toString());
        userRequest.setSex(gender.getText().toString());
        userRequest.setMarital_status(marital_status.getText().toString());
        userRequest.setBlood_group(blood_group.getText().toString());
        userRequest.setGenotype(genotype.getText().toString());
        userRequest.setNok_name(nok.getText().toString());
        userRequest.setNok_phone(nok_phone.getText().toString());
        userRequest.setState_of_origin(state_origin.getText().toString());
        userRequest.setLga(lga.getText().toString());
        userRequest.setHometown(hometown.getText().toString());
        userRequest.setNin(nin.getText().toString());
        userRequest.setSec_sch_year_in(school_in.getText().toString());
        userRequest.setSec_sch_year_out(school_out.getText().toString());

        Call<ResponseBody> responseBodyCall = service.getSignUpResponse(userRequest);

        responseBodyCall.enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progressBar.setVisibility(View.GONE);
                Log.d("fingerprint", "Response Code: " + response.code());

                InputStream inputStr = response.body().byteStream();

                String jsonResponse = "";
                try {
                    jsonResponse = IOUtils.toString(inputStr, "UTF-8");
                    Log.d("fingerprint", "" + jsonResponse);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //validate response
                try {
                    if (response.code() == 200 || response.code() == 201) {
                        JSONParser parse = new JSONParser();
                        JSONObject jobj;
                        try {
                            jobj = (JSONObject) parse.parse(jsonResponse);

                            JSONObject jsonobj_1 = (JSONObject) jobj.get("data");

                            String army_number = (String) jsonobj_1.get("army_number");
                            String token = (String) jobj.get("token");

                            startActivity(new Intent(getApplicationContext(), Enroll.class)
                                    .putExtra("extra", extra)
                                    .putExtra("army_number", army_number)
                                    .putExtra("token", token)
                                    .putExtra("fullname", fullname.getText().toString())); //put all string extras
                        } catch (ParseException | NullPointerException e) {
                            e.printStackTrace();
                        }

                    } else {
                        Log.d("fingerprint", "Response RAW: " + response.raw());
                        //something went wrong. Try again.
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "Something went wrong try again", Toast.LENGTH_SHORT).show();
                    }
                } catch (NullPointerException n) {
                    n.printStackTrace();


                    //Toast.makeText(getApplicationContext(), "Something went wrong. Fill field appropriately and try again", Toast.LENGTH_SHORT).show();
//                    Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "Network Error", Toast.LENGTH_SHORT).show();

                Log.d("fingerprint", "Failed to upload: " + t.toString());

            }
        });

    }
}