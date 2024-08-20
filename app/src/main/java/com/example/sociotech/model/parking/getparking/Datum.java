package com.example.sociotech.model.parking.getparking;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("parking")
    @Expose
    private Parking parking;
    @SerializedName("allocation_date")
    @Expose
    private String allocationDate;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("expiration_date")
    @Expose
    private String expirationDate;
    @SerializedName("block")
    @Expose
    private Object block;
    @SerializedName("flat")
    @Expose
    private Object flat;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Parking getParking() {
        return parking;
    }

    public void setParking(Parking parking) {
        this.parking = parking;
    }

    public String getAllocationDate() {
        return allocationDate;
    }

    public void setAllocationDate(String allocationDate) {
        this.allocationDate = allocationDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public Object getBlock() {
        return block;
    }

    public void setBlock(Object block) {
        this.block = block;
    }

    public Object getFlat() {
        return flat;
    }

    public void setFlat(Object flat) {
        this.flat = flat;
    }
}
