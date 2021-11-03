package com.example.bluetoothmessagingapp.chatslist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Menu;

import com.example.bluetoothmessagingapp.R;

import java.util.ArrayList;
import java.util.List;

public class ChatsActivity extends AppCompatActivity {

    private List chats = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chats);

        //Sets the toolbar at the top of the screen
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Test data for now
        chats.add("Chat 1");
        chats.add("Chat 2");
        chats.add("Chat 3");
        chats.add("Chat 4");
        chats.add("Chat 5");
        chats.add("Chat 1");
        chats.add("Chat 2");
        chats.add("Chat 3");
        chats.add("Chat 4");
        chats.add("Chat 5");

        //Creates a recyclerView using the chats adapter
        RecyclerView recyclerView = findViewById(R.id.chatsList);
        ChatsAdapter chatsAdapter = new ChatsAdapter(chats);

        //Allows for the recyclerView to be scrolled
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(chatsAdapter);
    }

    @SuppressLint("ResourceType")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.layout.chats_list_actions, menu);
        return true;
    }
}