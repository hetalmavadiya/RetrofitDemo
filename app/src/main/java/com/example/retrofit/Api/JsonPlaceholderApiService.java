package com.example.retrofit.Api;
import com.example.retrofit.Model.TodoModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;

public interface JsonPlaceholderApiService {
    @GET("todos")
    Call<List<TodoModel>> getTodos();
}
