package com.example.a56;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface StudentService {
    @GET("/names/players/list")
    Call<ArrayList<Student>> getStudents();

    @POST("/names/players/add")
    Call<Student> putStudent(@Body Student student);
}
