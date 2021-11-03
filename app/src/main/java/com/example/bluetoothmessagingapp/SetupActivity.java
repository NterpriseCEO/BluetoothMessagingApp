package com.example.bluetoothmessagingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Objects;

public class SetupActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher {

    EditText enterUsername;
    Button clearUsername;
    Button confirmUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        //Username text input
        enterUsername = findViewById(R.id.usernameInput);
        enterUsername.addTextChangedListener(this);
        enterUsername.setOnEditorActionListener((textView, i, keyEvent) -> {
            if((keyEvent != null && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER) || i == EditorInfo.IME_ACTION_DONE) {
                //Go to the camera page when done / enter is pressed on the keyboard
                Intent gotToCamera = new Intent(this, CameraActivity.class);
                startActivity(gotToCamera);
            }
            return false;
        });

        //Clear username button
        clearUsername = findViewById(R.id.clearUsername);
        clearUsername.setOnClickListener(this);

        //Go button
        confirmUsername = findViewById(R.id.confirmUsername);
        confirmUsername.setOnClickListener(this);
        confirmUsername.setEnabled(false); // Disable by default
    }

    @Override
    public void onClick(View view) {
        //Clear the text input when clicked
        if(view.getId() == R.id.clearUsername) {
            enterUsername.setText("");
            confirmUsername.setEnabled(false);
        }else if(view.getId() == R.id.confirmUsername) {
            //Go to the camera page when clicked
            Intent gotToCamera = new Intent(this, CameraActivity.class);
            startActivity(gotToCamera);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

    @Override
    public void afterTextChanged(Editable editable) {
        String username = enterUsername.getText().toString();
        //Enables the go button when the text input is longer than 0 characters
        confirmUsername.setEnabled(username.length() > 0);
    }
}