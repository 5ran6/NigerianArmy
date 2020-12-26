package com.naic.nigerianarmy;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.naic.nigerianarmy.interfaces.NAIC;
import com.naic.nigerianarmy.models.PassportRequest;
import com.naic.nigerianarmy.models.PassportResponse;
import com.naic.nigerianarmy.models.storageFile;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CameraCapture extends AppCompatActivity {
    ImageView imageViewCompat;
    AppCompatButton home;
    String token = "";
    String army_number = "";
    ProgressBar progressBar;
    //captured picture uri
    private Uri mImageUri;
    private String imageString;
    private boolean success = false;
    private byte[] byteArray;

    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;
    private String fromSharedPref = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_capture);

        sharedPref = getPreferences(Context.MODE_PRIVATE);
        editor = sharedPref.edit();
        fromSharedPref = sharedPref.getString("address", "nothing is here");

        imageViewCompat = findViewById(R.id.imageView);
        home = findViewById(R.id.home);
        progressBar = findViewById(R.id.progress);
        token = getIntent().getStringExtra("token");
        army_number = getIntent().getStringExtra("army_number");
        Log.d("fingerprint", "Token: " + token);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {
                //   Uri imageUri = data.getData();
                imageViewCompat.setImageURI(result.getUri());
                home.setEnabled(true);
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), result.getUri());
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 10, stream);
                    byteArray = stream.toByteArray();
                    imageString = Base64.encodeToString(byteArray, Base64.DEFAULT);
                    bitmap.recycle();

                } catch (IOException e) {
                    e.printStackTrace();
                }


                //get bytes, compress and send asynchronously


            }

        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
            Exception e = CropImage.getActivityResult(data).getError();
            Toast.makeText(this, "Possible Error: " + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }


    public void capture(View view) {
        success = false;
        CropImage.activity().start(CameraCapture.this);
    }

    public void home(View view) {
        if (success) {


            //delete directory
//            File dir = new File(Environment.getExternalStorageDirectory()+"Dir_name_here");
            File dir = new File(GetGreenbitDirectoryName());
            if (dir.isDirectory()) {
                String[] children = dir.list();
                for (String child : children) {
                    new File(dir, child).delete();
                }
                Log.d("fingerprint", "Deleted Greenbit folder successfully");
            }
            finish();
            startActivity(new Intent(getApplicationContext(), SuccessActivity.class).putExtra("extra", army_number));
        } else
            uploadImage();
    }

    public static String GetGreenbitDirectoryName() {
        String path = Environment.getExternalStorageDirectory().toString();
        File file = new File(path, "Greenbit");
        boolean success = true;
        if (!file.exists()) {
            success = file.mkdir();
        }
        path = file.getPath();
        return path;
    }

    private void uploadImage() {
        home.setEnabled(false);
        home.setText("Uploading...");
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
                .baseUrl(fromSharedPref)
                .addConverterFactory(GsonConverterFactory.create()).build();
        NAIC service = retrofit.create(NAIC.class);

        PassportRequest passportRequest = new PassportRequest();
        passportRequest.setPassport("data:image/jpeg;base64," + imageString);
        passportRequest.setArmy_number(army_number);

        Log.d("fingerprint", "ArmyNo  " + army_number);


        Call<ResponseBody> passportResponseCall = service.getPassportResponse(passportRequest);
        passportResponseCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                // TODO: still need to catch errors properly from accurate response filters
                Log.d("fingerprint", response.code() + ": " + response.message() + ":: " + response.body());
                if (response.code() == 200 || response.code() == 201) {
                    Log.d("fingerprint", "Uploaded successfully message " + response.message());
                    Log.d("fingerprint", "Uploaded successfully body " + response.body());
                    Log.d("fingerprint", "Uploaded successfully toString " + response.toString());
                    Log.d("fingerprint", "Uploaded successfully raw " + response.raw());
                    Log.d("fingerprint", "Uploaded successfully BASE64 IMG:  " + imageString);

                    progressBar.setVisibility(View.GONE);
                    home.setBackgroundColor(getResources().getColor(R.color.green_400));
                    home.setEnabled(true);
                    success = true;
                    home.setText("Done");
                    Log.d("fingerprint", "Uploaded successfully " + response);
                    home.performClick(); //to take you to next activity
                    //validate response

                } else {
                    Toast.makeText(getApplicationContext(), "Something went wrong. Try again!", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                success = false;
                home.setEnabled(true);
                home.setText("Retry");
                home.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                Log.d("fingerprint", "Failed to Upload: " + t.toString());
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        storageFile.fingerPrint.allFingerprints.clear();
    }
}
