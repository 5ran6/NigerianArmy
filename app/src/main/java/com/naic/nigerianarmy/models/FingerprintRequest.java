package com.naic.nigerianarmy.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class FingerprintRequest {

    @SerializedName("fingerprints")
    private ArrayList<String> fingerprints;
    @SerializedName("fingerprint_images")
    private ArrayList<String> fingerprintsImages;



    public ArrayList<String> getFingerprints() {
        return fingerprints;
    }

    public void setFingerprints(ArrayList<String> fingerprints) {
        this.fingerprints = fingerprints;
    }

    public ArrayList<String> getFingerprintsImages() {
        return fingerprintsImages;
    }

    public void setFingerprintsImages(ArrayList<String> fingerprintsImages) {
        this.fingerprintsImages = fingerprintsImages;
    }
}
