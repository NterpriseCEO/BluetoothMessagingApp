package com.example.bluetoothmessagingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.Objects;

public class SetupActivity extends AppCompatActivity implements View.OnClickListener {

    EditText enterUsername;
    Button clearUsername;
    Button confirmUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        enterUsername = findViewById(R.id.usernameInput);

        clearUsername = findViewById(R.id.clearUsername);
        clearUsername.setOnClickListener(this);

        confirmUsername = findViewById(R.id.confirmUsername);
        confirmUsername.setOnClickListener(this);

        Objects.requireNonNull(getSupportActionBar()).hide();
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.clearUsername) {
            enterUsername.setText("");
        }else if(view.getId() == R.id.confirmUsername) {
            Intent gotToCamera = new Intent(this, CameraActivity.class);
            startActivity(gotToCamera);
        }
    }
}