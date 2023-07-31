package com.example.retrofit.Activity;

import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.retrofit.Api.ServiceApi;
import com.example.retrofit.Api.RetrofitClient;
import com.example.retrofit.Model.TodoModel;
import com.example.retrofit.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    RecyclerView rec_data,checkedRecyclerView;
    List<TodoModel> todoModelList = new ArrayList<>();
    List<TodoModel> checkedTodoList = new ArrayList<>();
    ProgressBar progressbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressbar = findViewById(R.id.progressbar);
        rec_data = findViewById(R.id.rec_data);
        rec_data.setLayoutManager(new LinearLayoutManager(this));

        checkedRecyclerView = findViewById(R.id.checkedRecyclerView);
        checkedRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        CheckedRecyclerViewAdapter checkedAdapter = new CheckedRecyclerViewAdapter(checkedTodoList);
        checkedRecyclerView.setAdapter(checkedAdapter);

        fetchDataFromApi();

    }
    private void fetchDataFromApi() {
        ServiceApi apiService = RetrofitClient.getRetrofitInstance().create(ServiceApi.class);
        Call<List<TodoModel>> call = apiService.getTodos();
        call.enqueue(new Callback<List<TodoModel>>() {
            @Override
            public void onResponse(Call<List<TodoModel>> call, Response<List<TodoModel>> response) {

                if (response.isSuccessful()) {
                    List<TodoModel> todos = response.body();
                    if (todos != null) {
                        todoModelList = todos;
                        Log.e("total size:: ", String.valueOf(todoModelList.size()));
                        rec_data.setAdapter(new ActivityAdapter());
                    }
                } else {
                    Log.e("API Error", "Error occurred: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<TodoModel>> call, Throwable t) {
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
            holder.checkBox.setOnCheckedChangeListener(null);


            holder.checkBox.setButtonDrawable(R.drawable.custom_checkbox);
            holder.checkBox.setChecked(model.isChecked());
            if (model.isChecked()) {
                holder.description.setPaintFlags(holder.description.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            }

            holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                model.setChecked(isChecked);
                if (isChecked) {
                    checkedTodoList.add(model);
                    todoModelList.remove(model);
                } else {
                    checkedTodoList.remove(model);
                    todoModelList.add(position, model);
                }
                notifyDataSetChanged();
                checkedRecyclerView.getAdapter().notifyDataSetChanged();
            });

        }



        @Override
        public int getItemCount() {
            return todoModelList.size();
        }

        public class RecycleHolder extends RecyclerView.ViewHolder {
            TextView description;
            CheckBox checkBox;

            public RecycleHolder(@NonNull View itemView) {
                super(itemView);
                description = itemView.findViewById(R.id.description);
                checkBox=itemView.findViewById(R.id.checkBox);

            }
        }
    }

    class CheckedRecyclerViewAdapter extends RecyclerView.Adapter<CheckedRecyclerViewAdapter.ViewHolder> {
        private List<TodoModel> checkedDataList;

        public CheckedRecyclerViewAdapter(List<TodoModel> checkedDataList) {
            this.checkedDataList = checkedDataList;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_data, parent, false);
            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            TodoModel model = checkedDataList.get(position);
            holder.description.setText(model.getTitle());
            holder.checkBox.setOnCheckedChangeListener(null);

            holder.checkBox.setChecked(model.isChecked());
            if (model.isChecked()) {
                holder.description.setPaintFlags(holder.description.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            }

            holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                model.setChecked(isChecked);
                if (isChecked) {
                    checkedTodoList.add(model);
                    todoModelList.remove(model);
                } else {
                    checkedTodoList.remove(model);
                    todoModelList.add(position, model);
                }
                notifyDataSetChanged();
                checkedRecyclerView.getAdapter().notifyDataSetChanged();
            });
        }

        @Override
        public int getItemCount() {
            return checkedDataList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView description;
            CheckBox checkBox;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                description = itemView.findViewById(R.id.description);
                checkBox = itemView.findViewById(R.id.checkBox);
            }
        }
    }
}

