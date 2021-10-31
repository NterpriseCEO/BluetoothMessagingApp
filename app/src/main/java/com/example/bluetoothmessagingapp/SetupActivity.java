package com.example.bluetoothmessagingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.Objects;

public class SetupActivity extends AppCompatActivity implements View.OnClickListener {

    EditText enterUsername;
    Button clearUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        enterUsername = findViewById(R.id.usernameInput);

        clearUsername = findViewById(R.id.clearUsername);
        clearUsername.setOnClickListener(this);
        Objects.requireNonNull(getSupportActionBar()).hide();
    }

    @Override
    public void onClick(View view) {
        enterUsername.setText("");
    }
}