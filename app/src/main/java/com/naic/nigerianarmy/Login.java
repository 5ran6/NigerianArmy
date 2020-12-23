package com.naic.nigerianarmy;

import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.EditorInfo;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.transition.Transition;
import androidx.transition.TransitionValues;

import com.google.gson.Gson;
import com.naic.nigerianarmy.interfaces.NAIC;
import com.naic.nigerianarmy.models.LoginRequest;
import com.naic.nigerianarmy.models.UserResponse;
import com.naic.nigerianarmy.utils.LfsJavaWrapperDefinesMinutiaN;
import com.naic.nigerianarmy.utils.Tools;
import com.suprema.android.BioMiniJni;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.SerializationUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Objects;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.naic.nigerianarmy.GbExampleGrayScaleBitmapClass.GetBippiisDirectoryName;


public class Login extends AppCompatActivity {
    private AppCompatEditText bip;
    private String token = "";
    private String bippiis, getBippiis, extra;
    private ProgressBar progressBar;
    private AppCompatImageView img;
    private String mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        extra = "verify";
        bip = findViewById(R.id.bippiis_no);
        mode = getIntent().getStringExtra("extra");

//        //to be removed
//        bip.setText("O/S 2243");
//        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(Login.this, new OnSuccessListener<InstanceIdResult>() {
//            @Override
//            public void onSuccess(InstanceIdResult instanceIdResult) {
//                mToken = instanceIdResult.getToken();
//                Log.e("Token", mToken);
//                SharedPreferences pref = getApplicationContext().getSharedPreferences("bippiis", MODE_PRIVATE);
//                SharedPreferences.Editor editor = pref.edit();
//                editor.putString("firebaseToken", mToken);
//                editor.apply(); //commit changes
//            }
//        });

        progressBar = findViewById(R.id.progress);
        img = findViewById(R.id.img);


        final Animation animation = new AlphaAnimation(1, 0); // Change alpha from fully visible to invisible
        new Transition() {
            @Override
            public void captureEndValues(@NonNull TransitionValues transitionValues) {

            }

            @Override
            public void captureStartValues(@NonNull TransitionValues transitionValues) {

            }
        };
        animation.setDuration(2000); // duration - 2 seconds
        animation.setInterpolator(new LinearInterpolator()); // do not alter animation rate
        animation.setRepeatCount(3); // Repeat animation infinitely
        animation.setRepeatMode(Animation.REVERSE); // Reverse animation at the end so the button will fade back in
        img.startAnimation(animation);

