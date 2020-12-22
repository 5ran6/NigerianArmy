package com.naic.nigerianarmy.interfaces;


import com.naic.nigerianarmy.models.FingerprintRequest;
import com.naic.nigerianarmy.models.FingerprintResponse;
import com.naic.nigerianarmy.models.LoginRequest;
import com.naic.nigerianarmy.models.PassportRequest;
import com.naic.nigerianarmy.models.PassportResponse;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface BIPPIIS {
//    POST ->  https://bippiis.com/api/v1/user/uploadPassport
//    data -> passport, type base64 string
//
//    POST -> https://bippiis.com/api/v1/enroll
//    data{ fingerprints, type array, bippiis_number, type string}
//
//    GET -> https://bippiis.com/api/v1/user
//    data{ fingerprints, type array, bippiis_number, type string}

    @POST("enroll")
    Call<FingerprintResponse> getFingerprintResponse(@Body FingerprintRequest fingerprintRequest);

    @POST("user/uploadPassport")
    Call<PassportResponse> getPassportResponse(@Body PassportRequest passportRequest);

    @POST("auth")
    Call<ResponseBody> getLoginResponse(@Body LoginRequest loginRequest);

    @GET("user")
    Call<ResponseBody> getUserResponse();
}
