package com.demo.studentapp;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.demo.studentapp.apis.RestApis;
import com.demo.studentapp.apis.ServiceBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudentFragment extends Fragment {
    EditText firstName, lastName, div, std, rollNo;
    String sfirstName, slastName, sdiv, sstd, sclassTeacher="1";
    Spinner classTeacher;
    Button btn_submit;
    RestApis restApis;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_student, container, false);
        firstName = view.findViewById(R.id.firstName);
        lastName = view.findViewById(R.id.lastName);
        div = view.findViewById(R.id.div);
        std = view.findViewById(R.id.std);
        btn_submit = view.findViewById(R.id.btn_submit);
        classTeacher = view.findViewById(R.id.classTeacher);
        restApis = ServiceBuilder.getService(getContext());
        getClassTexh();

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postData();
            }
        });

        return view;
    }

    private void getClassTexh() {
        Call<JsonElement> call = restApis.getclassteacher();
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {

                if (response.code() == 200) {

                    JsonArray jsonArray = response.body().getAsJsonArray();
                    Log.e("LLNBB",jsonArray.toString());
                    ArrayList<String> arrayList = new ArrayList<>();
                    ArrayList<String> arrayList2 = new ArrayList<>();
                    for (int i = 0; i < jsonArray.size(); i++) {

                        arrayList.add(jsonArray.get(i).getAsJsonObject().get("firstName").getAsString());
                        arrayList2.add(jsonArray.get(i).getAsJsonObject().get("id").getAsString());
                    }

//

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                            R.layout.support_simple_spinner_dropdown_item, arrayList);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    classTeacher.setAdapter(adapter);

                    classTeacher.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            sclassTeacher = arrayList2.get(position);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            Log.e("kbjbvd3", "" + classTeacher.getSelectedItemPosition());
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Toast.makeText(getContext(), " kkk ", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void postData() {


        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("firstName", firstName.getText().toString());
        jsonObject.addProperty("lastName", lastName.getText().toString());
        jsonObject.addProperty("division", div.getText().toString());
        jsonObject.addProperty("standard", std.getText().toString());
        jsonObject.addProperty("rollNo", rollNo.getText().toString());
        jsonObject.addProperty("status", "Active");
        JsonObject object = new JsonObject();
        object.addProperty("id", sclassTeacher);
        jsonObject.add("classTeacher", object);
        Call<JsonElement> call = restApis.addData(jsonObject);
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                if (response.code() == 201 || response.code() == 200) {
                    Toast.makeText(getContext(), "Student added Sucessfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Server Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {

            }
        });

    }


}
