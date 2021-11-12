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

import com.example.bluetoothmessagingapp.R;
import com.example.bluetoothmessagingapp.SettingsActivity;
import com.example.bluetoothmessagingapp.bluetoothconnections.AcceptThread;
import com.example.bluetoothmessagingapp.bluetoothconnections.ConnectThread;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ChatsActivity extends AppCompatActivity {

    private final List<String[]> chats = new ArrayList<>();

    private final static int REQUEST_ENABLE_BT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chats);

        //Sets the toolbar at the top of the screen
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Accesses the bluetoothAdapter
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if(!bluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            //The thing this was replaced with sucks and now I'm stuck with deprecated code
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        }

        //Creates a recyclerView using the chats adapter
        RecyclerView recyclerView = findViewById(R.id.chatsList);
        ChatsAdapter chatsAdapter = new ChatsAdapter(chats, ChatsActivity.this);

//        AcceptThread aThread = new AcceptThread(this);
//        Thread t = new Thread(aThread);
//        t.start();

        //Gets a list of paired devices
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();

        //Loops through all paired devices and if they are smart phones, they are added to the chats list
        for(BluetoothDevice device: pairedDevices) {
            BluetoothClass bluetoothClass = device.getBluetoothClass();// The type of bluetooth device
            if(bluetoothClass.getDeviceClass() == BluetoothClass.Device.PHONE_SMART) {
                chats.add(new String[]{device.getName(), device.getAddress()});
//                ConnectThread thread = new ConnectThread(device, this);
//                Thread t2 = new Thread(thread);
//                t2.start();
//                thread.getUsername();
            }
        }
        //Shows a message if no chats are available
        if(chats.size() == 0) {
            findViewById(R.id.chatsList).setVisibility(View.GONE);
            findViewById(R.id.noChats).setVisibility(View.VISIBLE);
        }


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

    @Override
    public void onBackPressed() {
        Intent leaveApp = new Intent(Intent.ACTION_MAIN);
        leaveApp.addCategory(Intent.CATEGORY_HOME);
        leaveApp.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(leaveApp);
    }
}