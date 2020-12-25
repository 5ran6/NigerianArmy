package com.naic.nigerianarmy.interfaces;


import com.naic.nigerianarmy.models.FingerprintRequest;
import com.naic.nigerianarmy.models.FingerprintResponse;
import com.naic.nigerianarmy.models.LoginRequest;
import com.naic.nigerianarmy.models.PassportRequest;
import com.naic.nigerianarmy.models.UserRegisterRequest;
import com.naic.nigerianarmy.models.ArmyUserResponse;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface NAIC {
//    POST ->  https://bippiis.com/api/v1/user/uploadPassport
//    data -> passport, type base64 string
//
//    POST -> https://bippiis.com/api/v1/enroll
//    data{ fingerprints, type array, bippiis_number, type string}
//
//    GET -> https://bippiis.com/api/v1/user
//    data{ fingerprints, type array, bippiis_number, type string}

    @POST("enrollFingerPrint")
    Call<FingerprintResponse> getFingerprintResponse(@Body FingerprintRequest fingerprintRequest);

    @POST("uploadPassport")
    Call<ResponseBody> getPassportResponse(@Body PassportRequest passportRequest);

    @POST("verifyUser")
    Call<ArmyUserResponse> getLoginResponse(@Body LoginRequest loginRequest);

    @POST("signup")
    Call<ResponseBody> getSignUpResponse(@Body UserRegisterRequest userRequest);

//    @GET("user")
//    Call<ResponseBody> getUserResponse();
}
