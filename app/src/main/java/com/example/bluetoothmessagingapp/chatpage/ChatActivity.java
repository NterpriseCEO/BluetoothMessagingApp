package com.example.bluetoothmessagingapp.chatpage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.bluetoothmessagingapp.R;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        //Sets the toolbar at the top of the screen
        Toolbar toolbar = findViewById(R.id.chatTitle);
        toolbar.setTitle(getIntent().getExtras().getString("username"));
        setSupportActionBar(toolbar);
        //The back button for the chat
        toolbar.setNavigationIcon(ContextCompat.getDrawable(ChatActivity.this, R.drawable.ic_baseline_arrow_back_24));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public void onClick(View view) {

    }
}