package com.demo.studentapp.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.demo.studentapp.MainActivity;
import com.demo.studentapp.R;
import com.demo.studentapp.apis.RestApis;
import com.demo.studentapp.apis.ServiceBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    Button login;
    TextView et_username, et_password;
    RestApis restApis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login = findViewById(R.id.login);
        restApis = ServiceBuilder.getService(getApplicationContext());
        et_username = findViewById(R.id.et_username);
        et_password = findViewById(R.id.et_password);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dologin();

            }
        });
    }

    private void dologin() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("userName", et_password.getText().toString());
        jsonObject.addProperty("pass", et_password.getText().toString());
        Call<JsonElement> call = restApis.login(jsonObject);
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                if (response.code() == 200) {
                    Toast.makeText(LoginActivity.this, "Login Success", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(LoginActivity.this, "Login Failed Try again", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {

            }
        });
    }
}