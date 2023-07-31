package com.example.retrofit.Activity;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.retrofit.Api.JsonPlaceholderApiService;
import com.example.retrofit.Api.RetrofitClient;
import com.example.retrofit.Model.TodoModel;
import com.example.retrofit.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    RecyclerView rec_data;
    List<TodoModel> todoModelList;
    ProgressBar progressbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressbar=findViewById(R.id.progressbar);
        rec_data = findViewById(R.id.rec_data);
        rec_data.setLayoutManager(new LinearLayoutManager(this));
        fetchDataFromApi();

    }
    private void fetchDataFromApi() {
        progressbar.setVisibility(View.VISIBLE);
        JsonPlaceholderApiService apiService = RetrofitClient.getRetrofitInstance().create(JsonPlaceholderApiService.class);
        Call<List<TodoModel>> call = apiService.getTodos();
        call.enqueue(new Callback<List<TodoModel>>() {
            @Override
            public void onResponse(Call<List<TodoModel>> call, Response<List<TodoModel>> response) {
                progressbar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    List<TodoModel> todos = response.body();
                    if (todos != null) {
                        todoModelList = todos;
                        rec_data.setAdapter(new ActivityAdapter());
                    }
                } else {
                    Log.e("API Error", "Error occurred: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<TodoModel>> call, Throwable t) {
                progressbar.setVisibility(View.GONE);
                Log.e("API Error", "API call failed: " + t.getMessage());
            }
        });
    }

    class ActivityAdapter extends RecyclerView.Adapter<ActivityAdapter.RecycleHolder> {

        @NonNull
        @Override
        public RecycleHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_data, parent, false);
            return new RecycleHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull RecycleHolder holder, int position) {
            TodoModel model = todoModelList.get(position);
            holder.description.setText(model.getTitle());
        }

        @Override
        public int getItemCount() {
            return todoModelList.size();
        }

        public class RecycleHolder extends RecyclerView.ViewHolder {
            TextView description;

            public RecycleHolder(@NonNull View itemView) {
                super(itemView);
                description = itemView.findViewById(R.id.description);
            }
        }
    }
}
