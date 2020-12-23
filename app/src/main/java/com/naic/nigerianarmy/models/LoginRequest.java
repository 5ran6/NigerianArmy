package com.naic.nigerianarmy.models;

import com.google.gson.annotations.SerializedName;

public class LoginRequest {
    @SerializedName("army_number")
    private String army_number;

    public String getBippiis_number() {
        return army_number;
    }

    public void setBippiis_number(String army_number) {
        this.army_number = army_number;
    }
}
