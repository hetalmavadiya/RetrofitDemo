package com.example.retrofit.Api;
import com.example.retrofit.Model.TodoModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ServiceApi {
    @GET("todos")
    Call<List<TodoModel>> getTodos();
}
