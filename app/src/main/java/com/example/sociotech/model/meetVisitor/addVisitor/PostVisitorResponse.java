package com.example.sociotech.model.meetVisitor.addVisitor;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PostVisitorResponse {
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("data")
    @Expose
    private DataDetails data;
    @SerializedName("message")
    @Expose
    private String message;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public DataDetails getData() {
        return data;
    }

    public void setData(DataDetails data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
