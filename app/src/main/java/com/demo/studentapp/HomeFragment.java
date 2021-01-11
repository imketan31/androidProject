package com.demo.studentapp;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.studentapp.apis.RestApis;
import com.demo.studentapp.apis.ServiceBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    RecyclerView recycle;
    ListAdapter adapter;
    RestApis restApis;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        restApis = ServiceBuilder.getService(getContext());
        recycle = rootView.findViewById(R.id.recycle);

        getAllData();


        return rootView;
    }

    private void getAllData() {
        Call<JsonElement> call = restApis.getAllStudents();
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                if (response.code() == 200) {
                    JsonArray jsonArray = response.body().getAsJsonArray();
                    Log.e("jsonArray",jsonArray.toString());
                    adapter = new ListAdapter(jsonArray, getContext());
                    recycle.setHasFixedSize(true);
                    recycle.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                    recycle.getRecycledViewPool().setMaxRecycledViews(1, 0);
                    recycle.setAdapter(adapter);
                } else {
                    Toast.makeText(getContext(), "Server error", Toast.LENGTH_SHORT);
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
Log.e("",t.toString());
Toast.makeText(getContext(),t.toString(),Toast.LENGTH_SHORT).show();
            }
        });

    }

    class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {


        private List<JsonElement> list;
        private final JsonArray jsonArray;
        private final Context context;

        public ListAdapter(JsonArray jsonArray, Context context) {
            this.jsonArray = jsonArray;
            this.context = context;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
            View view = layoutInflater.inflate(R.layout.item, viewGroup, false);
            ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            JsonObject jsonObject = jsonArray.get(position).getAsJsonObject();
            holder.name.setText(jsonObject.get("firstName").getAsString() + " " +jsonObject.get("lastName").getAsString() );
            holder.div.setText(jsonObject.get("division").getAsString());
            holder.classTC.setText(jsonObject.get("classTeacher").getAsJsonObject().get("firstName").getAsString()+" "+jsonObject.get("classTeacher").getAsJsonObject().get("lastName").getAsString());
            holder.roll.setText(jsonObject.get("rollNo").getAsString());
            holder.std.setText(jsonObject.get("standard").getAsString());
        }

        @Override
        public int getItemCount() {
            return jsonArray.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView classTC, std, div, roll,name;
            Button btn;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                classTC = itemView.findViewById(R.id.classTC);
                std = itemView.findViewById(R.id.std);
                div = itemView.findViewById(R.id.div);
                name = itemView.findViewById(R.id.name);
                roll = itemView.findViewById(R.id.roll);
                btn = itemView.findViewById(R.id.btn);
            }
        }
    }
}
