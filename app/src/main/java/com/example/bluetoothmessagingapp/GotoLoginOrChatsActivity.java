package com.example.bluetoothmessagingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.os.Bundle;

import com.example.bluetoothmessagingapp.chatslist.ChatsActivity;
import com.example.bluetoothmessagingapp.database.DatabaseFunctions;

public class GotoLoginOrChatsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goto_login_or_chats);

        DatabaseFunctions db = new DatabaseFunctions(this);
        db.open();
        Cursor cursor = db.getLocalUser();
        //Goes to the chat page if the user has set up the app already
        //i.e. if there is a local user in the DB
        if(cursor.getCount() > 0) {
            Intent gotToChats = new Intent(this, ChatsActivity.class);
            startActivity(gotToChats);
        }else {
            //Goes through the setup activity otherwise
            Intent gotToSetup = new Intent(this, SetupActivity.class);
            startActivity(gotToSetup);
        }
        cursor.close();
        db.close();
    }
}