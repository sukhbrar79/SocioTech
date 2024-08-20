package com.example.sociotech.model.parking.parkingAvailableSlots;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AvailableSlots {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("spot_number")
    @Expose
    private String spotNumber;
    @SerializedName("spot_type")
    @Expose
    private String spotType;
    @SerializedName("location")
    @Expose
    private String location;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSpotNumber() {
        return spotNumber;
    }

    public void setSpotNumber(String spotNumber) {
        this.spotNumber = spotNumber;
    }

    public String getSpotType() {
        return spotType;
    }

    public void setSpotType(String spotType) {
        this.spotType = spotType;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
