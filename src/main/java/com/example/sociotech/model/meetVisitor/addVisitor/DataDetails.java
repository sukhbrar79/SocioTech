package com.example.sociotech.model.meetVisitor.addVisitor;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DataDetails {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("contact_number")
    @Expose
    private String contactNumber;
    @SerializedName("purpose")
    @Expose
    private String purpose;
    @SerializedName("vehicle_number")
    @Expose
    private String vehicleNumber;
    @SerializedName("block_id")
    @Expose
    private String blockId;
    @SerializedName("flat_id")
    @Expose
    private String flatId;
    @SerializedName("check_in_time")
    @Expose
    private Object checkInTime;
    @SerializedName("check_out_time")
    @Expose
    private Object checkOutTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public String getBlockId() {
        return blockId;
    }

    public void setBlockId(String blockId) {
        this.blockId = blockId;
    }

    public String getFlatId() {
        return flatId;
    }

    public void setFlatId(String flatId) {
        this.flatId = flatId;
    }

    public Object getCheckInTime() {
        return checkInTime;
    }

    public void setCheckInTime(Object checkInTime) {
        this.checkInTime = checkInTime;
    }

    public Object getCheckOutTime() {
        return checkOutTime;
    }

    public void setCheckOutTime(Object checkOutTime) {
        this.checkOutTime = checkOutTime;
    }
}
