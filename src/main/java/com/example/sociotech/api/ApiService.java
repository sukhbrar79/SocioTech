package com.example.sociotech.api;

import com.example.sociotech.model.announcements.AnnouncementsResponse;
import com.example.sociotech.model.emergencyContacts.EmergencyContactsResponse;
import com.example.sociotech.model.flatdetails.BlockResponse;
import com.example.sociotech.model.flatdetails.FlatResponse;
import com.example.sociotech.model.guidelines.GuidelinesResponse;
import com.example.sociotech.model.invoices.InvoicesResponse;
import com.example.sociotech.model.login.LoginResponse;
import com.example.sociotech.model.logout.LogoutResponse;
import com.example.sociotech.model.meetVisitor.addVisitor.PostVisitorResponse;
import com.example.sociotech.model.meetVisitor.getVisitor.GetVisitorResponse;
import com.example.sociotech.model.parking.addParking.PostParkingResponse;
import com.example.sociotech.model.parking.getparking.GetParkingResponse;
import com.example.sociotech.model.parking.parkingAvailableSlots.ParkingAvailableSlotsResponse;
import com.example.sociotech.model.profile.MyProfileResponse;
import com.example.sociotech.model.serviceRequest.postservicRequestModel.PostServiceRequestResponse;
import com.example.sociotech.model.serviceRequest.serviceRequestListingResponse.ServiceRequestResponse;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;


public interface ApiService {

    @POST("api/login")
    Call<LoginResponse> login(@Body JsonObject jsonObject);


    @POST("api/logout")
    Call<LogoutResponse> logout();

    @POST("api/complaints")
    Call<PostServiceRequestResponse> serviceRequest(@Body JsonObject jsonObject);

    @GET("api/complaints")
    Call<ServiceRequestResponse> serviceRequestStatus();

    @GET("api/guidelines")
    Call<GuidelinesResponse> getGuidelines();

    @GET("api/profile")
    Call<MyProfileResponse> myProfile();

    @PUT("api/profile")
    Call<MyProfileResponse> updateProfile(@Body JsonObject jsonObject);

    @GET("api/visitors")
    Call<GetVisitorResponse> getmyVisitors();

    @POST("api/visitors")
    Call<PostVisitorResponse> addVisitor(@Body JsonObject jsonObject);

    @GET("api/parking")
    Call<GetParkingResponse> getmyParking();

    @POST("api/parking")
    Call<PostParkingResponse> addParking(@Body JsonObject jsonObject);

    @GET("api/parking_slots")
    Call<ParkingAvailableSlotsResponse> getAvailableSlots();

    @GET("api/flats")
    Call<FlatResponse> getFlats();

    @GET("api/blocks")
    Call<BlockResponse> getBlocks();


    @GET("api/emergencyDetails")
    Call<EmergencyContactsResponse> getEmergencyContacts();

    @GET("api/invoice")
    Call<InvoicesResponse> getInvoice();


    @GET("api/notice-board")
    Call<AnnouncementsResponse> getNoticeBoard();
}