        bip.setOnEditorActionListener((v, actionId, event) -> {
            boolean handled = false;
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                hideSoftKeyboard();
                Toast.makeText(getApplicationContext(), "Verifying ID..", Toast.LENGTH_SHORT).show();
                login();
                handled = true;
            }
            return handled;

        });

    }

    //DONE
    public void hideSoftKeyboard() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    public void proceed(View view) {
        login();
    }

    private void login() {
        bippiis = Objects.requireNonNull(bip.getText()).toString().trim();

        if (bippiis.isEmpty()) {
            Toast.makeText(getApplicationContext(), "STAFF ID NUMBER CANNOT BE EMPTY!", Toast.LENGTH_SHORT).show();
        } else {
            getBippiis = bip.getText().toString().trim();

            //retrofit_auth and get token
            progressBar.setVisibility(View.VISIBLE);

            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
                @Override
                public okhttp3.Response intercept(Chain chain) throws IOException {
                    Request newRequest = chain.request().newBuilder()
                            .addHeader("Authorization", "Bearer " + token)
                            .build();
                    return chain.proceed(newRequest);
                }
            }).build();

            Retrofit retrofit = new Retrofit.Builder().client(client)
                    .baseUrl(getString(R.string.base_url))
                    .addConverterFactory(GsonConverterFactory.create()).build();
            NAIC service = retrofit.create(NAIC.class);

            LoginRequest loginRequest = new LoginRequest();
            loginRequest.setBippiis_number(Objects.requireNonNull(bip.getText()).toString().trim());
            //  loginRequest.setFirebaseToken(mToken);

            Call<UserResponse> ResponseBodyCall = service.getLoginResponse(loginRequest);


            ResponseBodyCall.enqueue(new Callback<UserResponse>() {
                @Override
                public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                    progressBar.setVisibility(View.GONE);
                    Log.d("fingerprint", "Response Code: " + response.code());
//                    Log.d("fingerprint",   getBippiis + " edited STAFF ID: " + bippiis + " token : " + token + " firebase token : " + mToken);
                    // Log.d("fingerprint",   getBippiis + " edited STAFF ID: " + bippiis + " token : " + token);

                    //validate response
                    try {
                        if (response.code() >= 200) {
//                            InputStream inputStr = response.body() != null ? response.body().byteStream() : null;
//
//                            String jsonResponse = "";
//
//
//                            try {
//                                jsonResponse = IOUtils.toString(inputStr, "UTF-8");
//                                Log.d("fingerprint", "" + jsonResponse);
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }

                            if (!response.body().getData().isEmpty()) {

                                JSONParser parse = new JSONParser();
                                JSONObject jobj;
                                try {
                                    jobj = (JSONObject) parse.parse(response.body().getData());

                                 //   JSONObject jsonobj_1 = (JSONObject) jobj.get("data");

                                    String army_number = (String) jobj.get("army_number");
                                    String name = (String) jobj.get("name");
                                    String age = (String) jobj.get("age");
                                    String height = (String) jobj.get("height");
                                    String eye_color = (String) jobj.get("eye_color");
                                    String hair_color = (String) jobj.get("hair_color");
                                    String tattoo = (String) jobj.get("tattoo");
                                    String sex = (String) jobj.get("sex");
                                    String phone = (String) jobj.get("phone");
                                    String weight = (String) jobj.get("weight");
                                    String marital_status = (String) jobj.get("marital_status");
                                    String blood_group = (String) jobj.get("blood_group");
                                    String state_of_origin = (String) jobj.get("state_of_origin");
                                    String lga = (String) jobj.get("lga");
                                    String hometown = (String) jobj.get("hometown");
                                    String nin = (String) jobj.get("nin");
                                    String sec_sch_year_in = (String) jobj.get("sec_sch_year_in");
                                    String sec_sch_year_out = (String) jobj.get("sec_sch_year_out");
                                    String nok_name = (String) jobj.get("nok_name");
                                    String nok_phone = (String) jobj.get("nok_phone");


                                    // store fingerprint in arrayList

                                    ArrayList<String> fingeprints = new ArrayList<String>();
                                    org.json.simple.JSONArray biometrics = new org.json.simple.JSONArray();
                                    biometrics = (org.json.simple.JSONArray) jobj.get("biometrics");

                                    assert biometrics != null;
                                    int len = biometrics.size();

                                    for (int j = 0; j < len; j++) {
                                        JSONObject json = (JSONObject) biometrics.get(j);
                                        fingeprints.add((String) json.get("fingerprint"));
                                        //    Log.d("myProbe", fingeprints.get(j));
                                        byte[] bytes = Base64.decode(fingeprints.get(j), Base64.DEFAULT);
                                        LfsJavaWrapperDefinesMinutiaN[] Probe = deSerialize(bytes);
                                        File file = new File(GetBippiisDirectoryName(),
                                                "temp_" + j + ".json");
                                        try {
                                            // Serialize Java object into JSON file.
                                            Gson gson = new Gson();
                                            String json1 = gson.toJson(Probe);

                                            //           mapper.writeValue(file, json);
                                            FileWriter fw = new FileWriter(file.getAbsolutePath());
                                            fw.write(json1);
                                            fw.close();

                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }

                                        Log.d("Fingerprint", "Closed " + j + " successfully");

                                    }
                                    if (extra.equalsIgnoreCase("profile"))
                                        startActivity(new Intent(getApplicationContext(), BioData.class)
                                                .putExtra("extra", extra)
                                                .putExtra("army_number", army_number)
                                                .putExtra("name", name)
                                                .putExtra("age", age)
                                                .putExtra("height", height)
                                                .putExtra("eye_color", eye_color)
                                                .putExtra("hair_color", hair_color)
                                                .putExtra("tattoo", tattoo)
                                                .putExtra("sex", sex)
                                                .putExtra("phone", phone)
                                                .putExtra("weight", weight)
                                                .putExtra("marital_status", marital_status)
                                                .putExtra("blood_group", blood_group)
                                                .putExtra("state_of_origin", state_of_origin)
                                                .putExtra("lga", lga)
                                                .putExtra("hometown", hometown)
                                                .putExtra("nin", nin)
                                                .putExtra("sec_sch_year_in", sec_sch_year_in)
                                                .putExtra("sec_sch_year_out", sec_sch_year_out)
                                                .putExtra("nok_name", nok_name)
                                                .putExtra("nok_phone", nok_phone)

                                        ); //put all string extras
                                    if (extra.equalsIgnoreCase("verify"))
                                        startActivity(new Intent(getApplicationContext(), Enroll.class).putExtra("fullname", name).putExtra("extra", extra));
                                } catch (ParseException | NullPointerException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                Log.d("fingerprint", "Response RAW: " + response.raw());

                                Toast.makeText(getApplicationContext(), "INVALID STAFF ID NUMBER", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            //something went wrong. Try again.
                            Toast.makeText(getApplicationContext(), "INVALID STAFF ID NUMBER. Try again", Toast.LENGTH_SHORT).show();
                        }
                    } catch (NullPointerException n) {
                        n.printStackTrace();

                        Toast.makeText(getApplicationContext(), "INVALID STAFF ID NUMBER", Toast.LENGTH_SHORT).show();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<UserResponse> call, Throwable t) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "Network Error", Toast.LENGTH_SHORT).show();

                    Log.d("fingerprint", "Failed to Verify: " + t.getLocalizedMessage());

                }
            });
        }
    }

    private long exitTime = 0;

    public void doExitApp() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Tools.toast("Press again to go back", Login.this);
            exitTime = System.currentTimeMillis();
        } else {
            finish();
        }
    }


    @Override
    public void onBackPressed() {
        doExitApp();
    }

    private LfsJavaWrapperDefinesMinutiaN[] deSerialize(byte[] templateCode) {
        return SerializationUtils.deserialize(templateCode);
    }


}
