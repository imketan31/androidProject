package com.demo.studentapp.apis;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.*;

public interface RestApis {

    @Headers({"Accept:application/json", "Content-Type:application/json"})
    @POST("login")
    Call<JsonObject> authenticate(@Body JsonObject user);

    @Headers({"Accept:application/json", "Content-Type:application/json"})
    @GET("students")
    Call<JsonElement> getAllStudents();

    @Headers({"Accept:application/json", "Content-Type:application/json"})
    @POST("students")
    Call<JsonElement> addData(@Body JsonObject jsonObject);

    @Headers({"Accept:application/json", "Content-Type:application/json"})
    @GET("students/get-classTeacher")
    Call<JsonElement> getclassteacher();



    @Headers({"Accept:application/json", "Content-Type:application/json"})
    @POST("students/login-classteacher")
    Call<JsonElement> login(@Body JsonObject jsonObject);
}
