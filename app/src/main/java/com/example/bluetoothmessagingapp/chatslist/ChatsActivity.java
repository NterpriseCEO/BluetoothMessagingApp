package com.example.bluetoothmessagingapp.chatslist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.bluetoothmessagingapp.R;
import com.example.bluetoothmessagingapp.SettingsActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ChatsActivity extends AppCompatActivity {

    private List chats = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chats);

        //Sets the toolbar at the top of the screen
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Access the bluetoothAdapter
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        //Gets a list of paired devices
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();

        //Loops through all paired devices and if they are smart phones, they are added to the chats list
        for(BluetoothDevice device: pairedDevices) {
            BluetoothClass bluetoothClass = device.getBluetoothClass();// The type of bluetooth device
            if(bluetoothClass.getDeviceClass() == BluetoothClass.Device.PHONE_SMART) {
                chats.add(device.getName());
            }
        }
        //Shows a message if no chats are available
        if(chats.size() == 0) {
            findViewById(R.id.chatsList).setVisibility(View.GONE);
            findViewById(R.id.noChats).setVisibility(View.VISIBLE);
        }

        //Creates a recyclerView using the chats adapter
        RecyclerView recyclerView = findViewById(R.id.chatsList);
        ChatsAdapter chatsAdapter = new ChatsAdapter(chats, ChatsActivity.this);

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

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        //Goes to the settings page on click
        if(menuItem.getItemId() == R.id.goToSettings) {
            Intent settingsPage = new Intent(ChatsActivity.this, SettingsActivity.class);
            startActivity(settingsPage);
        }
        return super.onOptionsItemSelected(menuItem);
    }
}